package com.signature.techlog.controller;

import com.signature.techlog.data.UserHandler;
import com.signature.techlog.model.Message;
import com.signature.techlog.model.User;
import com.signature.techlog.util.IDTokenVerifier;
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

@WebServlet(name = "SocialAuthGoogle", value = "/auth/google")
public class GoogleAuthentication extends HttpServlet {

    private final Logger LOGGER = LogManager.getLogger(GoogleAuthentication.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            try {
                String id_token = req.getParameter("id_token");

                User user = IDTokenVerifier.verifyGoogleIdToken(id_token);
                if (user == null) {
                    out.print(Message.builder()
                            .setLevel(Message.Level.ERROR)
                            .setContent("Failed to authenticate user, it may be due to an Invalid ID token or internal exception.")
                            .toJSON());
                } else {
                    User existingUser = UserHandler.getInstance().getUser(user.getEmail());

                    if (existingUser == null) {
                        if (UserHandler.getInstance().saveUser(user)) {
                            startSession(req, out, user);
                        } else {
                            out.print(Message.builder()
                                    .setLevel(Message.Level.ERROR)
                                    .setContent("Failed to create account, please try again!")
                                    .toJSON());
                        }
                    } else if (existingUser.getPassword() == null) {
                        if (user.getId().equals(existingUser.getId())) {
                            if (user.getName().equals(existingUser.getName()) &&
                                    user.getProfile().equals(existingUser.getProfile())) {
                                startSession(req, out, existingUser);
                            } else {
                                if (UserHandler.getInstance().updateUser(user)) {
                                    startSession(req, out, user);
                                } else {
                                    out.print(Message.builder()
                                            .setLevel(Message.Level.ERROR)
                                            .setContent("Failed to authenticate account, please try again!")
                                            .toJSON());
                                }
                            }
                        } else {
                            out.print(Message.builder()
                                    .setLevel(Message.Level.ERROR)
                                    .setContent("This email already exist as a social user account.")
                                    .toJSON());
                        }
                    } else {
                        out.print(Message.builder()
                                .setLevel(Message.Level.ERROR)
                                .setContent("This email already exist as a regular account and it will requires password to login!")
                                .toJSON());
                    }
                }
            } catch (Exception ex) {
                LOGGER.log(Level.ERROR, ex.getMessage(), ex);
                out.print(Message.builder()
                        .setLevel(Message.Level.ERROR)
                        .setContent("Something bad happened, please try again!")
                        .toJSON());
            }
        } catch (IOException ex) {
            LOGGER.log(Level.ERROR, ex.getMessage(), ex);
        }
    }

    private void startSession(HttpServletRequest request, PrintWriter out, User user) {
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }

        HttpSession session = request.getSession(true);
        session.setMaxInactiveInterval(60 * 60 * 24 * 7);// 1 week
        session.setAttribute("user", user);
        out.print(Message.builder()
                .setLevel(Message.Level.INFO)
                .setContent("Login Successful! Redirecting...")
                .toJSON());
    }
}