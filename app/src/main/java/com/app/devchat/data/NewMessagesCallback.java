package com.app.devchat.data;

import java.util.ArrayList;

/**
 * Callback interface for communicating with the for communication between
 * the app's {@link DataManager} and the {@link com.app.devchat.data.Network.NetworkHelper}'s
 * event listeners
 */
public interface NewMessagesCallback {
    void onNewMessages(ArrayList<Message> messages);
}
