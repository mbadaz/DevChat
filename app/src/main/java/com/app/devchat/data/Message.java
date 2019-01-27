package com.app.devchat.data;

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

    @ColumnInfo
    public String sender;


    public Message(String text, Date time, String sender) {
        this.text = text;
        this.time = time;
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public Date getTime() {
        return time;
    }

    public String getSender() {
        return sender;
    }

    @Override
    public boolean equals(Object obj) {
        Message message = (Message) obj;
        return message.text.equals(text) && message.time == time;
    }
}
