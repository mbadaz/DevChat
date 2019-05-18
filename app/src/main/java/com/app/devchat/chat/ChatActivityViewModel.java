package com.app.devchat.chat;

import android.app.Application;

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

    private DataManager dataManager;
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
        messages.add(new Message(null, text, new Date(), dataManager.getUserName()));
        dataManager.storeMessagesToLocalDatabase(messages);
        dataManager.sendMessagesToBackendDatabase(messages);
    }

    public void listenForNewMessages(Date date){
        dataManager.listenForNewMessages(date, dataManager);
    }

    String getUserName(){
        return dataManager.getUserName();
    }

    void getNewMessages(Date date){
        if(!hasDoneIntialLoad){
            dataManager.getNewMessagesFromBackendDatabase(date, dataManager);
            hasDoneIntialLoad = true;
        }
    }

    void saveNewUserToBackend(User user){
        dataManager.addNewUserToBackEndDatabase(user);

        LoginMode loginMode;

        if(user.getSignInMethod() == FacebookAuthProvider.FACEBOOK_SIGN_IN_METHOD){
            loginMode = LoginMode.FB_LOGIN;
        }else if (user.getSignInMethod() == GoogleAuthProvider.GOOGLE_SIGN_IN_METHOD){
            loginMode = LoginMode.GOOGLE_LOGIN;
        }else{
            loginMode = LoginMode.EMAIL_LOGIN;
        }

        dataManager.updateUserInfo(user.getUserName(), user.getUserEmail(), user.getPhotoUrl(), loginMode);
    }

    int getLoginStatus(){
        return dataManager.getLoginStatus();
    }

    void setLoginStatus(LoginMode mode) {
        dataManager.setLoginStatus(mode);
    }


}
