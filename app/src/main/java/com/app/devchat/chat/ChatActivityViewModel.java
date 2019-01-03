package com.app.devchat.chat;

import android.app.Application;

import com.app.devchat.data.SqlDatabase.AppDbHelper;

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

public class ChatActivityViewModel extends AndroidViewModel {
    private AppDbHelper dbHelper;
    LiveData<PagedList<Message>> messagesList;

    public ChatActivityViewModel(@NonNull Application application) {
        super(application);
        dbHelper = new AppDbHelper(application);
        messagesList = dbHelper.getMessages();
    }

    void addMessage(){
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(new Message("message 1", new Date()));
        messages.add(new Message("message 2", new Date()));
        messages.add(new Message("message 3", new Date()));
        dbHelper.storeMessages(messages);
    }

}
