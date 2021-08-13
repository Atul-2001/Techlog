package com.signature.techlog.model;

import com.sun.istack.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

import java.io.Serializable;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
public class OTP implements Serializable {

    @Transient
    private static final long serialVersionUID = 5L;

    @Id
    @NotNull
    @Column(name = "SESSION_ID", nullable = false)
    private String sessionID;

    @NotNull
    @Column(name = "USER_ID", nullable = false)
    private String userID;

    @NotNull
    @Column(name = "USER_EMAIL", nullable = false)
    private String email;

    @NotNull
    @Column(name = "CODE", nullable = false, length = 6)
    private int code;

    @NotNull
    @Column(name = "TIMESTAMP", nullable = false)
    private Timestamp timestamp;

    @NotNull
    @Column(name = "IS_VALIDATED", nullable = false)
    private boolean isValidated;

    public OTP() { }

    public OTP(String sessionID, String userID, String email) {
        assert sessionID != null && userID != null && email != null;
        this.sessionID = sessionID;
        this.userID = userID;
        this.email = email;
        SecureRandom random = new SecureRandom();
        this.code = random.nextInt(1000000);
        while (String.valueOf(this.code).length() != 6) {
            this.code = random.nextInt(1000000);
        }
        this.timestamp = Timestamp.from(Instant.now());
        this.isValidated = false;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }

    public boolean validate(int value) {
        Timestamp currentTimeStamp = Timestamp.from(Instant.now());

        if (currentTimeStamp.getTime() - timestamp.getTime() > 900000) { //15 min -> 900000 millisecond
            return false;
        } else {
            return this.code == value;
        }
    }

}