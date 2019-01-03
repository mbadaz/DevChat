package com.app.devchat.chat;


import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.app.devchat.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity {

    static final String TAG = ChatActivity.class.getSimpleName();

    @BindView(R.id.add_button)
    Button button;

    ChatActivityViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        ButterKnife.bind(this);

        viewModel = ViewModelProvider.AndroidViewModelFactory.
                getInstance(getApplication()).create(ChatActivityViewModel.class);
        viewModel.messagesList.observe(this, messages -> Log.d(TAG, "PagedList changed"));
    }

    @OnClick(R.id.add_button)
    void add(){
        viewModel.addMessage();
    }
}
