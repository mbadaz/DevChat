package com.app.devchat.data.SharedPrefs;

import com.app.devchat.data.LoginMode;

/**
 * Interface that exposes methods for interacting with the app's shared preferences
 */

public interface PreferencesHelper {

    LoginMode getLoginStatus();
    void setLoginStatus(LoginMode loginMode);

    String getUserName();
    void setUserName(String value);

    String getUserEmail();
    void setUserEmail(String value);

    String getUserStatus();
    void setUserStatus(String value);
}
