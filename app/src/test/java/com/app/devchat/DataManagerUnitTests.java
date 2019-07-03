package com.app.devchat;

import android.app.Application;

import com.app.devchat.data.AppDataManager;
import com.app.devchat.data.DataManager;
import com.app.devchat.data.DataModels.Message;
import com.app.devchat.data.Network.NetworkHelper;
import com.app.devchat.data.NewMessagesCallback;
import com.app.devchat.data.SharedPrefs.PreferencesHelper;
import com.app.devchat.data.SqlDatabase.LocalDatabaseHelper;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;

import androidx.core.app.NotificationCompat;

@RunWith(MockitoJUnitRunner.class)
public class DataManagerUnitTests {
    @Mock
    NetworkHelper networkHelper;

    @Mock
    LocalDatabaseHelper database;

    @Mock
    PreferencesHelper preferencesHelper;

    @Captor
    ArgumentCaptor<NewMessagesCallback> newMessagesCallback;


    DataManager dataManager;

    @Mock
    DataManager mockDataManager;

    @Mock
    NotificationCompat.Builder builder;



    final ArrayList<Message> NEW_MESSAGES = Lists.newArrayList(
            new Message("hello", new Date(), "Me"),
            new Message("hello", new Date(), "Me"),
            new Message("hello", new Date(), "Me"));

    @Before
    public void prepareDatabase(){
        Application application = Mockito.mock(BaseApplication.class);
        MockitoAnnotations.initMocks(this);
        dataManager = new AppDataManager(preferencesHelper, database, networkHelper, application);
    }

    @Test
    public void getNewMessagesFromBackendTest(){
        Date date = new Date();
        dataManager.getNewMessagesFromBackendDatabase(date, null);
        Mockito.verify(networkHelper).getNewMessagesFromBackendDatabase(Matchers.any(), newMessagesCallback.capture());
        newMessagesCallback.getValue().onNewMessages(NEW_MESSAGES);
        Mockito.verify(database).storeMessagesToLocalDatabase(NEW_MESSAGES);

    }
}
