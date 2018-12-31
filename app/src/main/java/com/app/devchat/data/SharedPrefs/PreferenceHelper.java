package com.app.devchat.data.SharedPrefs;

import com.app.devchat.BuildConfig;

/**
 * Exposes the app's Shared Preference API and should be implemented by any class that wants to
 * perform actions on the app's Shared Preferences or extended by any interface that also
 * wants to expose the app's Shared Preferences API
 */

public interface PreferenceHelper {
    String USER_INFO_PREFERENCE_FILE_KEY = BuildConfig.APPLICATION_ID + ".user_info";
    String KEY_USERNAME = "username";
    String KEY_LOGIN_STATUS = "login_status";
    int LOGGED_IN = 1;
    int LOGGED_OUT = 0;
    String KEY_USER_EMAIL = "user_email";
    String KEY_USER_STATUS = "user_status";

    String getLoginStatus();
    void setLoginStatus(int value);

    String getUserName();
    void setUserName(String value);

    String getUserEmail();
    void setUserEmail(String value);

    String getUserStatus();
    void setUserStatus(String value);
}
