package com.app.devchat;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.app.devchat.data.DataModels.Message;
import com.app.devchat.data.Network.FireBaseAPI;
import com.app.devchat.data.NewMessagesCallback;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

@RunWith(AndroidJUnit4.class)
public class FirebaseApiTest  {

    FireBaseAPI fireBaseAPI;

    @Before
    public void initializeFirebase(){
        fireBaseAPI = new FireBaseAPI();
    }

    @Test
    public void getNewMessageTest() throws InterruptedException {
        String messageText = "Hi, this is a new message test";
        String sender = "android_instrumentation_sender";
        Date olderDate = new Date();
        Date newerDate = new Date();
        CountDownLatch latch = new CountDownLatch(2);
        Message newMessage = new Message(null, messageText, newerDate, sender);
        ArrayList<Message> messagesToBeSent = new ArrayList<>();
        final ArrayList<Message>[] messagesReceived = new ArrayList[]{new ArrayList<Message>()};
        messagesToBeSent.add(newMessage);
        fireBaseAPI.setUserName("test");
        NewMessagesCallback mockCallback = messages -> {

            messagesReceived[0] = messages;
            latch.countDown();

        };
        fireBaseAPI.listenForNewMessages(olderDate, mockCallback);
        Thread.sleep(15000);
        fireBaseAPI.sendMessagesToBackendDatabase(messagesToBeSent);


        latch.await();

        Assert.assertNull(messagesReceived[0]);



    }


}
