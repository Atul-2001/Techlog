package com.signature.techlog.controller;

import com.signature.techlog.data.OTPHandler;
import com.signature.techlog.data.UserHandler;
import com.signature.techlog.model.Message;
import com.signature.techlog.model.OTP;
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

@WebServlet(name = "PasswordResetServlet", value = "/user/password/reset")
public class ResetPasswordServlet extends HttpServlet {

    private final Logger LOGGER = LogManager.getLogger(ResetPasswordServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            String email = req.getParameter("email");
            String password = req.getParameter("password_field");
            String repeatPassword = req.getParameter("repeat_password_field");

            if (email == null || email.isEmpty()) {
                out.print(Message.builder()
                        .setLevel(Message.Level.ERROR)
                        .setContent("Invalid Request!")
                        .toJSON());
            } else if ((password == null || password.isEmpty()) || (repeatPassword == null || repeatPassword.isEmpty())) {
                out.print(Message.builder()
                        .setLevel(Message.Level.WARNING)
                        .setContent("All fields are required!")
                        .toJSON());
            } else if (!password.equals(repeatPassword)) {
                out.print(Message.builder()
                        .setLevel(Message.Level.ERROR)
                        .setContent("Password did not match!")
                        .toJSON());
            } else if (Validator.validatePassword(password)) {
                out.print(Message.builder()
                        .setLevel(Message.Level.WARNING)
                        .setContent("Password length should be between 8 to 20 characters. " +
                                "It should contain at least one uppercase letter(A-Z), " +
                                "one lowercase letter(a-z), one digit(0-9) and " +
                                "one special character")
                        .toJSON());
            } else {
                HttpSession session = req.getSession(false);
                if (session == null) {
                    out.print(Message.builder()
                            .setLevel(Message.Level.ERROR)
                            .setContent("Invalid Request!")
                            .toJSON());
                } else {
                    UserHandler handler = UserHandler.getInstance();
                    User user = handler.getUser(email);

                    if (user == null) {
                        out.print(Message.builder()
                                .setLevel(Message.Level.ERROR)
                                .setContent("Invalid Request!")
                                .toJSON());
                    } else {
                        OTP otp = new OTPHandler().getOTP(session.getId(), user);
                        if (otp == null || !otp.isValidated()) {
                            out.print(Message.builder()
                                    .setLevel(Message.Level.ERROR)
                                    .setContent("Invalid Request!")
                                    .toJSON());
                        } else {
                            PasswordAuthentication authentication = new PasswordAuthentication();
                            user.setPassword(authentication.hash(password.toCharArray()));

                            if (handler.updateUser(user)) {
                                if (session.getAttribute("user") == null) {
                                    session.invalidate();
                                    out.print(Message.builder()
                                            .setLevel(Message.Level.INFO)
                                            .setContent("Password changed successfully! Redirecting...")
                                            .setRedirectURI("/login")
                                            .toJSON());
                                } else {
                                    out.print(Message.builder()
                                            .setLevel(Message.Level.INFO)
                                            .setContent("Password changed successfully! Redirecting...")
                                            .setRedirectURI("/home")
                                            .toJSON());
                                }
                            } else {
                                out.print(Message.builder()
                                        .setLevel(Message.Level.ERROR)
                                        .setContent("Failed to change password, please try again!")
                                        .toJSON());
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.ERROR, ex.getMessage(), ex);
        }
    }
}