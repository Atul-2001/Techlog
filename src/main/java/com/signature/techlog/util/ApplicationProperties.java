package com.signature.techlog.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ApplicationProperties {

    private static final Logger LOGGER = LogManager.getLogger(ApplicationProperties.class);

    private static final java.util.Properties properties = new java.util.Properties();

    public static void load() {
        try {
            LOGGER.info("Loading application properties...");
            properties.load(ApplicationProperties.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException ex) {
            LOGGER.error("Failed to load properties.", ex);
            LOGGER.info("Terminating application launch...");
            throw new RuntimeException();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}