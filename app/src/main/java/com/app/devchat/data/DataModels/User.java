package com.app.devchat.data.DataModels;

import android.net.Uri;

public class User {

    private String userName;
    private String userEmail;
    private String photoUrl;
    private String signInMethod;

    public User(String userName, String userEmail, Uri photoUrl, String signInMethod) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.photoUrl = photoUrl.toString();
        this.signInMethod = signInMethod;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getSignInMethod() {
        return signInMethod;
    }
}
