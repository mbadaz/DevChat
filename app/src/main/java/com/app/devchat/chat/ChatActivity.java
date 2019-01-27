package com.app.devchat.chat;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.app.devchat.R;

import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity {

    static final String TAG = ChatActivity.class.getSimpleName();

    ChatActivityViewModel viewModel;

    @BindView(R.id.message_input)
    EditText messageInput;

    @BindView(R.id.chats_recycler_view)
    RecyclerView recyclerView;

    InputMethodManager imm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        ButterKnife.bind(this);

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        viewModel = ViewModelProvider.AndroidViewModelFactory.
                getInstance(getApplication()).create(ChatActivityViewModel.class);
        ChatsAdapter adapter = new ChatsAdapter(this, viewModel.dataManager.userName);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        viewModel.liveMessages.observe(this, messages -> {
            adapter.submitList(messages);
            int first = layoutManager.findLastVisibleItemPosition();
            layoutManager.scrollToPositionWithOffset(0, 8);
            //recyclerView.scrollToPosition(0);
            if(messages.size() > 1){
                // get messages from backend with newer date than than the last message in database
                viewModel.getNewMessages(messages.get(0).getTime());

            }else {
                // if no messages in database listen for new messages from backend
                viewModel.listenForNewMessages(new Date());
            }

        });

        MutableLiveData<Boolean> input = new MutableLiveData<>();
        input.setValue(imm.isActive());
        input.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){

                }
            }
        });

    }

    @OnClick(R.id.send_button)
    void sendMessage(){
        String text = messageInput.getText().toString();
        if(!text.isEmpty()){
            viewModel.sendMessage(text);
            messageInput.setText(null);
            View view = this.getCurrentFocus();
            viewModel.liveMessages.getValue().loadAround(0);
        }
    }

}
