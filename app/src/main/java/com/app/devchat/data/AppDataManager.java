package com.app.devchat.data;

import android.util.Log;

import com.app.devchat.data.Network.NetworkHelper;
import com.app.devchat.data.SharedPrefs.PreferencesHelper;
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
import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

@Singleton
public class AppDataManager implements DataManager {

    private static final String TAG = AppDataManager.class.getSimpleName();
    private PreferencesHelper preferencesHelper;
    private DbHelper dbHelper;
    private NetworkHelper networkHelper;
    public String userName;

    @Inject
    public AppDataManager(PreferencesHelper preferencesHelper,
                           DbHelper dbHelper, NetworkHelper networkHelper){

        this.preferencesHelper = preferencesHelper;
        this.dbHelper = dbHelper;
        this.networkHelper = networkHelper;
    }

    // ********* Firebase database access methods **********************

    @Override
    public void listenForNewMessages(EventListener<QuerySnapshot> eventListener) {
        networkHelper.listenForNewMessages(eventListener);
    }

    @Override
    public void getNewMessagesFromBackend(Date date, OnSuccessListener listener) {
        networkHelper.getNewMessagesFromBackend(date, listener);
    }

    @Override
    public void sendMessagesToBackend(ArrayList<Message> messages) {
        networkHelper.sendMessagesToBackend(messages);
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
     * Is passed to {@link #listenForNewMessages(EventListener)}
     * @param queryDocumentSnapshots Document snapshots received from Firebase
     * @param e
     */
    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
        if(!queryDocumentSnapshots.getMetadata().isFromCache()){
            // if the query snapshot
            if(e == null && queryDocumentSnapshots != null){
                List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                ArrayList<Message> newMessages = new ArrayList<>();

                if(documentSnapshots.size() > 0){
                    for(DocumentSnapshot documentSnapshot : documentSnapshots){
                        String text = documentSnapshot.getString("text");
                        Date time = documentSnapshot.getDate("time");
                        String sender = documentSnapshot.getString("sender");

                        if(!sender.equals(userName)) {
                            newMessages.add(new Message(text, time, sender));
                        }
                    }

                    storeMessagesToLocal(newMessages);
                }
                Log.d(TAG, "snapshot from Firebase" + queryDocumentSnapshots.toString());
            }else {
                Log.e(TAG, e.getMessage());
            }

        }

    }

    /**
     * {@link OnSuccessListener} listener for listening for the completion of a data request
     * query to the Firebase database. Is passed to {@link #getNewMessagesFromBackend(Date, OnSuccessListener)}
     * @param queryDocumentSnapshots : Response data from the Firebase database
     */
    @Override
    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
        List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
        ArrayList<Message> messages = new ArrayList<>();
        if(documentSnapshots.size() > 0){
            for(DocumentSnapshot documentSnapshot : documentSnapshots){
                String text = documentSnapshot.getString("text");
                Date time = documentSnapshot.getDate("time");
                String sender = documentSnapshot.getString("sender");
                messages.add(new Message(text, time, sender));
            }
            Log.d(TAG, "snapshot from Firebase" + queryDocumentSnapshots.toString());
            storeMessagesToLocal(messages);
        }

        listenForNewMessages(this);
    }
}
