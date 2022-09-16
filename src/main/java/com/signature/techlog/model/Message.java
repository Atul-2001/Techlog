package com.signature.techlog.model;

public class Message {

    private Level level;
    private String content;
    private String redirect;

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

    public String getRedirect() {
        return redirect;
    }

    public Message setRedirect(String redirect) {
        this.redirect = redirect;
        return this;
    }

    public String toJSON() {
        if (redirect == null || redirect.isEmpty()) {
            return "{" +
                    "\"level\":\"" + level.name() + "\"," +
                    "\"content\":\"" + content + "\"" +
                    "}";
        } else {
            return "{" +
                    "\"level\":\"" + level.name() + "\"," +
                    "\"content\":\"" + content + "\"," +
                    "\"redirect\":\"" + redirect + "\"" +
                    "}";
        }
    }
}