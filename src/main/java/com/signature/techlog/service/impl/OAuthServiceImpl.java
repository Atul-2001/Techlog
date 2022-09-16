package com.signature.techlog.service.impl;

import com.signature.techlog.bootstrap.ApplicationContext;
import com.signature.techlog.model.User;
import com.signature.techlog.repository.UserRepository;
import com.signature.techlog.service.OAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Properties;

public abstract class OAuthServiceImpl implements OAuthService {

    protected final Properties messages;
    protected final UserRepository userRepository;

    public OAuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.messages = ApplicationContext.getMessages();
    }

    @Override
    public abstract Object authenticateRequest(HttpServletRequest request);

    @Override
    public abstract String register(HttpServletRequest request);

    @Override
    public abstract String authenticate(HttpServletRequest request);

    @Override
    public HttpSession startSession(HttpServletRequest request, User user) {
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }

        HttpSession session = request.getSession(true);
        session.setMaxInactiveInterval(60 * 60 * 24 * 7);// 1 week
        session.setAttribute("user", user);
        return session;
    }
}