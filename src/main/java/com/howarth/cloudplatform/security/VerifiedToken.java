package com.howarth.cloudplatform.security;

public class VerifiedToken {

    private String username;
    private boolean verified;

    public VerifiedToken(String username, boolean verified) {
        this.username = username;
        this.verified = verified;
    }

    public VerifiedToken() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
