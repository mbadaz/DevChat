package com.app.devchat.data;

import com.app.devchat.data.Network.NetworkingHelper;
import com.app.devchat.data.SharedPrefs.PreferenceHelper;
import com.app.devchat.data.SqlDatabase.DbHelper;

import java.util.List;

public interface DataManager extends PreferenceHelper, NetworkingHelper, DbHelper {

    List<String> getMessages();


}
