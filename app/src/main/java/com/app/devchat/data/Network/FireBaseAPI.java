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
public class FireBaseAPI implements NetworkHelper, EventListener<QuerySnapshot>, OnSuccessListener<QuerySnapshot>, OnCompleteListener<DocumentReference>{
    private static final String TAG = FireBaseAPI.class.getSimpleName();
    private final CollectionReference chatsRef;
    private final CollectionReference usersRef;
    private ListenerRegistration listenerRegistration;
    private static NewMessagesCallback newMessagesCallback;
    private static String userName;
    private final FirebaseFirestore firebaseFirestore;

    public FireBaseAPI() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        chatsRef = firebaseFirestore.collection("chats");
        usersRef = firebaseFirestore.collection("users");
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
        if(listenerRegistration == null){
            listenerRegistration = chatsRef.whereGreaterThan("time", new Timestamp(date)).addSnapshotListener(this);
        }else{
            // Replace old event listener and replace with new one that uses date of latest message in local database.
            listenerRegistration.remove();
            listenerRegistration = chatsRef.whereGreaterThan("time", new Timestamp(date)).addSnapshotListener(this);
        }
    }

    /**
     * Get all messages from from the messages collection with date newer than the given date
     * @param date : Date of the most recent message in the local messages SQL database
     * @param callback : Callback for communicating back with the App's {@link com.app.devchat.data.DataManager}
     */
    @Override
    public void getNewMessagesFromBackendDatabase(Date date, NewMessagesCallback callback) {
        setNewMessagesCallback(callback);
        chatsRef.whereGreaterThan("time", new Timestamp(date)).get().addOnSuccessListener(this);
    }

    /**
     * Send new messages to the messages collection
     */
    @Override
    public void sendMessagesToBackendDatabase(ArrayList<Message> messages) {
        for(Message message : messages){
            //TODO implement success listener
            chatsRef.add(message).addOnCompleteListener(this);
        }
    }

    /**
     * Sends the new User to the users collection
     * @param user
     */
    @Override
    public void addNewUserToBackEndDatabase(User user) {
        usersRef.add(user);
    }

    @Override
    public void setUserName(String userName) {
        FireBaseAPI.userName = userName;
    }

    public void setNewMessagesCallback(NewMessagesCallback callback){
        newMessagesCallback = callback;
    }


    /**
     * {@link OnSuccessListener} listener for listening for the completion of a data request
     * query to the Firebase database.
     * @param queryDocumentSnapshots : Response data from the Firebase database
     */
    @Override
    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
        if (!queryDocumentSnapshots.getMetadata().isFromCache()) {
            List<DocumentSnapshot> snapshots = queryDocumentSnapshots.getDocuments();
            ArrayList<Message> newMessages = new ArrayList<>();

            for (DocumentSnapshot snapshot:snapshots) {
                String sender = snapshot.getString("sender");
                if (sender != null && !sender.equals(userName)) {
                    String key = snapshot.getId();
                    String text = snapshot.getString("text");
                    Date date = snapshot.getDate("time");
                    newMessages.add(new Message(key, text, date, sender, Message.MessageType.TEXT));
                }
            }

            if (newMessages.size() > 0 ) {
                // save new messages to local database
                newMessagesCallback.onNewMessages(newMessages);
            } else {
                newMessagesCallback.onNewMessages(null);
            }

        }
    }

    /**
     * Listener for listening for realtime new messages snapshots from Firebase database.
     * @param queryDocumentSnapshots Document snapshots received from Firebase
     * @param e
     */
    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
        if (e == null) {
            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.getMetadata().isFromCache()) {
                // if the query snapshot is not from cache proceed to get new messages
                List<DocumentSnapshot> snapshots = queryDocumentSnapshots.getDocuments();
                ArrayList<Message> newMessages = new ArrayList<>();

                for (DocumentSnapshot snapshot:snapshots){
                    String sender = snapshot.getString("sender");
                    if (sender != null && !sender.equals(userName)){
                        String key = snapshot.getId();
                        String text = snapshot.getString("text");
                        Date date = snapshot.getDate("time");
                        newMessages.add(new Message(key, text, date, sender, Message.MessageType.TEXT));
                    }
                }

                if(newMessages.size() > 0 ){
                    newMessagesCallback.onNewMessages(newMessages);
                } else {
                    newMessagesCallback.onNewMessages(null);
                }

                Log.d(TAG, "snapshot from Firebase" + queryDocumentSnapshots.getMetadata().toString());
            }

        } else {
            // Error occurred in getting messages
            Log.e(TAG, e.getMessage());
        }

    }

    @Override
    public void onComplete(@NonNull Task task) {
        if (task.isSuccessful()){

        }
    }

    /**
     * Remove  Listener
     */
    public void disable() {
        if (listenerRegistration != null){
            listenerRegistration.remove();
        }

        firebaseFirestore.disableNetwork();
    }

    public void enable(){
        firebaseFirestore.enableNetwork();
    }
}
