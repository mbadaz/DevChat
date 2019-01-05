package com.app.devchat.data.Network;

import com.app.devchat.data.Message;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

/**
 * Api for accessing the Firebase realtime database
 */

public class AppNetworkHelper implements NetworkHelper {

    private final CollectionReference chatsRef;
    private final CollectionReference usersRef;

    public AppNetworkHelper() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        chatsRef = db.collection("chats");
        usersRef = db.collection("users");
    }

    /**
     * Get only new messages from Firebase database newer date than the most recent message in the local database
     * @param date : Date of the most recent message in the local messages SQL database
     * @param eventListener : Callback method to {@link com.app.devchat.data.AppDataManager}
     */
    @Override
    public void getNewMessagesFromBackend(Date date, EventListener<QuerySnapshot> eventListener) {

        chatsRef.whereGreaterThan("time", date).addSnapshotListener(eventListener);
    }

    /**
     * Get all messages from Firestore database
     * @param listener : Event callback to {@link com.app.devchat.data.AppDataManager}
     */
    @Override
    public void getMessagesFromBackend(OnSuccessListener<QuerySnapshot> listener) {

        chatsRef.get().addOnSuccessListener(listener);

    }


    /**
     * Send new messages to the Firebase Database
     */
    @Override
    public void sendMessagesToBackend(ArrayList<Message> messages) {

        for(Message message : messages){
            //TODO implement success listener
            chatsRef.add(message);
        }
    }
}
