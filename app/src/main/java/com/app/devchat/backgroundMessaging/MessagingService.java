package com.app.devchat.backgroundMessaging;

import android.app.Application;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.app.devchat.DepedencyInjecton.AppModule;
import com.app.devchat.DepedencyInjecton.DaggerMessagingServiceComponent;
import com.app.devchat.DepedencyInjecton.MessagingServiceModule;
import com.app.devchat.data.DataManager;
import com.app.devchat.data.DataModels.Message;
import com.app.devchat.data.DataModels.User;
import com.app.devchat.data.LoginMode;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

public class MessagingService extends IntentService {

    MessagingServiceBinder binder = new MessagingServiceBinder();

    @Inject
    DataManager dataManager;

    @Inject
    public MessagingService() {
        super("MessagingService");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerMessagingServiceComponent.builder().appModule(new AppModule(getApplication())).
                build().inject(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Date date = dataManager.getNewestMessageDate();
        if (date != null) {
            dataManager.getNewMessagesFromBackendDatabase(date, dataManager);
            dataManager.listenForNewMessages(date, dataManager);
        } else {
            dataManager.listenForNewMessages(new Date(), dataManager);
        }
    }

    public class MessagingServiceBinder extends Binder {

       public MessagingService getService(){
            return MessagingService.this;
        }
    }

    public void sendNewMessage(String text){
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(new Message(null, text, new Date(), dataManager.getUserName()));
        dataManager.sendMessagesToBackendDatabase(messages);
    }

    public String getUserName(){
        return dataManager.getUserName();
    }

    public void setBackgrooundMode(boolean mode){
        dataManager.setBackgroundMode(mode);
    }

    public LiveData<PagedList<Message>> getLiveMessages(){
        return dataManager.getMessagesFromLocalDatabase();
    }

    public void saveUser(User user){
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

    public void setLoginStatus(LoginMode mode) {
        dataManager.setLoginStatus(mode);
    }

    public int getLoginStatus(){
        return dataManager.getLoginStatus();
    }
}
