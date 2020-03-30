package com.app.devchat.data.Network;

import androidx.lifecycle.LiveData;

import com.app.devchat.data.DataModels.Message;
import com.app.devchat.data.DataModels.User;
import com.app.devchat.data.NewMessagesCallback;

import java.util.ArrayList;
import java.util.Date;

/**
 * Interface which provides methods for interacting with the app's network data source API.
 * Every class that wants to act as the app's network data source API must implement this interface
 */
public interface NetworkHelper {

    void listenForNewMessages(Date date, NewMessagesCallback callback);

    void getNewMessagesFromBackendDatabase(Date date, NewMessagesCallback callback);

    void sendMessagesToBackendDatabase(ArrayList<Message> messages);

    void setUserId(String userName);

    void addNewUserToBackEndDatabase(User user);

    LiveData<String> getOnlineStatusStream();

    boolean goOffline();
}
