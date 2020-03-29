package com.app.devchat.ui;

import android.app.Application;

import com.app.devchat.data.DataManager;
import com.app.devchat.data.Message;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;
@Singleton
public class ChatActivityViewModel extends AndroidViewModel {

    DataManager dataManager;
    LiveData<PagedList<Message>> liveMessages;
    boolean hasDoneIntialLoad = false;

    @Inject
    public ChatActivityViewModel(@NonNull Application application, DataManager dataManager) {
        super(application);
        this.dataManager = dataManager;
    }

    void initializeData(){
        liveMessages = dataManager.getMessagesFromLocalDatabase();
    }

    void sendMessage(String text){
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(new Message(text, new Date(), dataManager.getUserName()));
        dataManager.storeMessagesToLocalDatabase(messages);
        dataManager.sendMessagesToBackendDatabase(messages);
    }

    public void listenForNewMessages(Date date){
        dataManager.listenForNewMessages(date);
    }

    String getUserName(){
        return dataManager.getUserName();
    }

    public void getNewMessages(Date date){
        if(!hasDoneIntialLoad){
            dataManager.getNewMessagesFromBackendDatabase(date);
            hasDoneIntialLoad = true;
        }
    }

}
