package com.app.devchat.data.Network;

import android.util.Log;

import com.app.devchat.data.DataModels.Message;
import com.app.devchat.data.DataModels.User;
import com.app.devchat.data.NewMessagesCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Singleton;

import androidx.annotation.NonNull;

/**
 * Api for accessing the Firebase realtime database
 */

@Singleton
public class FireBaseAPI implements NetworkHelper{
    private static final String TAG = FireBaseAPI.class.getSimpleName();


    private static NewMessagesCallback newMessagesCallback;
    private static String userId;
    private ArrayList<Message> MockFirebaseDatabase = new ArrayList<>();



    public FireBaseAPI() {
    }

    /**
     * Get only new messages from Firebase database newer date than the most recent message in the local database
     */
    @Override
    public void listenForNewMessages(Date date, NewMessagesCallback callback) {
        if(newMessagesCallback == null ){
            setNewMessagesCallback(callback);
        }
        updateOnNewMessageListener(date);
    }

    /**
     * Updates the {@link EventListener} for listening for new message updates from
     * the Firebase realtime database
     * @param date is the date that is the basis of the listener's query
     */
    private void updateOnNewMessageListener(Date date) {

    }

    /**
     * Get all messages from from the messages collection with date newer than the given date
     * @param date : Date of the most recent message in the local messages SQL database
     * @param callback : Callback for communicating back with the App's {@link com.app.devchat.data.DataManager}
     */
    @Override
    public void getNewMessagesFromBackendDatabase(Date date, NewMessagesCallback callback) {

    }

    /**
     * Send new messages to the messages collection setNewMessagesCallback(callback);
        chatsRef.whereGreaterThan("time", new Timestamp(date)).get().addOnSuccessListener(this);
     */
    @Override
    public void sendMessagesToBackendDatabase(ArrayList<Message> messages) {
        for(Message message : messages){




        }
    }

    /**
     * Sends the new User to the users collection
     * @param user
     */
    @Override
    public void addNewUserToBackEndDatabase(User user) {

    }

    @Override
    public void setUserId(String userName) {
        FireBaseAPI.userId = userName;
    }

    public void setNewMessagesCallback(NewMessagesCallback callback){
        newMessagesCallback = callback;
    }


    /**
     * Remove  Listener
     */
    public void disable() {


    }

    public void enable(){

    }
}
