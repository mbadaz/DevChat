package com.app.devchat.data.SharedPrefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Handles saving and getting data from the app's Shared Preferences storage.
 */

public class AppPreferenceHelper implements PreferenceHelper {

    private SharedPreferences mSharedPrefs;

    public AppPreferenceHelper(Context context) {
        mSharedPrefs = context.getSharedPreferences(USER_INFO_PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
    }

    @Override
    public String getLoginStatus() {
        return mSharedPrefs.getString(KEY_LOGIN_STATUS, "");
    }

    @Override
    public void setLoginStatus(int value) {
        mSharedPrefs.edit().putInt(KEY_LOGIN_STATUS, value).apply();
    }

    @Override
    public String getUserName() {
        return mSharedPrefs.getString(KEY_USERNAME, "");
    }

    @Override
    public void setUserName(String value) {
        mSharedPrefs.edit().putString(KEY_USERNAME, value).apply();
    }

    @Override
    public String getUserEmail() {
        return mSharedPrefs.getString(KEY_USER_EMAIL, "");
    }

    @Override
    public void setUserEmail(String value) {
        mSharedPrefs.edit().putString(KEY_USER_EMAIL, value).apply();
    }

    @Override
    public void setUserStatus(String value) {
        mSharedPrefs.edit().putString(KEY_USER_STATUS, value).apply();
    }

    @Override
    public String getUserStatus() {
        return mSharedPrefs.getString(KEY_USER_STATUS, "");
    }


}
