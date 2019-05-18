package com.app.devchat.chat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.app.devchat.BaseApplication;
import com.app.devchat.BuildConfig;
import com.app.devchat.backgroundMessaging.BackgroundMessagingWorker;
import com.app.devchat.backgroundMessaging.CheckNewMessagesAlarmReceiver;
import com.app.devchat.NewMessageNotification;
import com.app.devchat.R;
import com.app.devchat.data.DataModels.User;
import com.app.devchat.data.LoginMode;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ChatActivity extends AppCompatActivity {

    static final String TAG = ChatActivity.class.getSimpleName();
    static final String CHANNEL_ID = BuildConfig.APPLICATION_ID;
    public static final int REQUEST_CODE_SIGN_IN = 2;
    private String loginToken;

    @Inject
    ChatActivityViewModel viewModel;


    @BindView(R.id.message_input)
    EditText messageInput;

    @BindView(R.id.chats_recycler_view)
    RecyclerView recyclerView;


    InputMethodManager imm;
    private LinearLayoutManager layoutManager;
    private Handler handler;

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

        // Observe for database changes and update ui accordingly
        viewModel.liveMessages.observe(this, messages -> {
            adapter.submitList(messages);
            handler.postDelayed(() -> layoutManager.scrollToPositionWithOffset(0, 8), 100);
            if(!viewModel.hasDoneIntialLoad && !messages.isEmpty()){
                viewModel.getNewMessages(messages.get(0).getTime());
            }else if(!viewModel.hasDoneIntialLoad) {
                viewModel.getNewMessages(new Date());
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overflow, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    /**
     * Check for long running work on the UI thread
     */
    private void enableStrictMode() {
        if(BuildConfig.DEBUG){
           StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build();
           StrictMode.setThreadPolicy(threadPolicy);
        }
    }


    @Override
    protected void onPause() {

        // Start background messages service
        Intent intent = new Intent(this, CheckNewMessagesAlarmReceiver.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, CheckNewMessagesAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(
                    AlarmManager.RTC,
                    SystemClock.elapsedRealtime() + CheckNewMessagesAlarmReceiver.ALARM_TRIGGER_TIME,
                    pendingIntent
            );
        }
        super.onPause();
    }


    @Override
    protected void onResume() {

        //Clear any notifications
        NewMessageNotification.cancel(this);
        viewModel.listenForNewMessages(new Date());

        // Stop background messages service
        WorkManager.getInstance().cancelAllWork();

        super.onResume();
    }

    /**
     * Sends new message
     */
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
}
