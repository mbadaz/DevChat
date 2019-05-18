package com.app.devchat.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.devchat.R;
import com.app.devchat.data.DataModels.Message;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ChatsAdapter extends PagedListAdapter<Message, ChatsAdapter.MyViewHolder> {

    /**
     * PagedListAdapter with default threading and
     * @param diffCallback The {@link DiffUtil.ItemCallback DiffUtil.ItemCallback} instance to
     *                     compare items in the list.
     */
    private final String userName;
    private final Context context;

    ChatsAdapter(Context context, String userName) {
        super(DIFF_CALLBACK);
        this.userName = userName;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.chat_listitem, parent, false);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Message message = getItem(position);
        //RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.rootView.getLayoutParams();
        int margin = context.getResources().getDimensionPixelSize(R.dimen.dimen_list_item_margin);
        if(message.getSender().equals(userName)){

            holder.inMessageRootView.setVisibility(View.GONE);
            holder.outMessageRootView.setVisibility(View.VISIBLE);
            holder.out_sender.setVisibility(View.GONE);
            holder.out_text.setText(message.getText());
            holder.out_time.setText(formatDate(message.getTime()));

        }else {

            holder.outMessageRootView.setVisibility(View.GONE);
            holder.inMessageRootView.setVisibility(View.VISIBLE);
            holder.in_sender.setText(message.getSender());
            holder.in_text.setText(message.getText());
            holder.in_time.setText(formatDate(message.getTime()));
        }
    }

    private String formatDate(Date date){
        SimpleDateFormat time;
        SimpleDateFormat day;
        SimpleDateFormat fullDate;

        Calendar today = new GregorianCalendar();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        long diffMillis = (today.getTimeInMillis() - date.getTime());
        long diff = TimeUnit.DAYS.convert(diffMillis, java.util.concurrent.TimeUnit.MILLISECONDS);

        if(diffMillis < 0){
            time = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return time.format(date);
        }else if(Math.abs(diff) > 7){
            fullDate = new SimpleDateFormat("dd MMM", Locale.getDefault());
            return fullDate.format(date);
        }else {
            time = new SimpleDateFormat("HH:mm", Locale.getDefault());
            day = new SimpleDateFormat("E", Locale.getDefault());
            return day.format(date) + " " + time.format(date);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView out_sender;
        TextView out_text;
        TextView out_time;
        TextView in_sender;
        TextView in_text;
        TextView in_time;
        RelativeLayout inMessageRootView;
        RelativeLayout outMessageRootView;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            out_sender = itemView.findViewById(R.id.out_message_sender);
            out_text = itemView.findViewById(R.id.out_message_text);
            out_time = itemView.findViewById(R.id.out_message_time);
            in_sender = itemView.findViewById(R.id.in_message_sender);
            in_text = itemView.findViewById(R.id.in_message_text);
            in_time = itemView.findViewById(R.id.in_message_time);
            inMessageRootView = itemView.findViewById(R.id.in_message_root_view);
            outMessageRootView = itemView.findViewById(R.id.out_message_root_view);
        }

    }

    private static final DiffUtil.ItemCallback<Message> DIFF_CALLBACK = new DiffUtil.ItemCallback<Message>() {
        @Override
        public boolean areItemsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return oldItem.equals(newItem);
        }
    };


}
