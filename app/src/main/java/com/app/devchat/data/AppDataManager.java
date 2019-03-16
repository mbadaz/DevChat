package com.app.devchat.data;

import android.app.Application;

import com.app.devchat.NewMessageNotification;
import com.app.devchat.data.Network.NetworkHelper;
import com.app.devchat.data.SharedPrefs.PreferencesHelper;
import com.app.devchat.data.SqlDatabase.LocalDatabase;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

@Singleton
public class AppDataManager implements DataManager {

    private static final String TAG = AppDataManager.class.getSimpleName();
    private PreferencesHelper preferencesHelper;
    private LocalDatabase dbHelper;
    private NetworkHelper networkHelper;
    private String userName;
    private String userEmail;
    private int userLoginStatus;
    private String userStatus;

    private Application application;

    @Inject
    public AppDataManager(PreferencesHelper preferencesHelper,
                          LocalDatabase dbHelper, NetworkHelper networkHelper, Application application){

        this.preferencesHelper = preferencesHelper;
        this.dbHelper = dbHelper;
        this.networkHelper = networkHelper;
        setNewMessagesCallBack(this);
        networkHelper.setUserName(getUserName());
        this.application = application;

        loadData();
    }

    private void loadData() {
        userLoginStatus = preferencesHelper.getLoginStatus();
        userName =  preferencesHelper.getUserName();
        userEmail = preferencesHelper.getUserEmail();
        userStatus = preferencesHelper.getUserStatus();
    }


    // ********* Backend database access methods **********************
    @Override
    public void listenForNewMessages(Date date) {
        networkHelper.listenForNewMessages(date);
    }

    @Override
    public void getNewMessagesFromBackendDatabase(Date date) {
        networkHelper.getNewMessagesFromBackendDatabase(date);
    }

    @Override
    public void sendMessagesToBackendDatabase(ArrayList<Message> messages) {
        networkHelper.sendMessagesToBackendDatabase(messages);
    }

    @Override
    public void setNewMessagesCallBack(NewMessagesCallback callBack){
        networkHelper.setNewMessagesCallBack(callBack);
    }


    // **************** Shared preferences data access methods *****************
    @Override
    public int getLoginStatus() {
        return userLoginStatus;
    }

    @Override
    public void setLoginStatus(LoginMode loginMode) {
        preferencesHelper.setLoginStatus(loginMode);
        userLoginStatus = loginMode.getMode();
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String value) {
        preferencesHelper.setUserName(value);
        userName = value;
    }

    @Override
    public String getUserEmail() {
        return userEmail;
    }

    @Override
    public void setUserEmail(String value) {
        preferencesHelper.setUserEmail(value);
        userEmail = value;
    }

    @Override
    public String getUserStatus() {
        return userStatus;
    }

    @Override
    public void setUserStatus(String value) {
        preferencesHelper.setUserStatus(value);
        userStatus = value;
    }


    // ************* Local database access methods **************
    @Override
    public LiveData<PagedList<Message>> getMessagesFromLocalDatabase() {
        return dbHelper.getMessagesFromLocalDatabase();
    }

    @Override
    public void storeMessagesToLocalDatabase(ArrayList<Message> messages) {
        dbHelper.storeMessagesToLocalDatabase(messages);
    }

    @Override
    public void updateUserInfo(String username, String userEmail, LoginMode loginMode) {
        setUserName(username);
        setUserEmail(userEmail);
        setLoginStatus(loginMode);
    }

    @Override
    public void onNewMessages(ArrayList<Message> messages) {
        storeMessagesToLocalDatabase(messages);
        NewMessageNotification.notify(application, messages);
    }

}
