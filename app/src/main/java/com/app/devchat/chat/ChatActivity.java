package com.app.devchat.chat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.app.devchat.BaseApplication;
import com.app.devchat.BuildConfig;
import com.app.devchat.NewMessageNotification;
import com.app.devchat.R;
import com.app.devchat.backgroundMessaging.MessagingService;
import com.app.devchat.backgroundMessaging.MessagingService.MessagingServiceBinder;
import com.app.devchat.data.DataModels.Message;
import com.app.devchat.data.DataModels.User;
import com.app.devchat.data.LoginMode;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private ChatsAdapter adapter;
    private boolean isBound;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        ((BaseApplication) getApplication()).getComponent().inject(this);

        ButterKnife.bind(this);

        enableStrictMode();

        createNotificationChannel();

        handler = new Handler(Looper.getMainLooper());
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overflow, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_logout) {

            viewModel.setLoginStatus(LoginMode.LOGGED_OUT);
        }

        super.onOptionsItemSelected(item);
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

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
             MessagingServiceBinder binder = (MessagingServiceBinder) service;
            viewModel.setService(binder.getService());
            loadUI();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onStart() {

        //Clear any notifications
        NewMessageNotification.cancel(this);

        Intent intent = new Intent(this, MessagingService.class);

        startService(intent);

        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        if (isBound) {


        }

        super.onStart();
    }

    void loadUI(){
        if(viewModel.getLoginStatus() == LoginMode.LOGGED_OUT.getMode()){
            userLogin();
        }

        adapter = new ChatsAdapter(this, viewModel.getUserName());
        recyclerView.setAdapter(adapter);

        viewModel.getData().observe(this, new Observer<PagedList<Message>>() {
            @Override
            public void onChanged(PagedList<Message> messages) {
                adapter.submitList(messages);
                layoutManager.scrollToPositionWithOffset(0, 8);
            }
        });
    }


    @Override
    protected void onStop() {

        viewModel.setBackgroundMode(true);
        // Start background messages service
        unbindService(connection);
        isBound = false;

        super.onStop();
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
            layoutManager.scrollToPositionWithOffset(0, 8);

            /*
            if(!data.getValue().isEmpty()) {
                data.getValue().loadAround(0);
                layoutManager.scrollToPositionWithOffset(0, 8);
            }
            */
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


    void userLogin(){

        List<AuthUI.IdpConfig> authProviders = Arrays.asList(
                 new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build()
        );

        startActivityForResult(
                AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(authProviders)
                .build(), REQUEST_CODE_SIGN_IN
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_SIGN_IN && data != null){
            IdpResponse response = IdpResponse.fromResultIntent(data);
           loginToken = response.getIdpToken();

            if(resultCode == RESULT_OK){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(!response.isNewUser()){
                    //Save user to backend database
                    User user1 = new User(
                            user.getDisplayName(),
                            user.getEmail(),
                            user.getPhotoUrl(),
                            user.getProviderId()
                    );

                    viewModel.saveNewUserToBackend(user1);
                }
            }else {
                Log.e(TAG, "Firebase error", response.getError().getCause());
            }
        } else if (data == null) {
            // i.e when user presses back button


        }
    }

}
