package com.signature.techlog.controller;

import com.signature.techlog.data.UserHandler;
import com.signature.techlog.model.Message;
import com.signature.techlog.model.User;
import com.signature.techlog.util.PasswordAuthentication;
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

@WebServlet(name = "LoginServlet", value = "/ajax/login")
public class LoginServlet extends HttpServlet {

    private final Logger LOGGER = LogManager.getLogger(LoginServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            String email = req.getParameter("email");
            String password = req.getParameter("password");

            if ((email == null || email.isEmpty()) || (password == null || password.isEmpty())) {
                out.print(Message.builder()
                        .setLevel(Message.Level.WARNING)
                        .setContent("All fields are required!")
                        .toJSON());
            } else {
                User user = UserHandler.getInstance().getUser(email);

                if (user == null) {
                    out.print(Message.builder()
                            .setLevel(Message.Level.ERROR)
                            .setContent("Invalid email!")
                            .toJSON());
                } else {
                    PasswordAuthentication authenticator = new PasswordAuthentication();
                    if (authenticator.authenticate(password.toCharArray(), user.getPassword())) {
                        HttpSession oldSession = req.getSession(false);
                        if (oldSession != null) {
                            oldSession.invalidate();
                        }

                        HttpSession session = req.getSession(true);
                        session.setMaxInactiveInterval(60 * 60 * 24 * 7);// 1 week
                        session.setAttribute("user", user);
                        out.print(Message.builder()
                                .setLevel(Message.Level.INFO)
                                .setContent("Login Successful! Redirecting...")
                                .toJSON());
                    } else {
                        out.print(Message.builder()
                                .setLevel(Message.Level.ERROR)
                                .setContent("Incorrect password!")
                                .toJSON());
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.ERROR, ex.getMessage(), ex);
        }
    }
}