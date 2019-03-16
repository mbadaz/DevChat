package com.app.devchat.data.Network;

import com.app.devchat.data.Message;
import com.app.devchat.data.NewMessagesCallback;

import java.util.ArrayList;
import java.util.Date;

/**
 * Interface which provides methods for interacting with the app's network data source API.
 * Every class that wants to act as the app's network data source API must implement this interface
 */
public interface NetworkHelper {

    void listenForNewMessages(Date date);

    void getNewMessagesFromBackendDatabase(Date date);

    void sendMessagesToBackendDatabase(ArrayList<Message> messages);

    void setNewMessagesCallBack(NewMessagesCallback callBack);

    void setUserName(String userName);
}
