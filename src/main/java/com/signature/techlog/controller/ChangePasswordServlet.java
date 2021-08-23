package com.signature.techlog.controller;

import com.signature.techlog.data.UserHandler;
import com.signature.techlog.model.Message;
import com.signature.techlog.model.User;
import com.signature.techlog.util.PasswordAuthentication;
import com.signature.techlog.util.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

@WebServlet(name = "Change Password Servlet", value = {"/users/password", "/account/password"})
public class ChangePasswordServlet extends HttpServlet {

    private final Logger LOGGER = LogManager.getLogger(ChangePasswordServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            switch (request.getServletPath()) {
                case "/users/password":
                    out.print(validateUserNewPassword(request, response));
                    break;
                case "/account/password":
                    out.print(changeAccountPassword(request, response));
                    break;
                default:
                    out.print(Message.builder()
                            .setLevel(Message.Level.ERROR)
                            .setContent("Request URI not found : " + request.getRequestURI() + " : " + request.getRequestURL() + " : " + request.getServletPath())
                            .toJSON());
                    break;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.ERROR, ex.getMessage(), ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private String validateUserNewPassword(HttpServletRequest request, HttpServletResponse response) {
        String userPassword = request.getParameter("user[password]");
        if (userPassword == null || userPassword.isEmpty()) {
            response.setStatus(422);
            return Message.builder().setLevel(Message.Level.WARNING).setContent("Password cannot be empty.").toJSON();
        } else {
            if (Validator.validatePassword(userPassword)) {
                response.setStatus(HttpServletResponse.SC_OK);
                return Message.builder().setLevel(Message.Level.INFO).setContent("It's Ok").toJSON();
            } else {
                response.setStatus(422);
                return Message.builder()
                        .setLevel(Message.Level.ERROR)
                        .setContent(checkPassword(userPassword))
                        .toJSON();
            }
        }
    }

    private String changeAccountPassword(HttpServletRequest request, HttpServletResponse response) {
        String oldPassword = request.getParameter("user[old_password]");
        if (oldPassword == null || oldPassword.isEmpty()) {
            return Message.builder().setLevel(Message.Level.WARNING).setContent("Old password cannot be empty.").toJSON();
        } else {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                return Message.builder().setLevel(Message.Level.ERROR).setContent("Invalid request!").toJSON();
            } else {
                final User user = (User) session.getAttribute("user");
                UserHandler handler = UserHandler.getInstance();
                if (handler.getUserByID(user.getId()) == null) {
                    return Message.builder().setLevel(Message.Level.ERROR).setContent("Invalid user session!").toJSON();
                } else {
                    final PasswordAuthentication authentication = new PasswordAuthentication();
                    if (authentication.authenticate(oldPassword.toCharArray(), user.getPassword())) {
                        String newPassword = request.getParameter("user[password]");
                        String newPasswordConfirmation = request.getParameter("user[password_confirmation]");
                        if (newPassword == null || newPassword.isEmpty() || newPasswordConfirmation == null || newPasswordConfirmation.isEmpty()) {
                            return Message.builder().setLevel(Message.Level.WARNING).setContent("New password cannot be empty.").toJSON();
                        } else {
                            if (Validator.validatePassword(newPassword)) {
                                if (newPassword.equals(newPasswordConfirmation) &&
                                        !authentication.hash(newPassword.toCharArray()).equals(authentication.hash(newPasswordConfirmation.toCharArray()))) {
                                    user.setPassword(new PasswordAuthentication().hash(newPassword.toCharArray()));
                                    if (handler.updateUser(user)) {
                                        session.setAttribute("user", user);
                                        return Message.builder().setLevel(Message.Level.INFO).setContent("Password changes successfully.").toJSON();
                                    } else {
                                        return Message.builder().setLevel(Message.Level.ERROR).setContent("Failed to change password! Please try again....").toJSON();
                                    }
                                } else {
                                    return Message.builder()
                                            .setLevel(Message.Level.ERROR)
                                            .setContent("New and Repeat Password did not match!")
                                            .toJSON();
                                }
                            } else {
                                return Message.builder()
                                        .setLevel(Message.Level.ERROR)
                                        .setContent("Password length should be between 8 to 64 characters. " +
                                                "It should contain at least one uppercase letter(A-Z), " +
                                                "one lowercase letter(a-z), one digit(0-9) and " +
                                                "one special character.")
                                        .toJSON();
                            }
                        }
                    } else {
                        return Message.builder().setLevel(Message.Level.WARNING).setContent("Incorrect old password.").toJSON();
                    }
                }
            }
        }
    }

    private final Pattern lowercase = Pattern.compile("[a-z]");
    private final Pattern uppercase = Pattern.compile("[A-Z]");
    private final Pattern digit = Pattern.compile("\\d");

    private String checkPassword(String password) {
        if (password == null || password.isEmpty()) {
            return "Password length should be between 8 to 20 characters. " +
                    "It should contain at least one uppercase letter(A-Z), " +
                    "one lowercase letter(a-z), one digit(0-9) and " +
                    "one special character.";
        } else {
            String msg = "Password";
            if (password.matches("^\\w{1,8}$")) {
                msg = msg.concat(" is too short (minimum is 8 characters)");
            }

            if (!lowercase.matcher(password).find()) {
                if (!msg.equals("Password")) {
                    msg = msg.concat(",");
                }
                msg = msg.concat(" needs at least 1 lowercase letter");
            }

            if (!uppercase.matcher(password).find()) {
                if (!msg.equals("Password")) {
                    msg = msg.concat(",");
                }
                msg = msg.concat(" needs at least 1 uppercase letter");
            }

            if (!digit.matcher(password).find()) {
                if (!msg.equals("Password")) {
                    msg = msg.concat(",");
                }
                msg = msg.concat(" needs at least 1 number");
            }

            if (password.matches("^\\w{1,4}$")) {
                if (!msg.equals("Password")) {
                    msg = msg.concat(",");
                }
                msg = msg.concat(" cannot include your login");
            }

            if (!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*-+=()]).{8,64}$")) {
                if (!msg.equals("Password")) {
                    msg = msg.concat(", and");
                }
                if (password.toCharArray().length <= 64) {
                    msg = msg.concat(" is in a list of passwords commonly used on other websites");
                } else {
                    msg = msg.concat(" should be between 8 to 64 character");
                }
            }

            if (msg.equals("Password")) {
                return "";
            } else {
                return msg;
            }
        }
    }
}