package com.app.devchat.data.Network;

import android.os.Build;
import android.util.Log;

import com.app.devchat.data.Message;
import com.app.devchat.data.NewMessagesCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Nullable;
import javax.inject.Singleton;

/**
 * Api for accessing the Firebase realtime database
 */

@Singleton
public class FireBaseAPI implements NetworkHelper, EventListener<QuerySnapshot>, OnSuccessListener<QuerySnapshot>{
    private static final String TAG = FireBaseAPI.class.getSimpleName();
    private final CollectionReference chatsRef;
    private final CollectionReference usersRef;
    private Query realTimeQuery;
    private ListenerRegistration listenerRegistration;
    private static NewMessagesCallback newMessagesCallback;
    private static String userName;

    public FireBaseAPI() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.getApp().setAutomaticResourceManagementEnabled(true);
        chatsRef = db.collection("chats");
        usersRef = db.collection("users");
    }

    /**
     * Get only new messages from Firebase database newer date than the most recent message in the local database
     */
    @Override
    public void listenForNewMessages(Date date) {
        if(listenerRegistration == null){
           listenerRegistration = chatsRef.whereGreaterThan("time", new Timestamp(date)).addSnapshotListener(this);
        }else{
            // Replace old event lister with new one.
            listenerRegistration.remove();
            listenerRegistration = chatsRef.whereGreaterThan("time", new Timestamp(date)).addSnapshotListener(this);
        }
    }

    /**
     * Get all messages from Firestore database
     * @param date : Date of the most recent message in the local messages SQL database
     */
    @Override
    public void getNewMessagesFromBackendDatabase(Date date) {

        chatsRef.whereGreaterThan("time", new Timestamp(date)).get().addOnSuccessListener(this);

    }


    /**
     * Send new messages to the Firebase Database
     */
    @Override
    public void sendMessagesToBackendDatabase(ArrayList<Message> messages) {

        for(Message message : messages){
            //TODO implement success listener
            chatsRef.add(message);
        }
    }

    @Override
    public void setNewMessagesCallBack(NewMessagesCallback callBack) {
        FireBaseAPI.newMessagesCallback = callBack;
    }

    @Override
    public void setUserName(String userName) {
        FireBaseAPI.userName = userName;
    }


    /**
     * {@link OnSuccessListener} listener for listening for the completion of a data request
     * query to the Firebase database.
     * @param queryDocumentSnapshots : Response data from the Firebase database
     */
    @Override
    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
        if(!queryDocumentSnapshots.getMetadata().isFromCache()){
            ArrayList<Message> newMessages = (ArrayList) queryDocumentSnapshots.toObjects(Message.class);

            if(newMessages.size() > 0 ){
                //remove messages from this user to prevent duplicate messages in the local database.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    newMessages.removeIf(message -> message.getSender().equals(userName));
                }else {
                    for(Message message : newMessages){
                        if (message.getSender().equals(userName)){
                            newMessages.remove(message);
                        }
                    }
                }

                newMessagesCallback.onNewMessages(newMessages);
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
        if (e == null){
            if(queryDocumentSnapshots != null && !queryDocumentSnapshots.getMetadata().isFromCache()){
                // if the query snapshot is not from cache proceed to get new messages

                ArrayList<Message> newMessages = (ArrayList) queryDocumentSnapshots.toObjects(Message.class);

                if(e == null && newMessages.size() >  0){
                    // remove messages by this user to prevent duplicate entries in local database
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        newMessages.removeIf(message -> message.getSender().equals(userName));
                    }else {
                        for(Message message : newMessages){
                            if (message.getSender().equals(userName)){
                                newMessages.remove(message);
                            }
                        }
                    }

                    if(!newMessages.isEmpty()){
                        // Add messages to local database
                        newMessagesCallback.onNewMessages(newMessages);
                        return;
                    }

                    Log.d(TAG, "snapshot from Firebase" + queryDocumentSnapshots.getMetadata().toString());
                }
            }
        }else {
            Log.e(TAG, e.getMessage());
        }
    }
}
