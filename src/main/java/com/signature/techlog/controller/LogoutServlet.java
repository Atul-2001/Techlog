package com.signature.techlog.controller;

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

import com.signature.techlog.model.User;

@WebServlet(name = "LogoutServlet", value = "/user/logout")
public class LogoutServlet extends HttpServlet {

    private final Logger LOGGER = LogManager.getLogger(LogoutServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            LOGGER.log(Level.INFO, "User - " + user.getId() + " is logging out!");
            session.removeAttribute("user");
            session.invalidate();
        }

        resp.sendRedirect("/");
    }
}