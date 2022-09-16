package com.signature.techlog.controller;

import com.signature.techlog.model.Message;
import com.signature.techlog.model.User;
import com.signature.techlog.repository.UserRepository;
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

@WebServlet(name = "Manage Account Servlet",
        value = {"/account/rename_check",
                "/user/username/rename"})
public class ManageAccountServlet extends HttpServlet {

    private final Logger LOGGER = LogManager.getLogger(ManageAccountServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getServletPath().equals("/account/rename_check")) {
            HttpSession session = req.getSession(false);
            String given_username = req.getParameter("given_username");

            if (given_username == null || given_username.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            } else if (session == null || session.getAttribute("user") == null) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                User user = (User) session.getAttribute("user");
                UserRepository handler = UserRepository.getInstance();
                if (user.getUsername() != null && user.getUsername().equals(given_username)) {
                    resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                } else if (handler.checkUsername(given_username)) {
                    resp.setStatus(HttpServletResponse.SC_CONFLICT);
                } else {
                    resp.setStatus(HttpServletResponse.SC_ACCEPTED);
                }
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            LOGGER.log(Level.TRACE, "Request failed for : " + req.getRequestURL());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        if (req.getServletPath().equals("/user/username/rename")) {
            try (PrintWriter writer = resp.getWriter()) {
                HttpSession session = req.getSession(false);
                if (session == null || session.getAttribute("user") == null) {
                    writer.print(
                            Message.builder().setLevel(Message.Level.ERROR).setContent("Invalid Request!").toJSON());
                } else {
                    String given_username = req.getParameter("login");

                    if (given_username == null || given_username.isEmpty()) {
                        writer.print(Message.builder().setLevel(Message.Level.ERROR)
                                .setContent("Username cannot be empty!").toJSON());
                    } else if (!Validator.validateUsername(given_username)) {
                        writer.print(Message.builder().setLevel(Message.Level.ERROR).setContent(
                                "Username may only contain alphanumeric characters or single hyphens, and cannot begin or end with a hyphen.")
                                .toJSON());
                    } else {
                        User user = (User) session.getAttribute("user");
                        UserRepository handler = UserRepository.getInstance();

                        if (handler.checkUsername(given_username)) {
                            writer.print(Message.builder().setLevel(Message.Level.ERROR)
                                    .setContent("Username must be different.").toJSON());
                        } else {
                            user.setUsername(given_username);
                            if (handler.update(user)) {
                                session.setAttribute("user", user);
                                writer.print(Message.builder().setLevel(Message.Level.INFO).setContent(
                                        "Username updated successfully — <a onclick='messageModal.hide();' href='http://localhost:8080/"
                                                + user.getUsername() + "'>view your profile.</a>")
                                        .toJSON());
                            } else {
                                writer.print(Message.builder().setLevel(Message.Level.ERROR)
                                        .setContent("Failed to update username!").toJSON());
                            }
                        }
                    }
                }
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            LOGGER.log(Level.TRACE, "Request failed for : " + req.getRequestURL());
        }
    }
    
}