package com.app.devchat.data;

import java.util.ArrayList;

/**
 * Callback interface implemented by {@link AppDataManager} and called by the listeners in the
 * {@link com.app.devchat.data.Network.NetworkHelper} implementation.
 */
public interface NewMessagesCallback {
    void onNewMessages(ArrayList<Message> messages);
}
