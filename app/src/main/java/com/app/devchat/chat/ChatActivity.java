package com.app.devchat.chat;


import android.os.Bundle;

import com.app.devchat.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity {

    static final String TAG = ChatActivity.class.getSimpleName();



    ChatActivityViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        ButterKnife.bind(this);

        viewModel = ViewModelProvider.AndroidViewModelFactory.
                getInstance(getApplication()).create(ChatActivityViewModel.class);
    }

}
