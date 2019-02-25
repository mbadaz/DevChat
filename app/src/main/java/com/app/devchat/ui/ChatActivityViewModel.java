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
    private boolean hasDoneInitialLoad = false;
    String userName;

    @Inject
    public ChatActivityViewModel(@NonNull Application application, DataManager dataManager) {
        super(application);
        this.dataManager = dataManager;
        liveMessages = dataManager.getMessagesFromLocal();
        userName = dataManager.getUserName();
    }

    public AndroidViewModel setDataManager(DataManager dataManager){
        this.dataManager = dataManager;
        return this;
    }

    void sendMessage(String text){
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(new Message(text, new Date(), userName));
        dataManager.storeMessagesToLocal(messages);
        dataManager.sendMessagesToBackend(messages);
    }

    void getNewMessages(Date date){
        if (!hasDoneInitialLoad){
            dataManager.getNewMessagesFromBackend(date, dataManager);
            hasDoneInitialLoad = true;
        }
    }

    void listenForNewMessages(){
        dataManager.listenForNewMessages(dataManager);
    }
}
