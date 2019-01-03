package com.app.devchat.data;

import com.app.devchat.data.Network.NetworkingHelper;
import com.app.devchat.data.SharedPrefs.PreferencesHelper;
import com.app.devchat.data.SqlDatabase.DbHelper;

/**
 * Interface which provides the contract methods for interacting with the app's
 * datasources i.e {@link com.app.devchat.data.SharedPrefs.AppPreferenceHelper},
 * {@link com.app.devchat.data.SqlDatabase.AppDbHelper} &
 */
public interface DataManager extends PreferencesHelper, NetworkingHelper, DbHelper {

    enum LoginMode{
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

    void updateUserInfo(String username, String userEmail, DataManager.LoginMode loginMode);

}
