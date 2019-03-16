package com.app.devchat.data.SharedPrefs;

import com.app.devchat.data.LoginMode;

/**
 * Exposes the app's Shared Preference API and should be implemented by any class that wants to
 * perform actions on the app's Shared Preferences or extended by any interface that also
 * wants to expose the app's Shared Preferences API
 */

public interface PreferencesHelper {

    int getLoginStatus();
    void setLoginStatus(LoginMode loginMode);

    String getUserName();
    void setUserName(String value);

    String getUserEmail();
    void setUserEmail(String value);

    String getUserStatus();
    void setUserStatus(String value);
}
