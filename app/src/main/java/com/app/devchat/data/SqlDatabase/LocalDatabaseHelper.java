package com.app.devchat.data.SqlDatabase;

import com.app.devchat.data.DataModels.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

/**
 * Interface which exposes methos for interacting with the app's local database.
 * Every class that wants to act as the app's local messages database must implement this interface
 */

public interface LocalDatabaseHelper {

    LiveData<PagedList<Message>> getMessagesFromLocalDatabase();

    void storeMessagesToLocalDatabase(ArrayList<Message> messages);

    Date getNewestMessageDate();
}
