package com.app.devchat.chat;

import android.app.Application;

import com.app.devchat.data.AppDataManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

class ChatActivityViewModel extends AndroidViewModel {

    final AppDataManager dataManager;

    public ChatActivityViewModel(@NonNull Application application) {
        super(application);
        dataManager = AppDataManager.getInstace(application);
    }

    void sendMessage(String text){
        dataManager.sendNewMessage(text);
    }

}
