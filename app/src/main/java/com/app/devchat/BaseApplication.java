package com.app.devchat;


import android.content.Context;
import android.content.Intent;

import com.app.devchat.DepedencyInjecton.AppComponent;
import com.app.devchat.DepedencyInjecton.AppModule;
import com.app.devchat.DepedencyInjecton.DaggerAppComponent;
import com.app.devchat.backgroundMessaging.MessagingService;


import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

public class BaseApplication extends MultiDexApplication {

    AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        // start messaging service
        Intent intent = new Intent(getApplicationContext(), MessagingService.class);
        getApplicationContext().startService(intent);

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