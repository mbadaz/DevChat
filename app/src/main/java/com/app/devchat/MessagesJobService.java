package com.app.devchat;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.os.Build;

import com.app.devchat.ui.ChatActivity;
import com.app.devchat.ui.ChatActivityViewModel;

import java.util.Date;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class MessagesJobService extends JobService {

    public MessagesJobService(){

    }

    @Override
    public boolean onStartJob(JobParameters params) {

        AsyncTask<Void, Void, Void> job = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ((ChatActivityViewModel)((ChatActivity) getApplicationContext()).getViewModel()).listenForNewMessages(new Date());
                return null;
            }
        };
        job.execute();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
