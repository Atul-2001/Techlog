package com.signature.techlog.catalog;

public enum Profile {

    MALE("app.user.profile.male"),
    FEMALE("app.user.profile.female"),
    TRANSGENDER("app.user.profile.transgender");

    public final String label;

    Profile(String label) {
        this.label = label;
    }
}