package com.app.devchat;

import android.app.Application;

import com.app.devchat.DepedencyInjecton.DaggerAppComponent;
import com.app.devchat.data.AppDataManager;
import com.app.devchat.data.DataManager;
import com.app.devchat.data.Message;
import com.app.devchat.data.Network.NetworkHelper;
import com.app.devchat.data.NewMessagesCallback;
import com.app.devchat.data.SharedPrefs.PreferencesHelper;
import com.app.devchat.data.SqlDatabase.LocalDatabase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;

public class DataManagerTests {

    @Mock
    NetworkHelper networkHelper;

    @Mock
    LocalDatabase sqliteDatabase;

    @Mock
    PreferencesHelper preferencesHelper;

    @Mock
    Application application;

    @Mock
    OnSuccessListener successListener;

    @Captor
    ArgumentCaptor<NewMessagesCallback> newMessagesCallback;

    DataManager dataManager;

    ArrayList<Message> newMessages = Lists.newArrayList(new Message("message1", new Date(), "sender"));

    @Before
    public void prepareDataManager(){
        MockitoAnnotations.initMocks(this);
        dataManager = new AppDataManager(preferencesHelper, sqliteDatabase, networkHelper, application);
    }

    @Test
    public void getNewMessagesFromBackendDatabase(){
        Date date = new Date();
        dataManager.getNewMessagesFromBackendDatabase(date);
        Mockito.verify(networkHelper).getNewMessagesFromBackendDatabase(date);


    }
}
