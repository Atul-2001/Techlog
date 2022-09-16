package com.signature.techlog.service;

import com.signature.techlog.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public interface OAuthService extends AuthService {

    Object authenticateRequest(HttpServletRequest request);

    HttpSession startSession(HttpServletRequest request, User user);
}