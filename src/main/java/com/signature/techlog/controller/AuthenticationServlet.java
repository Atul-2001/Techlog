package com.signature.techlog.controller;

import com.signature.techlog.service.AuthService;
import com.signature.techlog.service.impl.GoogleOAuthService;
import com.signature.techlog.service.impl.StandardAuthService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Authentication Servlet",
        value = {"/auth/ajax/register",
                "/auth/ajax/login",
                "/oauth/google/register",
                "/oauth/google/login"})
public class AuthenticationServlet extends HttpServlet {

    private final Logger LOGGER = LogManager.getLogger(AuthenticationServlet.class);

    private AuthService authService, oauthService;

    @Override
    public void init() throws ServletException {
        super.init();
        ServletContext context = getServletContext();
        this.authService = (AuthService) context.getAttribute(StandardAuthService.class.getSimpleName());
        this.oauthService = (AuthService) context.getAttribute(GoogleOAuthService.class.getSimpleName());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            switch (req.getServletPath()) {
                case "/auth/ajax/register":
                    out.print(authService.register(req));
                    break;
                case "/auth/ajax/login":
                    out.print(authService.authenticate(req));
                    break;
                case "/oauth/google/register":
                    out.print(oauthService.register(req));
                    break;
                case "/oauth/google/login":
                    out.print(oauthService.authenticate(req));
                    break;
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}