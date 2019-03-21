package com.app.devchat.ui;


import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.app.devchat.BaseApplication;
import com.app.devchat.BuildConfig;
import com.app.devchat.MessagesJobService;
import com.app.devchat.NewMessageNotification;
import com.app.devchat.R;

import java.util.Date;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity {

    static final String TAG = ChatActivity.class.getSimpleName();
    static final String CHANNEL_ID = BuildConfig.APPLICATION_ID;
    public static final int BACKGROUND_MESSAGES_JOB_SCHEDULER_ID = 1;

    @Inject
    ChatActivityViewModel viewModel;


    @BindView(R.id.message_input)
    EditText messageInput;

    @BindView(R.id.chats_recycler_view)
    RecyclerView recyclerView;

    InputMethodManager imm;
    private LinearLayoutManager layoutManager;
    private Handler handler;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        ((BaseApplication) getApplication()).getComponent().inject(this);

        ButterKnife.bind(this);

        enableStrictMode();

        createNotificationChannel();

        handler = new Handler(Looper.getMainLooper());
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        
        ChatsAdapter adapter = new ChatsAdapter(this, viewModel.getUserName());
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        viewModel.initializeData();
        viewModel.liveMessages.observe(this, messages -> {
            adapter.submitList(messages);
            handler.postDelayed(() -> layoutManager.scrollToPositionWithOffset(0, 8), 100);
            if(!viewModel.hasDoneIntialLoad && !messages.isEmpty()){
                viewModel.getNewMessages(messages.get(0).getTime());
                viewModel.listenForNewMessages(messages.get(0).getTime());
            }else if(!viewModel.hasDoneIntialLoad && messages.isEmpty()){
                viewModel.getNewMessages(new Date());
            }else {
                viewModel.listenForNewMessages(messages.get(0).getTime());
            }
        });
    }

    private void enableStrictMode() {
        if(BuildConfig.DEBUG){
            StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build();
            StrictMode.setThreadPolicy(threadPolicy);
        }
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onPause() {

        // Start background messages service
        ComponentName componentName = new ComponentName(this, MessagesJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(BACKGROUND_MESSAGES_JOB_SCHEDULER_ID, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build();
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);
        super.onPause();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        //Clear any notifications
        NewMessageNotification.cancel(this);

        //Cancel the background service
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(BACKGROUND_MESSAGES_JOB_SCHEDULER_ID);
    }



    @OnClick(R.id.send_button)
    void sendMessage(){
        String text = messageInput.getText().toString();
        if(!text.isEmpty()){
            viewModel.sendMessage(text);
            messageInput.setText(null);
            if(!viewModel.liveMessages.getValue().isEmpty()) {
                viewModel.liveMessages.getValue().loadAround(0);
                layoutManager.scrollToPositionWithOffset(0, 8);
            }
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.shouldVibrate();
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public ViewModel getViewModel(){
        return ((ChatActivityViewModel)viewModel);
    }

}


