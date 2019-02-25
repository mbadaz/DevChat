package com.app.devchat.ui;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.app.devchat.BaseApplication;
import com.app.devchat.R;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity {

    static final String TAG = ChatActivity.class.getSimpleName();

    @Inject
    ChatActivityViewModel viewModel;


    @BindView(R.id.message_input)
    EditText messageInput;

    @BindView(R.id.chats_recycler_view)
    RecyclerView recyclerView;

    InputMethodManager imm;
    private LinearLayoutManager layoutManager;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        ((BaseApplication) getApplication()).getComponent().inject(this);

        ButterKnife.bind(this);

        handler = new Handler(Looper.getMainLooper());

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);


        
        ChatsAdapter adapter = new ChatsAdapter(this, viewModel.userName);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        //layoutManager.setStackFromEnd(true);
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        viewModel.liveMessages.observe(this, messages -> {
            adapter.submitList(messages);
            handler.postDelayed(() -> layoutManager.scrollToPositionWithOffset(0, 8), 100);

            if(messages.size() > 1){
                // get messages from backend with newer date than than the last message in database
                viewModel.getNewMessages(messages.get(0).getTime());

            }else {
                // if no messages in database listen for new messages from backend
                viewModel.listenForNewMessages();
            }
        });

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
}
