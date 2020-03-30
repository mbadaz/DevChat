package com.app.devchat.chat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.app.devchat.backgroundServices.MessagingService;
import com.app.devchat.data.DataManager;
import com.app.devchat.data.DataModels.Message;
import com.app.devchat.data.DataModels.User;
import com.app.devchat.data.LoginMode;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivityViewModel extends ViewModel {

    private DataManager dataManager;

    void setService(MessagingService messagingService){
        dataManager = messagingService.getData();
    }

    LiveData<PagedList<Message>> getData(){
        return dataManager.getMessagesFromLocalDatabase();
    }

    public void sendMessage(String text){
        ArrayList<Message> messages = new ArrayList<>();
        Date date = new Date();
        messages.add(new Message(text, date, dataManager.getUserName(), Message.MessageType.TEXT));
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
//        LoginMode loginMode;
//        if (user.getSignInMethod().equals(FacebookAuthProvider.FACEBOOK_SIGN_IN_METHOD)){
//            loginMode = LoginMode.FB_LOGIN;
//        } else if (user.getSignInMethod().equals(GoogleAuthProvider.GOOGLE_SIGN_IN_METHOD)){
//            loginMode = LoginMode.GOOGLE_LOGIN;
//        } else {
//            loginMode = LoginMode.EMAIL_LOGIN;
//        }

        dataManager.updateUserInfo(user.getUserName(), user.getUserEmail(), user.getPhotoUrl(), LoginMode.GOOGLE_LOGIN);
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

    LiveData<String> getOnlineStatusStream() {
        return dataManager.getOnlineStatusStream();
    }

    boolean goOffline() {
      return dataManager.goOffline();
    }

}
