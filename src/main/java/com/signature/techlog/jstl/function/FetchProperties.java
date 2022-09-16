package com.signature.techlog.jstl.function;

import com.signature.techlog.util.ApplicationProperties;

public class FetchProperties {

    public static String getProperty(String key) {
        return ApplicationProperties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return ApplicationProperties.getProperty(key, defaultValue);
    }
}