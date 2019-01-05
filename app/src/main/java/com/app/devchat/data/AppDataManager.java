package com.app.devchat.data;

import android.app.Application;
import android.util.Log;

import com.app.devchat.data.Network.AppNetworkHelper;
import com.app.devchat.data.Network.NetworkHelper;
import com.app.devchat.data.SharedPrefs.AppPreferenceHelper;
import com.app.devchat.data.SharedPrefs.PreferencesHelper;
import com.app.devchat.data.SqlDatabase.AppDbHelper;
import com.app.devchat.data.SqlDatabase.DbHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

public class AppDataManager implements DataManager {

    private static final String TAG = AppDataManager.class.getSimpleName();
    private static AppDataManager dataManager = null;
    private PreferencesHelper preferencesHelper;
    private DbHelper dbHelper;
    private NetworkHelper networkHelper;
    private String userName;

    public LiveData<PagedList<Message>> liveMessages;

    private AppDataManager(Application context){

        preferencesHelper = new AppPreferenceHelper(context);
        dbHelper = new AppDbHelper(context);
        networkHelper = new AppNetworkHelper();
        loadData();
    }

    public static AppDataManager getInstace(Application context){
        if (dataManager == null){
            dataManager = new AppDataManager(context);
            return dataManager;
        }else {
            return dataManager;
        }
    }

    private void loadData() {
        liveMessages = getMessagesFromLocal();

        if(liveMessages.getValue().isEmpty()){
            // if no message in local database get new messages from backend database
            getNewMessagesFromBackend(new Date(), this);
        }else {
            // get from the Firebase storage messages newer than the most recent in the local database
            int count = liveMessages.getValue().getLoadedCount();
            Message latestMessage = liveMessages.getValue().get(count - 1);
            getNewMessagesFromBackend(latestMessage.getTime(), this);
        }

        userName = getUserName();
    }

    public void sendNewMessage(String text){
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(new Message(text, new Date(), userName));
        storeMessagesToLocal(messages);
        sendMessagesToBackend(messages);
    }

    // ********* Firebase database access methods **********************

    @Override
    public void getNewMessagesFromBackend(Date date, EventListener<QuerySnapshot> eventListener) {
        networkHelper.getNewMessagesFromBackend(date, eventListener);
    }

    @Override
    public void getMessagesFromBackend(OnSuccessListener listener) {
        networkHelper.getMessagesFromBackend(listener);
    }

    @Override
    public void sendMessagesToBackend(ArrayList<Message> messages) {
        networkHelper.sendMessagesToBackend(messages);
        storeMessagesToLocal(messages);
    }


    // **************** Shared preferences data access methods *****************

    @Override
    public int getLoginStatus() {
        return preferencesHelper.getLoginStatus();
    }

    @Override
    public void setLoginStatus(DataManager.LoginMode loginMode) {
        preferencesHelper.setLoginStatus(loginMode);
    }

    @Override
    public String getUserName() {
        return preferencesHelper.getUserName();
    }

    @Override
    public void setUserName(String value) {
        preferencesHelper.setUserName(value);
    }

    @Override
    public String getUserEmail() {
        return preferencesHelper.getUserEmail();
    }

    @Override
    public void setUserEmail(String value) {
        preferencesHelper.setUserEmail(value);
    }

    @Override
    public String getUserStatus() {
        return preferencesHelper.getUserStatus();
    }

    @Override
    public void setUserStatus(String value) {
        preferencesHelper.setUserStatus(value);
    }


    // ************* Local database access methods **************

    @Override
    public LiveData<PagedList<Message>> getMessagesFromLocal() {
        return dbHelper.getMessagesFromLocal();
    }

    @Override
    public void storeMessagesToLocal(ArrayList<Message> messages) {
        dbHelper.storeMessagesToLocal(messages);
    }

    @Override
    public void updateUserInfo(String username, String userEmail, LoginMode loginMode) {
        setUserName(username);
        setUserEmail(userEmail);
        setLoginStatus(loginMode);
    }

    /**
     * Listener for listening for realtime new messages snapshots from Firebase database.
     * Is passed to {@link #getNewMessagesFromBackend(Date, EventListener)}
     * @param queryDocumentSnapshots Document snapshots received from Firebase
     * @param e
     */
    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
        if(e == null && queryDocumentSnapshots != null){
            List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
            ArrayList<Message> newMessages = new ArrayList<>();

            for(DocumentSnapshot documentSnapshot : documentSnapshots){
                String text = documentSnapshot.getString("text");
                Date time = documentSnapshot.getDate("time");
                String sender = documentSnapshot.getString("sender");
                newMessages.add(new Message(text, time, sender));
            }

            // store new messages to local database
            storeMessagesToLocal(newMessages);
        }else {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * {@link OnSuccessListener} listener for listening for the completion of a data request
     * query to the Firebase database. Is passed to {@link #getMessagesFromBackend(OnSuccessListener)}
     * @param queryDocumentSnapshots : Response data from the Firebase database
     */
    @Override
    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
        List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
        ArrayList<Message> messages = new ArrayList<>();

        for(DocumentSnapshot documentSnapshot : documentSnapshots){
            String text = documentSnapshot.getString("text");
            Date time = documentSnapshot.getDate("time");
            String sender = documentSnapshot.getString("sender");
            messages.add(new Message(text, time, sender));
        }

        storeMessagesToLocal(messages);
    }
}
