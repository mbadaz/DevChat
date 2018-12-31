package com.app.devchat.chat;

import java.util.Date;

public class Message {
    String text;
    Date time;


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
