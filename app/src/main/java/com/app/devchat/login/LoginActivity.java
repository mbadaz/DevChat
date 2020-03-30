package com.app.devchat.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.devchat.R;
import com.app.devchat.chat.ChatActivity;
import com.app.devchat.data.SharedPrefs.AppPreferenceHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.editTextUsername)
    EditText usernameInput;
    AppPreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        preferenceHelper = new AppPreferenceHelper(this);

        if (!preferenceHelper.getUserName().equals("none")) {
            launchActivity();
        }
    }

    @OnClick(R.id.btnLogin)
    void saveUserName() {
        String username = usernameInput.getText().toString();
        if (username.isEmpty()) {
            Toast.makeText(this, "Username cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        preferenceHelper.setUserName("~" + username);
        launchActivity();
    }

    void launchActivity() {
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
        finish();
    }

}
