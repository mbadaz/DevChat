package com.app.devchat.data.Network;

import com.app.devchat.chat.Message;

import java.util.List;

/**
 *Networking interface class which should be implemented by any class
 * that wants to perform the app's networking actions or extended by an interface
 * that wants to expose the app's api. *
 */
public interface NetworkingHelper {

    List<Message> getNewMessages();
    void sendMessages(List<Message> messages);
}
