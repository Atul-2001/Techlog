package com.signature.techlog.util;

public class Validator {

    public static boolean validateEmail(String email) {
        if (email != null) {
            return email.matches("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9]+\\.)+[a-zA-Z]{2,6}$");
        } else {
            return false;
        }
    }

    public static boolean validateUsername(String username) {
        if (username != null) {
            return username.matches("^[a-zA-Z\\d](?:[a-zA-Z\\d]|-(?=[a-zA-Z\\d])){0,38}$");
        } else {
            return false;
        }
    }

    public static boolean validatePhone(String phone) {
        if (phone != null) {
            return phone.matches("^(\\+?)(?:[0-9] ?){6,14}[0-9]$");
        } else {
            return false;
        }
    }

    public static boolean validateCountry(String country) {
        if (country != null) {
            return country.contains("Country") || country.contains("country") || country.contains("COUNTRY");
        } else {
            return false;
        }
    }

    public static boolean validatePassword(String password) {
        if (password != null) {
            return !password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*-+=()]).{8,20}$");
        } else {
            return false;
        }
    }
}