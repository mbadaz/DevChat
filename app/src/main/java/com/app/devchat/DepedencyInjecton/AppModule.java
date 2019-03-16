package com.app.devchat.DepedencyInjecton;

import android.app.Application;
import android.content.Context;

import com.app.devchat.data.AppDataManager;
import com.app.devchat.data.DataManager;
import com.app.devchat.data.Network.FireBaseAPI;
import com.app.devchat.data.Network.NetworkHelper;
import com.app.devchat.data.SharedPrefs.AppPreferenceHelper;
import com.app.devchat.data.SharedPrefs.PreferencesHelper;
import com.app.devchat.data.SqlDatabase.SQLiteDatabase;
import com.app.devchat.data.SqlDatabase.LocalDatabase;

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
        return new FireBaseAPI();
    }

    @Provides
    @Singleton
    static PreferencesHelper providePreferencesHelper(AppPreferenceHelper preferencesHelper){
        return preferencesHelper;
    }

    @Provides
    @Singleton
    static LocalDatabase provideAppDbHelper(SQLiteDatabase dbHelper){
        return dbHelper;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager dataManager){
        return dataManager;
    }




}
