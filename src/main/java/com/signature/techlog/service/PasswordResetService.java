package com.signature.techlog.service;

import com.signature.techlog.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

import java.util.regex.Pattern;

public class PasswordResetService {

    private final UserRepository userRepository;

    private final Pattern lowercase = Pattern.compile("[a-z]");
    private final Pattern uppercase = Pattern.compile("[A-Z]");
    private final Pattern digit = Pattern.compile("\\d");

    private PasswordResetService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static PasswordResetService getInstance(UserRepository userRepository) {
        return new PasswordResetService(userRepository);
    }

    public String validateNewPassword(HttpServletRequest request) {
        return null;
    }

    public String changeAccountPassword(HttpServletRequest request) {
        return null;
    }

    private String checkPassword(String password) {
        if (password == null || password.isEmpty()) {
            return "Password length should be between 8 to 20 characters. " +
                    "It should contain at least one uppercase letter(A-Z), " +
                    "one lowercase letter(a-z), one digit(0-9) and " +
                    "one special character.";
        }
        return null;
    }
}