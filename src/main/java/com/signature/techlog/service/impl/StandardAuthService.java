package com.signature.techlog.service.impl;

import com.signature.techlog.bootstrap.ApplicationContext;
import com.signature.techlog.catalog.AccountType;
import com.signature.techlog.catalog.Gender;
import com.signature.techlog.generator.SequenceGenerator;
import com.signature.techlog.generator.UsernameGenerator;
import com.signature.techlog.model.Message;
import com.signature.techlog.model.User;
import com.signature.techlog.repository.UserRepository;
import com.signature.techlog.service.AuthService;
import com.signature.techlog.util.PasswordAuthentication;
import com.signature.techlog.util.Validator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class StandardAuthService implements AuthService {

    private final Properties messages;
    private final UserRepository userRepository;

    private final String NAME = "user[profile_name]";
    private final String EMAIL = "user[profile_email]";
    private final String GENDER = "user[profile_gender]";
    private final String COUNTRY = "user[profile_country]";
    private final String PASSWORD = "user[profile_key]";

    private StandardAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.messages = ApplicationContext.getMessages();
    }

    public static StandardAuthService getInstance(UserRepository userRepository) {
        return new StandardAuthService(userRepository);
    }

    @Override
    public String register(HttpServletRequest request) {
        Map<String, String> parameters = getParameterMap(request);

        try {
            if (hasEmptyParameter(parameters.values())) {
                return Message.builder().setLevel(Message.Level.WARNING).setContent(messages.getProperty("empty.fields")).toJSON();
            } else if (!Validator.validateEmail(parameters.get(EMAIL))) {
                return Message.builder().setLevel(Message.Level.WARNING).setContent(messages.getProperty("invalid.email")).toJSON();
            } else if (Validator.validateCountry(parameters.get(COUNTRY))) {
                return Message.builder().setLevel(Message.Level.WARNING).setContent(messages.getProperty("invalid.country")).toJSON();
            } else if (!Validator.validatePassword(parameters.get(PASSWORD))) {
                return Message.builder().setLevel(Message.Level.WARNING)
                        .setContent(messages.getProperty("invalid.password")).toJSON();
            } else {
                PasswordAuthentication authenticator = new PasswordAuthentication();

                String passwordHash = authenticator.hash(parameters.get(PASSWORD).toCharArray());

                if (userRepository.existsByEmail(parameters.get(EMAIL))) {
                    return Message.builder().setLevel(Message.Level.ERROR).setContent(messages.getProperty("duplicate.auth.email")).toJSON();
                } else {
                    try {
                        User user = new User();
                        user.setId(String.valueOf(SequenceGenerator.getInstance().nextId()));
                        user.setAccountType(AccountType.REGULAR);
                        user.setName(parameters.get(NAME));
                        user.setEmail(parameters.get(EMAIL));
                        user.setUsername(UsernameGenerator.generate(user.getEmail()));
                        if (parameters.get(GENDER).equals(Gender.MALE.name())) {
                            user.setGender(Gender.MALE);
                        } else if (parameters.get(GENDER).equals(Gender.FEMALE.name())) {
                            user.setGender(Gender.FEMALE);
                        } else if (parameters.get(GENDER).equals(Gender.TRANSGENDER.name())) {
                            user.setGender(Gender.TRANSGENDER);
                        }
                        user.setCountry(parameters.get(COUNTRY));
                        user.setPassword(passwordHash);

                        if (userRepository.save(user)) {
                            return Message.builder().setLevel(Message.Level.INFO).setContent(messages.getProperty("success.auth.register")).toJSON();
                        } else {
                            return Message.builder().setLevel(Message.Level.ERROR).setContent(messages.getProperty("error.auth")).toJSON();
                        }
                    } catch (UsernameGenerator.InvalidEmailException ex) {
                        LOGGER.info(ex.getMessage());
                        return Message.builder().setLevel(Message.Level.ERROR).setContent(messages.getProperty("error.auth")).toJSON();
                    }
                }
            }
        } catch (NullPointerException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return Message.builder().setLevel(Message.Level.ERROR).setContent(messages.getProperty("error.auth")).toJSON();
        }
    }

    @Override
    public String authenticate(HttpServletRequest request) {
        Map<String, String> parameters = getParameterMap(request);

        try {
            if (hasEmptyParameter(parameters.values())) {
                return Message.builder().setLevel(Message.Level.WARNING).setContent(messages.getProperty("empty.fields")).toJSON();
            } else {
                User user = userRepository.findByEmail(parameters.get(EMAIL));

                if (user == null) {
                    return Message.builder().setLevel(Message.Level.ERROR).setContent(messages.getProperty("error.auth.login_email")).toJSON();
                } else {
                    PasswordAuthentication authenticator = new PasswordAuthentication();
                    if (authenticator.authenticate(parameters.get(PASSWORD).toCharArray(), user.getPassword())) {
                        HttpSession oldSession = request.getSession(false);
                        if (oldSession != null) { oldSession.invalidate(); }

                        HttpSession session = request.getSession(true);
                        session.setMaxInactiveInterval(60 * 60 * 24 * 7);// 1 week
                        session.setAttribute("user", user);

                        return Message.builder().setLevel(Message.Level.INFO).setContent(messages.getProperty("success.auth.login")).toJSON();
                    } else {
                        return Message.builder().setLevel(Message.Level.ERROR).setContent(messages.getProperty("error.auth.login_passwd")).toJSON();
                    }
                }
            }
        } catch (NullPointerException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return Message.builder().setLevel(Message.Level.ERROR).setContent(messages.getProperty("error.auth")).toJSON();
        }
    }

    private Map<String, String> getParameterMap(HttpServletRequest request) {
        Map<String, String> parameters = new HashMap<>();
        request.getParameterNames().asIterator().forEachRemaining(name -> parameters.put(name, request.getParameter(name)));
        return parameters;
    }

    private boolean hasEmptyParameter(Collection<String> parameters) {
        if (parameters.isEmpty()) {
            return true;
        } else {
            for (String value : parameters) {
                if (value == null || value.isEmpty()) {
                    return true;
                }
            }
            return false;
        }
    }
}