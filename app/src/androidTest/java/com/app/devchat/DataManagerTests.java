package com.app.devchat;


import android.content.Context;

import com.app.devchat.data.DataManager;
import com.app.devchat.data.DataModels.Message;
import com.app.devchat.data.Network.FireBaseAPI;
import com.app.devchat.data.SharedPrefs.AppPreferenceHelper;
import com.app.devchat.data.SqlDatabase.SQLiteDatabaseHelper;
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
public class DataManagerTests {

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getContext();

        assertEquals("com.app.devchat", appContext.getPackageName());
    }



}
