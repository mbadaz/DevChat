package com.app.devchat.data;

public enum LoginMode {
    LOGGED_OUT,
    GOOGLE_LOGIN,
    FB_LOGIN,
    EMAIL_LOGIN,
    ANONYMOUS_LOGIN;

    public static LoginMode getMode(int mode) {
        switch (mode) {
            case 1:
                return GOOGLE_LOGIN;
            case 2:
                return FB_LOGIN;
            case 3:
                return EMAIL_LOGIN;
            case 4:
                return ANONYMOUS_LOGIN;
        }
        return LOGGED_OUT;
    }


}
