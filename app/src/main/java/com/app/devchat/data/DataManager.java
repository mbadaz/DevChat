package com.app.devchat.data;

import com.app.devchat.data.Network.FireBaseAPI;
import com.app.devchat.data.Network.NetworkHelper;
import com.app.devchat.data.SharedPrefs.PreferencesHelper;
import com.app.devchat.data.SqlDatabase.LocalDatabase;
import com.app.devchat.data.SqlDatabase.SQLiteDatabase;

/**
 * Interface which provides the contract methods for interacting with the app's
 * datasources APIs i.e
 * {@link com.app.devchat.data.SharedPrefs.AppPreferenceHelper}
 * {@link SQLiteDatabase}
 * {@link FireBaseAPI }
 */
public interface DataManager extends PreferencesHelper, NetworkHelper, LocalDatabase, NewMessagesCallback{


    void updateUserInfo(String username, String userEmail, LoginMode loginMode);

}
