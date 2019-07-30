package com.app.devchat.data;

import com.app.devchat.data.Network.NetworkHelper;
import com.app.devchat.data.SharedPrefs.PreferencesHelper;
import com.app.devchat.data.SqlDatabase.LocalDatabaseHelper;

/**
 * Provides the interface for interacting with the app's data sources.
 * Any class that wants to act as the app's data manager must implement this interface
 */
public interface DataManager extends PreferencesHelper, NetworkHelper, LocalDatabaseHelper, NewMessagesCallback{

    void updateUserInfo(String username, String userEmail, String userPhoto, LoginMode loginMode);

    void setBackgroundMode(boolean lightMode);

    boolean getIsBackgroundMode();

}
