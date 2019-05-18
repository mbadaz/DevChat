package com.app.devchat.data.SqlDatabase;

import com.app.devchat.data.DataModels.Message;

import java.util.List;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MessageDAO {

    @Query("SELECT * FROM Messages ORDER BY time DESC")
    DataSource.Factory<Integer, Message> getMessages();

    @Insert
    void saveMessages(List<Message> messages);
}
