package com.app.devchat.data;

public enum LoginMode {
    LOGGED_OUT(0),
    GOOGLE_LOGIN(1),
    FB_LOGIN(2),
    EMAIL_LOGIN(3);

    private final int mode;

    LoginMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }
}
