package com.howarth.cloudplatform.model;

public class Token {

    private final String username;
    private final boolean isValid;

    public Token(String username, boolean isValid) {
        this.username = username;
        this.isValid = isValid;
    }

    public String getUsername() {
        return username;
    }

    public boolean isValid() {
        return isValid;
    }
}
