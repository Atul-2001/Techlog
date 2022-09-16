package com.signature.techlog.service;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface AuthService {

    Logger LOGGER = LogManager.getLogger(AuthService.class);

    String register(HttpServletRequest request);

    String authenticate(HttpServletRequest request);
}