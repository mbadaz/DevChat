package com.app.devchat.DepedencyInjecton;

import android.app.Application;
import android.content.Context;

import com.app.devchat.data.AppDataManager;
import com.app.devchat.data.DataManager;
import com.app.devchat.data.Network.AppNetworkHelper;
import com.app.devchat.data.Network.NetworkHelper;
import com.app.devchat.data.SharedPrefs.AppPreferenceHelper;
import com.app.devchat.data.SharedPrefs.PreferencesHelper;
import com.app.devchat.data.SqlDatabase.AppDbHelper;
import com.app.devchat.data.SqlDatabase.DbHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private static Application application;

    public AppModule(Application app) {
        application = app;
    }

    @Provides
    @Singleton
    static Application provideApplication(){
        return application;
    }

    @Provides
    @Singleton
    static Context provideContext(){
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    static NetworkHelper provideAppNeworkHelper(){
        return new AppNetworkHelper();
    }

    @Provides
    @Singleton
    static PreferencesHelper providePreferencesHelper(AppPreferenceHelper preferencesHelper){
        return preferencesHelper;
    }

    @Provides
    @Singleton
    static DbHelper provideAppDbHelper(AppDbHelper dbHelper){
        return dbHelper;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager dataManager){
        return dataManager;
    }




}
