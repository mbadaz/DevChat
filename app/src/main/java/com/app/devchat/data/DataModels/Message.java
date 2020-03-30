package com.app.devchat.data.DataModels;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Messages")
public class Message {
    public enum MessageType{
        TEXT,
        MULTIMEDIA;
    }

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    public String key;

    @ColumnInfo
    public String text;

    @ColumnInfo
    public Date time;

    @ColumnInfo
    public String sender;

    @ColumnInfo
    public MessageType type;


    @Ignore
    public Message(String text, Date time, String sender, MessageType type) {
        this.text = text;
        this.time = time;
        this.sender = sender;
        this.type = type;
    }

    public Message(){

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

    public String getKey() {
        return key;
    }

    @Override
    public boolean equals(Object obj) {
        Message message = (Message) obj;
        return message.time == time;
    }
}
