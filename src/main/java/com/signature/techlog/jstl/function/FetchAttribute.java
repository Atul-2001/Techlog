package com.signature.techlog.jstl.function;

import com.signature.techlog.model.User;
import jakarta.servlet.http.HttpSession;

public class FetchAttribute {

    public static User getSessionAttribute(HttpSession session, String key) {
        if (session == null) {
            return null;
        } else {
            if (session.getAttribute(key) == null) {
                return null;
            } else {
                
                return (User) session.getAttribute(key);
            }
        }
    }
}