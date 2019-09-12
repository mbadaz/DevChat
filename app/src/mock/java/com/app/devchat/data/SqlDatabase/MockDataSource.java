package com.app.devchat.data.SqlDatabase;

import androidx.annotation.NonNull;
import androidx.paging.ItemKeyedDataSource;

import com.app.devchat.data.DataModels.Message;

import java.util.ArrayList;
import java.util.Date;

/**
 * Mock {@link androidx.paging.DataSource}
 */
public class MockDataSource extends ItemKeyedDataSource<Integer, Message> {

    private ArrayList<Message> mockDatabase = new ArrayList<>();

    MockDataSource() {

        for (int x = 0; x < 50; x++) {
            mockDatabase.add(
                    new Message(
                            null, "message"+ x, new Date(), "mock", Message.MessageType.TEXT));
        }
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Message> callback) {
        callback.onResult(mockDatabase);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Message> callback) {
        callback.onResult(mockDatabase.subList(params.key, mockDatabase.size()-1));
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Message> callback) {
        callback.onResult(mockDatabase.subList(0, params.key));
    }


    @NonNull
    @Override
    public Integer getKey(@NonNull Message item) {
        return mockDatabase.indexOf(item);
    }

    ArrayList<Message> getMockDatabase() {
        return mockDatabase;
    }
}
