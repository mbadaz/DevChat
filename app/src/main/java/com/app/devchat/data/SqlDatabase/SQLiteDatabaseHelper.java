package com.app.devchat.data.SqlDatabase;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.app.devchat.BuildConfig;
import com.app.devchat.data.DataModels.Message;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.room.Room;

/**
 * Api for the app's database functions. Uses the Room Persistence library and the Paging library
 * to load paged data from the SQL database.
 */
@Singleton
public class SQLiteDatabaseHelper implements LocalDatabaseHelper, Runnable{

    private static final String DB_NAME = BuildConfig.APPLICATION_ID + ".db";
    private LiveData<PagedList<Message>> messagesList;
    private AppDatabase db;
    private Date latestMessageDate;

    @Inject
    public SQLiteDatabaseHelper(Context context) {

        //TODO implement proper migration policy
        db = Room.databaseBuilder(context, AppDatabase.class, DB_NAME).
                fallbackToDestructiveMigration().build();
        PagedList.Config config = new PagedList.Config.Builder().
                setEnablePlaceholders(false).setPrefetchDistance(40).
                setPageSize(20).
                build();
        DataSource.Factory<Integer, Message> dataSource = db.messageDAO().getMessages();
        messagesList = new LivePagedListBuilder<>(dataSource, config).
                build();
    }

    @Override
    public LiveData<PagedList<Message>> getMessagesFromLocalDatabase() {
        return messagesList;
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

    @Override
    public void storeMessagesToLocalDatabase(ArrayList<Message> messages) {
        SaveMessagesTask task = new SaveMessagesTask(db.messageDAO(), messages);
        task.execute();
    }

    /**
     * Gets the date of the newest message in the local database
     * @return Date
     */
    @Override
    public Date getNewestMessageDate() {
        Thread thread = new Thread(this);
        thread.run();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return latestMessageDate;
    }

    @Override
    public void run() {
        Cursor cursor = db.query("SELECT * FROM Messages ORDER BY time DESC LIMIT 1", null);

        int timeColumnIndex = cursor.getColumnIndex("time");

        if (cursor.getCount() < 0) {
            // if the database is empty i.e app has been newly installed
            latestMessageDate = null;
        }else if (cursor.moveToFirst()) {
            latestMessageDate = new Date(cursor.getLong(timeColumnIndex));
        }

    }

    public void stopDatabase(){
        if (db.inTransaction()){
            db.getOpenHelper().getWritableDatabase().endTransaction();
        }
    }




}
