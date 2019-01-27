package com.app.devchat.chat;

import android.app.Application;

import com.app.devchat.data.AppDataManager;
import com.app.devchat.data.Message;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

public class ChatActivityViewModel extends AndroidViewModel {

    AppDataManager dataManager;
    LiveData<PagedList<Message>> liveMessages;
    private boolean hasDoneInitialLoad = false;

    public ChatActivityViewModel(@NonNull Application application) {
        super(application);
        dataManager = AppDataManager.getInstace(application);
        liveMessages = dataManager.liveMessages;
    }

    void sendMessage(String text){
        dataManager.sendNewMessage(text);
    }

    void getNewMessages(Date date){
        if (!hasDoneInitialLoad){
            dataManager.getNewMessages(date);
            hasDoneInitialLoad = true;
        }
    }

    void listenForNewMessages(Date date){
        dataManager.listenForNewMessages(dataManager, date);
    }
}
