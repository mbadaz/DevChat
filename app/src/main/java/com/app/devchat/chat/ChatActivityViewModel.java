package com.app.devchat.chat;

import android.app.Application;

import com.app.devchat.backgroundServices.MessagingService;
import com.app.devchat.data.DataManager;
import com.app.devchat.data.DataModels.Message;
import com.app.devchat.data.DataModels.User;
import com.app.devchat.data.LoginMode;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import java.util.ArrayList;
import java.util.Date;

@Singleton
public class ChatActivityViewModel extends AndroidViewModel {

    private DataManager dataManager;

    @Inject
    public ChatActivityViewModel(@NonNull Application application) {
        super(application);
    }


    public void setService(MessagingService messagingService){
        dataManager = messagingService.getData();
    }

    LiveData<PagedList<Message>> getData(){
        return dataManager.getMessagesFromLocalDatabase();
    }

    public void sendMessage(String text){
        ArrayList<Message> messages = new ArrayList<>();
        Date date = new Date();
        messages.add(new Message(null, text, date, dataManager.getUserName()));
        dataManager.storeMessagesToLocalDatabase(messages);
        dataManager.sendMessagesToBackendDatabase(messages);
        dataManager.listenForNewMessages(date, dataManager);
    }

    String getUserName(){
        return dataManager.getUserName();
    }

    public void saveNewUserToBackend(User user){
        // new user to backend database
        dataManager.addNewUserToBackEndDatabase(user);

        // Update local user info store
        LoginMode loginMode;
        if (user.getSignInMethod().equals(FacebookAuthProvider.FACEBOOK_SIGN_IN_METHOD)){
            loginMode = LoginMode.FB_LOGIN;
        } else if (user.getSignInMethod().equals(GoogleAuthProvider.GOOGLE_SIGN_IN_METHOD)){
            loginMode = LoginMode.GOOGLE_LOGIN;
        } else {
            loginMode = LoginMode.EMAIL_LOGIN;
        }

        dataManager.updateUserInfo(user.getUserName(), user.getUserEmail(), user.getPhotoUrl(), loginMode);
    }

    public LoginMode getLoginStatus(){
        return dataManager.getLoginStatus();
    }

    public void setLoginStatus(LoginMode mode) {
        dataManager.setLoginStatus(mode);
    }

    public void setBackgroundMode(boolean mode){
        dataManager.setBackgroundMode(mode);
    }


}
