package com.app.devchat;

import com.app.devchat.data.DataManager;
import com.app.devchat.chat.ChatActivityViewModel;
import com.app.devchat.data.DataModels.Message;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivityViewModelTests {


    BaseApplication application;

    ChatActivityViewModel viewModel;

    @Mock
    DataManager dataManager;

    @Captor
    ArgumentCaptor<ArrayList<Message>> arrayListArgumentCaptors;

    @Before
    public void chatsActivityViewModel(){

        MockitoAnnotations.initMocks(this);
        application = Mockito.mock(BaseApplication.class);
        viewModel = new ChatActivityViewModel(application);
    }

    @Test
    public void sendNewMessage(){
        String messageText = "this is a new message";
        viewModel.sendMessage(messageText);

       Mockito.verify(dataManager).storeMessagesToLocalDatabase(arrayListArgumentCaptors.capture());
       Message messageToBeSent = arrayListArgumentCaptors.getValue().get(0);
        Assert.assertEquals(messageText, messageToBeSent.text);
    }
}
