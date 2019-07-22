package com.app.devchat.backgroundServices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Objects;

public class AppBroadCastsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.requireNonNull(intent.getAction()).equals(Intent.ACTION_BOOT_COMPLETED)) {
            // Device has completed boot, schedule background messaging tasks
            BackgroundMessagingWorker.enqeueWork();
        }

    }
}
