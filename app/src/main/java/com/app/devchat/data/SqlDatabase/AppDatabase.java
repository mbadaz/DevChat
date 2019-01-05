package com.app.devchat.data.SqlDatabase;

import com.app.devchat.data.Message;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = Message.class, version = 4, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {

    public abstract MessageDAO messageDAO();
}
