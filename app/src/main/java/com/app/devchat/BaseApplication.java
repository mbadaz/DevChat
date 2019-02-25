package com.app.devchat;


import android.app.Application;

import com.app.devchat.DepedencyInjecton.AppComponent;
import com.app.devchat.DepedencyInjecton.AppModule;
import com.app.devchat.DepedencyInjecton.DaggerAppComponent;


public class BaseApplication extends Application {

    AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
       component = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }

    public AppComponent getComponent() {
        return component;
    }
}
