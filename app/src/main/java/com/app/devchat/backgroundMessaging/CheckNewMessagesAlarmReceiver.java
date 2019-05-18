package com.app.devchat.backgroundMessaging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * Receives the alarm broadcast and starts the background messaging service through the {@link BackgroundMessagingWorker}
 */
public class CheckNewMessagesAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12342;
    public static final int ALARM_TRIGGER_TIME = 10 * 60 * 1000; // time the alarm will be triggered.

    @Override
    public void onReceive(Context context, Intent intent) {
        // start check new messages work

        BackgroundMessagingWorker.run();
    }
}
