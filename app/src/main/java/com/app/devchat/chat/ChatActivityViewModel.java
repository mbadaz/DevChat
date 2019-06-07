package com.app.devchat.chat;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.app.devchat.backgroundMessaging.MessagingService;
import com.app.devchat.backgroundMessaging.MessagingService.MessagingServiceBinder;
import com.app.devchat.data.DataManager;
import com.app.devchat.data.DataModels.Message;
import com.app.devchat.data.DataModels.User;
import com.app.devchat.data.LoginMode;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;

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

    private MessagingService messagingService;

    @Inject
    public ChatActivityViewModel(@NonNull Application application) {
        super(application);
    }


    void setService(MessagingService messagingService){
        this.messagingService = messagingService;
        this.messagingService.setBackgrooundMode(false);
    }



    LiveData<PagedList<Message>> getData(){
        return messagingService.getLiveMessages();
    }

    void sendMessage(String text){
        messagingService.sendNewMessage(text);
    }

    String getUserName(){
        return messagingService.getUserName();
    }


    void saveNewUserToBackend(User user){
        messagingService.saveUser(user);

    }

    int getLoginStatus(){
        return messagingService.getLoginStatus();
    }

    void setLoginStatus(LoginMode mode) {
        messagingService.setLoginStatus(mode);
    }

    void setBackgroundMode(boolean mode){
        messagingService.setBackgrooundMode(mode);
    }




}
