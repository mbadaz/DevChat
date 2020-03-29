package com.app.devchat;

import android.app.Application;

import com.app.devchat.data.DataManager;
import com.app.devchat.data.Network.NetworkHelper;
import com.app.devchat.ui.ChatActivityViewModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import androidx.lifecycle.ViewModel;

public class ChatsActivityViewModelTest {


    Application application;

    @Mock
    DataManager dataManager;

    ChatActivityViewModel viewModel;

    @Before
    public void initViewModel(){

        application = Mockito.mock(BaseApplication.class);
        MockitoAnnotations.initMocks(this);
        viewModel = new ChatActivityViewModel(application, dataManager);
    }

    @Test
    public void getNewMessagesViewModelTest(){
        Date date = new Date();

        viewModel.getNewMessages(date);
        Mockito.verify(dataManager).getNewMessagesFromBackendDatabase(date);
        //Mockito.verify(networkHelper).getNewMessagesFromBackendDatabase(date);
    }
}
