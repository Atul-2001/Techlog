package com.signature.techlog.model;

public class Message {

    private Level level;
    private String content;
    private String redirectURI;

    public enum Level {
        ALL,
        TRACE,
        DEBUG,
        INFO,
        WARNING,
        ERROR
    }

    private Message() {
    }

    public static Message builder() {
        return new Message();
    }

    public Level getLevel() {
        return level;
    }

    public Message setLevel(Level level) {
        this.level = level;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Message setContent(String content) {
        this.content = content;
        return this;
    }

    public String getRedirectURI() {
        return redirectURI;
    }

    public Message setRedirectURI(String redirectURI) {
        this.redirectURI = redirectURI;
        return this;
    }

    public String toJSON() {
        if (redirectURI == null || redirectURI.isEmpty()) {
            return "{" +
                    "\"level\":\"" + level.name() + "\"," +
                    "\"content\":\"" + content + "\"" +
                    "}";
        } else {
            return "{" +
                    "\"level\":\"" + level.name() + "\"," +
                    "\"content\":\"" + content + "\"," +
                    "\"redirectURI\":\"" + redirectURI + "\"" +
                    "}";
        }
    }
}