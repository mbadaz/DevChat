package com.app.devchat.data.SqlDatabase;

import com.app.devchat.data.DataModels.Message;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = Message.class, version = 8, exportSchema = false)
@androidx.room.TypeConverters(TypeConverters.class)
public abstract class AppDatabase extends RoomDatabase {

    public abstract MessageDAO messageDAO();

}
