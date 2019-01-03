package com.app.devchat.chat;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Messages")
public class Message {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    @ColumnInfo
    public String text;

    @ColumnInfo
    public Date time;


    public Message(String text, Date time) {
        this.text = text;
        this.time = time;
    }


    public String getText() {
        return text;
    }

    public Date getTime() {
        return time;
    }
}
