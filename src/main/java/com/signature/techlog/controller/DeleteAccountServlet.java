package com.signature.techlog.controller;

import java.io.IOException;
import java.io.PrintWriter;

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

@WebServlet(name = "Delete Account Servlet", value = "/user/account/delete")
public class DeleteAccountServlet extends HttpServlet {

    private final Logger LOGGER = LogManager.getLogger(DeleteAccountServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (PrintWriter writer = resp.getWriter()) {
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                writer.print(Message.builder().setLevel(Message.Level.ERROR).setContent("Invalid Request!").toJSON());
            } else {
                String login = req.getParameter("sudo_login");
                String confirmationPhrase = req.getParameter("confirmation_phrase");
                String password = req.getParameter("sudo_password");

                User user = (User) session.getAttribute("user");

                if (login == null || login.isEmpty()) {
                    writer.print(Message.builder().setLevel(Message.Level.ERROR)
                            .setContent("Username or email required").toJSON());
                } else if (confirmationPhrase == null || confirmationPhrase.isEmpty()) {
                    writer.print(Message.builder().setLevel(Message.Level.ERROR)
                            .setContent("Confirmation phrase required.").toJSON());
                } else if ((password == null || password.isEmpty()) && !(user.getPassword() == null || user.getPassword().isEmpty())) {
                    writer.print(Message.builder().setLevel(Message.Level.ERROR)
                            .setContent("Password required for authentication.").toJSON());
                } else {
                    UserHandler handler = UserHandler.getInstance();

                    if (handler.getUserByID(user.getId()) == null) {
                        writer.print(Message.builder().setLevel(Message.Level.ERROR).setContent("Invalid User Request!")
                                .toJSON());
                    } else {
                        if (login.equals(user.getEmail()) || login.equals(user.getUsername())) {
                            if (confirmationPhrase.equalsIgnoreCase("delete my account")) {
                                if (password == null || new PasswordAuthentication().authenticate(password.toCharArray(),
                                        user.getPassword())) {
                                    writer.print(Message.builder().setLevel(Message.Level.INFO)
                                            .setContent("User authenticated successfully.").toJSON());
                                    LOGGER.log(Level.INFO,
                                            "User : " + user.getId() + " has authenticated for deletion of account.");
                                } else {
                                    writer.print(Message.builder().setLevel(Message.Level.ERROR)
                                            .setContent("Incorrect password!").toJSON());
                                    LOGGER.log(Level.INFO, "Failed authentication for user - " + user.getId()
                                            + " | Process : Account deletion | Cause: Incorrect Password");
                                }
                            } else {
                                writer.print(Message.builder().setLevel(Message.Level.ERROR)
                                        .setContent("Incorrect confirmation phrase!").toJSON());
                            }
                        } else {
                            writer.print(Message.builder().setLevel(Message.Level.ERROR)
                                    .setContent("Incorrect username or email!").toJSON());
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (PrintWriter writer = resp.getWriter()) {
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                writer.print(Message.builder().setLevel(Message.Level.ERROR).setContent("Invalid Request!").toJSON());
            } else {
                User user = (User) session.getAttribute("user");
                UserHandler handler = UserHandler.getInstance();

                if (handler.getUserByID(user.getId()) == null) {
                    writer.print(Message.builder().setLevel(Message.Level.ERROR).setContent("Invalid User Request!")
                            .toJSON());
                } else {
                    if (handler.deleteUser(user)) {
                        session.removeAttribute("user");
                        session.setAttribute("message", Message.builder().setLevel(Message.Level.INFO)
                                .setContent("Account successfully deleted."));
                        writer.print(Message.builder().setLevel(Message.Level.INFO)
                                .setContent("Account successfully deleted.").toJSON());
                        LOGGER.log(Level.INFO, "User " + user.getId() + " has successfully deleted account.");
                    } else {
                        writer.print(Message.builder().setLevel(Message.Level.ERROR)
                                .setContent("Failed to delete account.").toJSON());
                        LOGGER.log(Level.INFO, "User " + user.getId() + " failed to delete account.");
                    }
                }
            }
        }
    }

}