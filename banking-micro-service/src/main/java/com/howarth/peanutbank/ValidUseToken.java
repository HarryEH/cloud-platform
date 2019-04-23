package com.howarth.peanutbank;

public class ValidUseToken {

    private String username;
    private boolean valid;

    public ValidUseToken(String username, boolean valid) {
        this.username = username;
        this.valid = valid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getUsername() {
        return username;
    }

    public boolean isValid() {
        return valid;
    }
}
