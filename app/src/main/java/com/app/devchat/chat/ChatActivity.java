package com.app.devchat.chat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
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

    InputMethodManager inputMethodManager;
    private LinearLayoutManager layoutManager;;
    private ChatsAdapter adapter;
    private boolean isBound;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        ((BaseApplication) getApplication()).getComponent().inject(this);

        ButterKnife.bind(this);

        enableStrictMode();

        createNotificationChannel();

        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
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
            isBound = true;
            loadUI();

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

        // start messaging service
        Intent intent = new Intent(this, MessagingService.class);
        startService(intent);

        // bind to messaging service
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        super.onStart();
    }

    void loadUI(){

        viewModel.setBackgroundMode(false);

        adapter = new ChatsAdapter(this, viewModel.getUserName());
        recyclerView.setAdapter(adapter);

        viewModel.getData().observe(this, messages -> {
           // adapter.submitList(messages);
            adapter.submitList(messages, new Runnable() {
                @Override
                public void run() {
                    //messages.loadAround(0);
                    layoutManager.scrollToPositionWithOffset(0, 8);
                }
            });
        });
    }


    @Override
    protected void onStop() {

        if (isBound) {
            // Start background messages service
            unbindService(connection);
            isBound = false;
        }

        super.onStop();
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        if(isBound){
            viewModel.setBackgroundMode(true);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
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
            inputMethodManager.hideSoftInputFromWindow(messageInput.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
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
                String userName = user.getDisplayName();
                String userEmail = user.getEmail();
                Uri userPhotoUri = user.getPhotoUrl();
                String authProvider = user.getProviderId();
                if(response.isNewUser()){
                    //Save user to backend database
                    User user1 = new User(
                            userName,
                            userEmail,
                            userPhotoUri,
                            authProvider
                    );

                    viewModel.saveNewUserToBackend(user1);

                }
            }else {
                Log.e(TAG, "Firebase error", response.getError());
            }
        } else if (data == null) {
            // i.e when user presses back button


        }
    }

}
