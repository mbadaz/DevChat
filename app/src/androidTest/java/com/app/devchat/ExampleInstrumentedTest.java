package com.app.devchat;


import android.content.Context;

import com.app.devchat.data.DataManager;
import com.app.devchat.data.Message;
import com.app.devchat.data.Network.AppNetworkHelper;
import com.app.devchat.data.SharedPrefs.AppPreferenceHelper;
import com.app.devchat.data.SqlDatabase.AppDbHelper;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Nullable;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getContext();

        assertEquals("com.app.devchat", appContext.getPackageName());
    }

    @Test
    public void preferencesTest(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getContext();
        AppPreferenceHelper preferenceHelper = new AppPreferenceHelper(appContext);
        preferenceHelper.setUserName("mbada");
        preferenceHelper.setLoginStatus(DataManager.LoginMode.FB_LOGIN);
        assertEquals(preferenceHelper.getUserName(), "mbada");
        DataManager.LoginMode loginMode = DataManager.LoginMode.FB_LOGIN;
        assertEquals(preferenceHelper.getLoginStatus(), loginMode.getMode());

    }

    @Test
    public void sqlDatabaseTest() throws IllegalAccessException, ClassNotFoundException, InstantiationException {

        BaseApplication app = (BaseApplication) InstrumentationRegistry.getInstrumentation().
                getTargetContext().getApplicationContext();
        AppDbHelper appDbHelper = new AppDbHelper(app);
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(new Message("message 1", new Date(), ""));
        messages.add(new Message("message 2", new Date(), ""));
        messages.add(new Message("message 3", new Date(), ""));

        appDbHelper.storeMessagesToLocal(messages);

        assertNotNull(appDbHelper.getMessagesFromLocal().getValue());
    }

    @Test
    public void firebaseDatabaseTest(){
        AppNetworkHelper networkHelper = new AppNetworkHelper();

        /**
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(new Message("message 1", new Date(), ""));
        messages.add(new Message("message 2", new Date(), ""));
        messages.add(new Message("message 3", new Date(), ""));
        */

        networkHelper.listenForNewMessages(new Date(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                assertTrue(queryDocumentSnapshots.getDocuments().isEmpty());
            }
        });

    }

}
