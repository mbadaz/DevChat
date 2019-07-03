package com.app.devchat.DepedencyInjecton;

import android.app.Application;
import android.content.Context;

import com.app.devchat.data.AppDataManager;
import com.app.devchat.data.DataManager;
import com.app.devchat.data.Network.FireBaseAPI;
import com.app.devchat.data.Network.NetworkHelper;
import com.app.devchat.data.SharedPrefs.AppPreferenceHelper;
import com.app.devchat.data.SharedPrefs.PreferencesHelper;
import com.app.devchat.data.SqlDatabase.LocalDatabaseHelper;
import com.app.devchat.data.SqlDatabase.SQLiteDatabaseHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Context mContext;
    private static Application app;

    public AppModule(Context context) {
        mContext = context;
    }

    public AppModule (Application application){
        app = application;
    }

    @Provides
    @Singleton
    static Application provideApplication(){
        return app;
    }

    @Provides
    @Singleton
    Context provideContext(){
        if (mContext == null){
            return app.getApplicationContext();
        }
        return mContext.getApplicationContext();
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
    static LocalDatabaseHelper provideAppDbHelper(SQLiteDatabaseHelper dbHelper){
        return dbHelper;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager dataManager){
        return dataManager;
    }




}
