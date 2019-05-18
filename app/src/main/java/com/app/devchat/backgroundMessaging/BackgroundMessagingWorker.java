package com.app.devchat.backgroundMessaging;

import android.content.Context;

import com.app.devchat.BaseApplication;
import com.app.devchat.data.AppDataManager;
import com.app.devchat.data.Network.FireBaseAPI;
import com.app.devchat.data.SharedPrefs.AppPreferenceHelper;
import com.app.devchat.data.SqlDatabase.SQLiteDatabase;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * Handles background messaging functions through the {@link androidx.work.WorkManager}
 */

public class BackgroundMessagingWorker extends Worker {

    AppDataManager dm;

    public BackgroundMessagingWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        BaseApplication application = (BaseApplication) context.getApplicationContext();

        // intialize DataManager
        dm = new AppDataManager(new AppPreferenceHelper(context),
                new SQLiteDatabase(application), new FireBaseAPI(), application);
    }

    /**
     * Prepares and enqueues the background messaging work and submits to the WorkManager
     */
    public static void run(){
        Constraints constraints = new Constraints.Builder().
                setRequiresBatteryNotLow(true).
                setRequiredNetworkType(NetworkType.CONNECTED).
                build();
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(BackgroundMessagingWorker.class).
                setConstraints(constraints).
                build();
        WorkManager.getInstance().enqueue(oneTimeWorkRequest);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Clear any background work to prevent multiple work instances running
        WorkManager.getInstance().cancelAllWork();

        // Set up new message listener.
        dm.listenForNewMessages(new Date(), dm);
        return Result.success();
    }
}
