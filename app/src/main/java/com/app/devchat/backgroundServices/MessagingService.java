package com.app.devchat.backgroundServices;



import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.app.devchat.DepedencyInjecton.AppModule;
import com.app.devchat.DepedencyInjecton.DaggerMessagingServiceComponent;
import com.app.devchat.data.DataManager;
import com.app.devchat.data.Network.FireBaseAPI;

import java.util.Date;

import javax.inject.Inject;


/**
 * {@link Service} that executes the app's messaging functions in the background when
 * the app is not running and also when the app is running ( when an activity has bound to it)
 */
public class MessagingService extends Service {
    public static final String TAG = MessagingService.class.toString();
    MessagingServiceBinder binder = new MessagingServiceBinder();

    @Inject
    DataManager dataManager;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerMessagingServiceComponent.builder().appModule(new AppModule(getApplication())).
                build().inject(this);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Date date = dataManager.getNewestMessageDate();
        if (date != null) {
            dataManager.getNewMessagesFromBackendDatabase(date, dataManager);
            dataManager.listenForNewMessages(date, dataManager);
        } else {
            dataManager.listenForNewMessages(new Date(), dataManager);
        }

        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(@NonNull Intent intent) {
        return binder;
    }

    /**
     * The {@link Binder} that allows clients to bind to the service
     */
    public class MessagingServiceBinder extends Binder {

       public MessagingService getService(){
            return MessagingService.this;
        }
    }

    public DataManager getData(){
        return dataManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
       // ((FireBaseAPI) dataManager.getNetworkHelper()).disable();
        // Schedule background new message checking task
        BackgroundMessagingWorker.enqeueWork();
    }


}
