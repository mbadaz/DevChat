package com.app.devchat.data.SqlDatabase;

import android.app.Application;
import android.os.AsyncTask;

import com.app.devchat.BuildConfig;
import com.app.devchat.chat.Message;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.room.Room;

/**
 * Api for the app's database functions. Uses the Room Persistence library and the Paging library
 * to load paged data from the SQL database.
 */
public class AppDbHelper implements DbHelper {

    private static final String DB_NAME = BuildConfig.APPLICATION_ID + ".db";
    private LiveData<PagedList<Message>> messagesList;
    private AppDatabase db;

    public AppDbHelper(Application application) {
        db = Room.databaseBuilder(application, AppDatabase.class, DB_NAME).
                fallbackToDestructiveMigration().build();
        messagesList = new LivePagedListBuilder<>(db.messageDAO().
                getMessages(), 5).setInitialLoadKey(10).
                build();
    }

    @Override
    public LiveData<PagedList<Message>>getMessages() {
        return messagesList;
    }

    @Override
    public void storeMessages(ArrayList<Message> messages) {
        SaveMessagesTask task = new SaveMessagesTask(db.messageDAO(), messages);
        task.execute();
    }

    /**
     * AsyncTask class for saving {@link Message}s to the database asynchronously
     */
    private static class SaveMessagesTask extends AsyncTask<Void,Void,Void>{
        MessageDAO messageDAO;
        ArrayList<Message> messages;

        SaveMessagesTask(MessageDAO messageDAO, ArrayList<Message> messages) {
            this.messageDAO = messageDAO;
            this.messages = messages;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            messageDAO.saveMessages(messages);
            return null;
        }
    }


}
