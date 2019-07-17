package com.app.devchat.chat;

import android.app.Application;

import com.app.devchat.backgroundMessaging.MessagingService;
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
import androidx.room.Update;

import java.util.ArrayList;
import java.util.Date;

@Singleton
public class ChatActivityViewModel extends AndroidViewModel {

    private DataManager dataManager;

    @Inject
    public ChatActivityViewModel(@NonNull Application application) {
        super(application);
    }


    void setService(MessagingService messagingService){
        dataManager = messagingService.getData();
    }

    LiveData<PagedList<Message>> getData(){
        return dataManager.getMessagesFromLocalDatabase();
    }

    void sendMessage(String text){
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(new Message(null, text, new Date(), dataManager.getUserName()));
        dataManager.storeMessagesToLocalDatabase(messages);
        dataManager.sendMessagesToBackendDatabase(messages);
    }

    String getUserName(){
        return dataManager.getUserName();
    }

    void saveNewUserToBackend(User user){
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

    LoginMode getLoginStatus(){
        return dataManager.getLoginStatus();
    }

    void setLoginStatus(LoginMode mode) {
        dataManager.setLoginStatus(mode);
    }

    void setBackgroundMode(boolean mode){
        dataManager.setBackgroundMode(mode);
    }


}
