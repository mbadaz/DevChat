package com.app.devchat;

import com.app.devchat.data.DataManager;
import com.app.devchat.chat.ChatActivityViewModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Date;

public class ChatActivityViewModelTests {


    BaseApplication application;

    ChatActivityViewModel viewModel;

    @Mock
    DataManager dataManager;

    @Before
    public void chatsActivityViewModel(){

        MockitoAnnotations.initMocks(this);
        application = Mockito.mock(BaseApplication.class);
        viewModel = new ChatActivityViewModel(application, dataManager);
    }

    @Test
    public void getNewMessagesFromBackendTest(){
        Date date = new Date();
       viewModel.listenForNewMessages(date);

       Mockito.verify(dataManager).listenForNewMessages(date, null);
    }
}
