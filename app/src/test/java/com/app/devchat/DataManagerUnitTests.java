package com.app.devchat;

import android.app.Application;

import com.app.devchat.data.AppDataManager;
import com.app.devchat.data.DataManager;
import com.app.devchat.data.DataModels.Message;
import com.app.devchat.data.Network.FireBaseAPI;
import com.app.devchat.data.Network.NetworkHelper;
import com.app.devchat.data.NewMessagesCallback;
import com.app.devchat.data.SharedPrefs.PreferencesHelper;
import com.app.devchat.data.SqlDatabase.LocalDatabaseHelper;
import com.google.common.collect.Lists;

import org.junit.Assert;
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

@RunWith(MockitoJUnitRunner.class)
public class DataManagerUnitTests {
    @Mock
    NetworkHelper networkHelper;

    @Mock
    FireBaseAPI fireBaseAPI;

    @Mock
    LocalDatabaseHelper database;

    @Mock
    PreferencesHelper preferencesHelper;

    @Captor
    ArgumentCaptor<Date> dateArgumentCaptor;

    @Captor
    ArgumentCaptor<NewMessagesCallback> newMessagesCallbackArgumentCaptor;


    DataManager dataManager;




    final ArrayList<Message> NEW_MESSAGES = Lists.newArrayList(
            new Message(null, "hello", new Date(), "Me"),
            new Message(null, "hello", new Date(), "Me"),
            new Message(null,"hello", new Date(), "Me"));

    @Before
    public void prepareDatabase(){
        Application application = Mockito.mock(BaseApplication.class);
        MockitoAnnotations.initMocks(this);
        dataManager = new AppDataManager(preferencesHelper, database, networkHelper, application);
    }

    @Test
    public void getNewMessagesFromBackendTest(){
        Date date = new Date();
        Date date2 = new Date();
        dataManager.getNewMessagesFromBackendDatabase(date, null);

        Mockito.verify(fireBaseAPI).getNewMessagesFromBackendDatabase(dateArgumentCaptor.capture(), newMessagesCallbackArgumentCaptor.capture() );
        Assert.assertSame(date, dateArgumentCaptor.getValue());

    }
}
