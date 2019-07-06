package com.app.devchat.data.SharedPrefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.devchat.BuildConfig;
import com.app.devchat.data.LoginMode;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Handles saving and getting data from the app's Shared Preferences storage.
 */
@Singleton
public class AppPreferenceHelper implements PreferencesHelper {
    private static final String USER_INFO_PREFERENCE_FILE_KEY = BuildConfig.APPLICATION_ID + ".user_info";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_LOGIN_STATUS = "login_status";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_STATUS = "user_status";


    private SharedPreferences mSharedPrefs;

    @Inject
    public AppPreferenceHelper(Context context) {
        mSharedPrefs = context.getSharedPreferences(USER_INFO_PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
    }

    @Override
    public LoginMode getLoginStatus() {
        return LoginMode.getMode(mSharedPrefs.getInt(KEY_LOGIN_STATUS, 0));
    }

    @Override
    public void setLoginStatus(LoginMode loginMode) {
        mSharedPrefs.edit().putInt(KEY_LOGIN_STATUS, loginMode.ordinal()).apply();
    }

    @Override
    public String getUserName() {
        return mSharedPrefs.getString(KEY_USERNAME, "none");
    }

    @Override
    public void setUserName(String value) {
        mSharedPrefs.edit().putString(KEY_USERNAME, value).apply();
    }

    @Override
    public String getUserEmail() {
        return mSharedPrefs.getString(KEY_USER_EMAIL, "anonymous");
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
