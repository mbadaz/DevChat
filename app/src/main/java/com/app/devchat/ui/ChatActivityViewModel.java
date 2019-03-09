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
    String userName;

    @Inject
    public ChatActivityViewModel(@NonNull Application application, DataManager dataManager) {
        super(application);
        this.dataManager = dataManager;
        liveMessages = dataManager.getMessagesFromLocal();
        userName = dataManager.getUserName();
    }

    void initializeData(){
        if(liveMessages.getValue() != null){
            getNewMessages(liveMessages.getValue().get(0).getTime());
        }else {
            listenForNewMessages(new Date());
        }
    }

    void sendMessage(String text){
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(new Message(text, new Date(), userName));
        dataManager.storeMessagesToLocal(messages);
        dataManager.sendMessagesToBackend(messages);
    }

    void getNewMessages(Date date){
        dataManager.getNewMessagesFromBackend(date, dataManager);
    }

    void listenForNewMessages(Date date){
        dataManager.listenForNewMessages(dataManager, date);
    }
}
