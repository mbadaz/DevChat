package com.app.devchat.backgroundMessaging;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.app.devchat.DepedencyInjecton.AppModule;
import com.app.devchat.DepedencyInjecton.DaggerMessagingWokerComponent;
import com.app.devchat.NewMessageNotification;
import com.app.devchat.data.DataManager;
import com.app.devchat.data.DataModels.Message;
import com.app.devchat.data.Network.FireBaseAPI;
import com.app.devchat.data.NewMessagesCallback;
import com.app.devchat.data.SqlDatabase.SQLiteDatabaseHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;


public class BackgroundMessagingWorker extends Worker implements NewMessagesCallback{

    static final String TAG = BackgroundMessagingWorker.class.toString();

    @Inject
    DataManager dataManager;

    private CountDownLatch threadLatch;

    public BackgroundMessagingWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        DaggerMessagingWokerComponent.builder().
                appModule(new AppModule(context.getApplicationContext())).
                build().inject(this);

    }

    static void enqeueWork(){
        Constraints constraints = new Constraints.Builder().
                setRequiresBatteryNotLow(true).
                setRequiredNetworkType(NetworkType.CONNECTED).build();
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(BackgroundMessagingWorker.class,
                PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS).
                setConstraints(constraints).
                build();

        WorkManager.getInstance().enqueue(workRequest);

        Log.d(TAG, "Background messaging work enqueued");
    }

    @NonNull
    @Override
    public Result doWork() {
        ((FireBaseAPI)(dataManager.getNetworkHelper())).enable();
        threadLatch = new CountDownLatch(1);

        // Get latest message date in local database to use as basis for
        // querying backend database for new messages
        Date date = dataManager.getNewestMessageDate();

        try {
            dataManager.getNewMessagesFromBackendDatabase(date, this);

            // Lock thread and wait 15 seconds for new messages callback (onNewMessages) to get called
            threadLatch.await(15, TimeUnit.SECONDS);

            if (threadLatch.getCount() > 0) {
                Log.d(TAG, "Time out, back-end took too long to respond");
                return Result.retry();
            } else {
                return Result.success();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            return Result.retry();
        }
    }

    @Override
    public void onNewMessages(ArrayList<Message> messages) {
        if(messages != null) {
            dataManager.storeMessagesToLocalDatabase(messages);
            // Show new messages' notification
            NewMessageNotification.notify(getApplicationContext(), messages);
        }

        // Release thread
        threadLatch.countDown();
    }

    @Override
    public void onStopped() {
        super.onStopped();
        ((FireBaseAPI)(dataManager.getNetworkHelper())).disable();
        ((SQLiteDatabaseHelper)(dataManager.getLocalDatabaseHeper())).stopDatabase();
    }
}
