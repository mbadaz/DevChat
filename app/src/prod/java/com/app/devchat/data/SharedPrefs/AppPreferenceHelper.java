package com.app.devchat.data.SharedPrefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.devchat.BuildConfig;
import com.app.devchat.ThreadHelper;
import com.app.devchat.data.LoginMode;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Handles saving and getting data from the app's Shared Preferences storage.
 */
@Singleton
public class AppPreferenceHelper implements PreferencesHelper{
    private static final String USER_INFO_PREFERENCE_FILE_KEY = BuildConfig.APPLICATION_ID + ".user_info";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_LOGIN_STATUS = "login_status";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_STATUS = "user_status";

    private SharedPreferences mSharedPrefs;

    @Inject
    public AppPreferenceHelper(Context context) {

        Callable<SharedPreferences> callable = () -> context.getSharedPreferences(USER_INFO_PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        ThreadHelper<SharedPreferences> threadHelper = new ThreadHelper<>();
        mSharedPrefs = threadHelper.runBackgroundTask(callable, 1);
    }

    @Override
    public LoginMode getLoginStatus() {
        ThreadHelper<LoginMode> threadHelper = new ThreadHelper<>();
        Callable<LoginMode> callable = () -> LoginMode.getMode(mSharedPrefs.getInt(KEY_LOGIN_STATUS, 0));
        return threadHelper.runBackgroundTask(callable, 1);
    }

    @Override
    public void setLoginStatus(LoginMode loginMode) {
        mSharedPrefs.edit().putInt(KEY_LOGIN_STATUS, loginMode.ordinal()).apply();
    }

    @Override
    public String getUserName() {
        ThreadHelper<String> threadHelper = new ThreadHelper<>();
        Callable<String> callable = () -> mSharedPrefs.getString(KEY_USERNAME, "none");
        return threadHelper.runBackgroundTask(callable, 1);
    }

    @Override
    public void setUserName(String value) {
        mSharedPrefs.edit().putString(KEY_USERNAME, value).apply();
    }

    @Override
    public String getUserEmail() {
        ThreadHelper<String> threadHelper = new ThreadHelper<>();
        Callable<String> callable = () -> mSharedPrefs.getString(KEY_USER_EMAIL, "anonymous");
        return threadHelper.runBackgroundTask(callable, 1);
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
        ThreadHelper<String> threadHelper = new ThreadHelper<>();
        Callable<String> callable = () -> mSharedPrefs.getString(KEY_USER_STATUS, "");
        return threadHelper.runBackgroundTask(callable, 1);
    }

}
