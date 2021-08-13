package com.signature.techlog.generator;

import com.signature.techlog.data.UserHandler;
import com.signature.techlog.util.Validator;

import java.security.SecureRandom;

public class UsernameGenerator {

    private static final UserHandler handler = UserHandler.getInstance();

    public static String generate(String email) throws InvalidEmailException {
        if (Validator.validateEmail(email)) {
            String username = email.split("@")[0];
            String[] suggestions = username.split("\\.");

            int salt = 0, count = 0;
            int bound = 10000;
            do {
                for (String suggested : suggestions) {
                    if (salt > 0) {
                        suggested = suggested.concat("-").concat(String.valueOf(salt));
                    }
                    if (!handler.checkUsername(suggested)) {
                        return suggested;
                    }
                }

                if (count >= bound) {
                    bound *= 10;
                    count = 0;
                }
                salt = new SecureRandom().nextInt(bound);
                count++;
            } while (true);
        } else {
            throw new InvalidEmailException(email);
        }
    }

    public static class InvalidEmailException extends Exception {
        public InvalidEmailException() {
            this("");
        }

        public InvalidEmailException(String message) {
            super("Invalid email pattern: " + message);
        }
    }
}