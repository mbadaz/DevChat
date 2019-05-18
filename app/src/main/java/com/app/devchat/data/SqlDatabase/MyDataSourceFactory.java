package com.app.devchat.data.SqlDatabase;

import com.app.devchat.data.DataModels.Message;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

public class MyDataSourceFactory extends DataSource.Factory<Integer, Message> {
    /**
     * Create a DataSource.
     * <p>
     * The DataSource should invalidate itself if the snapshot is no longer valid. If a
     * DataSource becomes invalid, the only way to query more data is to create a new DataSource
     * from the Factory.
     * <p>
     * {@link androidx.paging.LivePagedListBuilder} for example will construct a new PagedList and DataSource
     * when the current DataSource is invalidated, and pass the new PagedList through the
     * {@code LiveData<PagedList>} to observers.
     *
     * @return the new DataSource.
     */
    @NonNull
    @Override
    public DataSource<Integer, Message> create() {
        return null;
    }
}
