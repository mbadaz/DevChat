package com.app.devchat.data.SqlDatabase;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.app.devchat.data.DataModels.Message;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

/**
 * Mock implementation of {@link LocalDatabaseHelper} that uses a mocked {@link DataSource}
 * for the paging library
 */

public class SQLiteDatabaseHelper implements LocalDatabaseHelper {


    private LiveData<PagedList<Message>> messages;
    private final MockDataSource mockDataSource;

    @Inject
    public SQLiteDatabaseHelper() {

        mockDataSource = new MockDataSource();
        PagedList.Config config = new PagedList.Config.Builder().
                setEnablePlaceholders(false).setPrefetchDistance(20).
                setInitialLoadSizeHint(10).
                setPageSize(10).
                build();
        DataSource.Factory<Integer, Message> factory = new DataSource.Factory<Integer, Message>() {
            @NonNull
            @Override
            public DataSource<Integer, Message> create() {
               return mockDataSource;
            }
        };
        messages = new LivePagedListBuilder<>(factory, config).build();

    }

    @Override
    public LiveData<PagedList<Message>> getMessagesFromLocalDatabase() {
        return messages;
    }

    @Override
    public void storeMessagesToLocalDatabase(ArrayList<Message> messages) {


    }

    @Override
    public Date getNewestMessageDate() {
        ArrayList<Message> mockDatabase = mockDataSource.getMockDatabase();
        return mockDatabase.get(mockDatabase.size()-1).time;
    }
}
