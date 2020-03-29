package com.app.devchat.data;

import android.app.Application;
import android.util.Log;

import com.app.devchat.NewMessageNotification;
import com.app.devchat.data.DataModels.Message;
import com.app.devchat.data.DataModels.User;
import com.app.devchat.data.Network.NetworkHelper;
import com.app.devchat.data.SharedPrefs.PreferencesHelper;
import com.app.devchat.data.SqlDatabase.LocalDatabaseHelper;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

/**
 * This is the apps {@link DataManager} implementation and handles processing of all the app's
 * data. For consistency the in the processing of data, AppDataManager regards the local database
 * i.e {@link LocalDatabaseHelper} as the app's single source of truth.
 * This means that the UI only observes the local database for its data.
 * New messages from the remote database are simply added to the local database and the UI will update accordingly.
 */
@Singleton
public class AppDataManager implements DataManager {

    private static final String TAG = AppDataManager.class.getSimpleName();
    private PreferencesHelper preferencesHelper;
    private LocalDatabaseHelper localDatabaseHelper;
    private NetworkHelper networkHelper;
    private String userName;
    private String userEmail;
    private LoginMode userLoginStatus;
    private String userStatus;
    private Application application;
    private static DataManager dataManager;
    private boolean backgroundMode = true;

    @Inject
    public AppDataManager(PreferencesHelper preferencesHelper,
                          LocalDatabaseHelper dbHelper, NetworkHelper networkHelper, Application application){

        this.preferencesHelper = preferencesHelper;
        this.localDatabaseHelper = dbHelper;
        this.networkHelper = networkHelper;
        this.application = application;

        loadData();

        networkHelper.setUserId(getUserName());
    }

    private void loadData() {
        userLoginStatus = preferencesHelper.getLoginStatus();
        userName =  preferencesHelper.getUserName();
        userEmail = preferencesHelper.getUserEmail();
        userStatus = preferencesHelper.getUserStatus();
    }

    /**
     * Sets whether the data manager is running in the background or foreground.
     * @param mode
     */
    @Override
    public synchronized void setBackgroundMode(boolean mode) {
        backgroundMode = mode;
    }


    // ********* Backend database access methods **********************
    @Override
    public void listenForNewMessages(Date date, NewMessagesCallback callback) {
        networkHelper.listenForNewMessages(date,  callback);
    }

    @Override
    public void getNewMessagesFromBackendDatabase(Date date, NewMessagesCallback callback) {
        networkHelper.getNewMessagesFromBackendDatabase(date, callback);
    }

    @Override
    public void sendMessagesToBackendDatabase(ArrayList<Message> messages) {
        networkHelper.sendMessagesToBackendDatabase(messages);
    }

    @Override
    public void addNewUserToBackEndDatabase(User user) {
        networkHelper.addNewUserToBackEndDatabase(user);
    }


    // **************** Shared preferences data access methods *****************
    @Override
    public LoginMode getLoginStatus() {
        return userLoginStatus;
    }

    @Override
    public void setLoginStatus(LoginMode loginMode) {
        preferencesHelper.setLoginStatus(loginMode);
        userLoginStatus = loginMode;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String value) {

    }

    @Override
    public void setUserId(String value) {
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

    @Override
    public void updateUserInfo(String username, String userEmail, String userPhoto, LoginMode loginMode) {
        setUserId(username);
        setUserEmail(userEmail);
        setLoginStatus(loginMode);
    }



    // ************* Local database access methods **************
    @Override
    public LiveData<PagedList<Message>> getMessagesFromLocalDatabase() {
        return localDatabaseHelper.getMessagesFromLocalDatabase();
    }

    @Override
    public void storeMessagesToLocalDatabase(ArrayList<Message> messages) {
        localDatabaseHelper.storeMessagesToLocalDatabase(messages);
    }

    @Override
    public Date getNewestMessageDate() {
        return localDatabaseHelper.getNewestMessageDate();
    }


    /**
     * New messages callback method
     * @param messages
     */
    @Override
    public void onNewMessages(ArrayList<Message> messages) {
        if(messages != null) {
            storeMessagesToLocalDatabase(messages);

            // update new messages listener
            Date newestMessageDate = messages.get(messages.size() - 1).getTime();

            if (backgroundMode) {
                // Show new messages' notification
                NewMessageNotification.notify(application, messages);
            }

            listenForNewMessages(newestMessageDate, this);
        }

        Log.d(TAG, "New messages saved to local database");
    }

    @Override
    public synchronized boolean getIsBackgroundMode() {
        return backgroundMode;
    }
}
