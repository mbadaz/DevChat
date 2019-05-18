package com.app.devchat;


import android.content.Context;

import com.app.devchat.DepedencyInjecton.AppComponent;
import com.app.devchat.DepedencyInjecton.AppModule;
import com.app.devchat.DepedencyInjecton.DaggerAppComponent;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

//import com.app.devchat.DepedencyInjecton.DaggerAppComponent;
public class BaseApplication extends MultiDexApplication {

    AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
       component = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public AppComponent getComponent() {
        return component;
    }
}