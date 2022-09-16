package com.signature.techlog.service.impl;

import com.signature.techlog.catalog.AccountType;
import com.signature.techlog.model.Message;
import com.signature.techlog.model.User;
import com.signature.techlog.repository.UserRepository;
import com.signature.techlog.util.TokenVerifier;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class GoogleOAuthService extends OAuthServiceImpl {

    private GoogleOAuthService(UserRepository userRepository) {
        super(userRepository);
    }

    public static GoogleOAuthService getInstance(UserRepository userRepository) {
        return new GoogleOAuthService(userRepository);
    }

    @Override
    public Object authenticateRequest(HttpServletRequest request) {
        if (request.getAttribute("user") == null) {
            String credential = request.getParameter("credential");
            if (credential == null || credential.isEmpty()) {
                return Message.builder()
                        .setLevel(Message.Level.ERROR)
                        .setContent(messages.getProperty("empty.google_credential"))
                        .toJSON();
            } else {
                User user = TokenVerifier.verifyGoogleCredential(credential);
                if (user == null) {
                    return Message.builder()
                            .setLevel(Message.Level.ERROR)
                            .setContent(messages.getProperty("invalid.token"))
                            .toJSON();
                } else {
                    request.setAttribute("user", user);
                    return user;
                }
            }
        } else {
            return request.getAttribute("user");
        }
    }

    @Override
    public String register(HttpServletRequest request) {
        Object response = authenticateRequest(request);
        if (response instanceof String) {
            return (String) response;
        } else if (response instanceof User) {
            User user = (User) response;
            if (super.userRepository.existsById(user.getId())) {
                return authenticate(request);
            } else {
                if (super.userRepository.existsByEmail(user.getEmail())) {
                    return Message.builder()
                            .setLevel(Message.Level.ERROR)
                            .setContent(messages.getProperty("duplicate.oauth.email"))
                            .toJSON();
                } else {
                    user.setAccountType(AccountType.GOOGLE);
                    if (super.userRepository.save(user)) {
                        HttpSession session = super.startSession(request, user);
                        session.setAttribute("message", messages.getProperty("welcome"));
                        return Message.builder().setLevel(Message.Level.INFO).setContent(messages.getProperty("success.oauth.register")).setRedirect("/settings/profile").toJSON();
                    } else {
                        return Message.builder().setLevel(Message.Level.ERROR).setContent(messages.getProperty("error.oauth.register")).toJSON();
                    }
                }
            }
        } else {
            return "{\"level\":\"\",\"content\":\"\"}";
        }
    }

    @Override
    public String authenticate(HttpServletRequest request) {
        Object response = authenticateRequest(request);
        if (response instanceof String) {
            return (String) response;
        } else if (response instanceof User) {
            User user = (User) response;
            user = super.userRepository.findById(user.getId());
            if (user == null) {
                return register(request);
            } else {
                super.startSession(request, user);
                return Message.builder().setLevel(Message.Level.INFO).setContent(messages.getProperty("success.oauth.login")).setRedirect("/home").toJSON();
            }
        } else {
            return "{\"level\":\"\",\"content\":\"\"}";
        }
    }
}