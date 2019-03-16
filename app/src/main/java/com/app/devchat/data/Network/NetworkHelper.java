package com.app.devchat.data.Network;

import com.app.devchat.data.Message;
import com.app.devchat.data.NewMessagesCallback;

import java.util.ArrayList;
import java.util.Date;

/**
 * Provides the methods contract for interacting with the {@link FireBaseAPI}
 */
public interface NetworkHelper {

    void listenForNewMessages(Date date);

    void getNewMessagesFromBackendDatabase(Date date);

    void sendMessagesToBackendDatabase(ArrayList<Message> messages);

    void setNewMessagesCallBack(NewMessagesCallback callBack);

    void setUserName(String userName);
}
