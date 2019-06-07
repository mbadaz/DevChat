package com.app.devchat.data.SqlDatabase;

import com.app.devchat.data.DataModels.Message;
import com.google.j2objc.annotations.ObjectiveCName;

import java.util.ArrayList;
import java.util.List;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;

@Dao
public interface MessageDAO {

    @Query("SELECT * FROM Messages ORDER BY time DESC")
    DataSource.Factory<Integer, Message> getMessages();

    @Insert(onConflict = OnConflictStrategy.REPLACE
    )
    void saveMessages(List<Message> messages);
}
