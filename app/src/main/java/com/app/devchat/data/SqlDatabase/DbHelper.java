package com.app.devchat.data.SqlDatabase;

import com.app.devchat.chat.Message;

import java.util.List;

/**
 * Exposes the app's SQLite Database API and should be implemented by any class that wants
 * to access the SQLite database or extended by any interface that wants to expose the
 * database API
 */

public interface DbHelper {

    List<Message> getStoredMessages();
    void storeMessages(List<Message> messages);
}
