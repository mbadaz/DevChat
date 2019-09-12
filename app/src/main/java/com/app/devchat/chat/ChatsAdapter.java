package com.app.devchat.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.devchat.R;
import com.app.devchat.ThreadHelper;
import com.app.devchat.data.DataModels.Message;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;
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
    private static final Calendar today;
    private String previousMessageSender = null;

    static {
        today = new GregorianCalendar();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
    }


    ChatsAdapter(Context context, String userName) {
        super(DIFF_CALLBACK);
        this.userName = userName;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).
                    inflate(viewType, parent, false);



        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Message message = getItem(position);
        //RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.rootView.getLayoutParams();
        int margin = context.getResources().getDimensionPixelSize(R.dimen.dimen_list_item_margin);
        holder.text.setText(message.text);
        holder.time.setText(formatDate(message.time));
        if(holder.sender != null){
            holder.sender.setText(message.sender);
        }
    }

    /**
     * Return the view type of the item at <code>position</code> for the purposes
     * of view recycling.
     *
     * <p>The default implementation of this method returns 0, making the assumption of
     * a single view type for the adapter. Unlike ListView adapters, types need not
     * be contiguous. Consider using id resources to uniquely identify item view types.
     *
     * @param position position to query
     * @return integer value identifying the type of the view needed to represent the item at
     * <code>position</code>. Type codes need not be contiguous.
     */
    @Override
    public int getItemViewType(int position) {
        String sender = Objects.requireNonNull(getItem(position)).sender;
        boolean isInMessage = !sender.equals(userName);
        boolean isSameSender = getItemCount() -1 != position && Objects.requireNonNull(getItem(position + 1)).
                sender.equals(Objects.requireNonNull(getItem(position)).sender);

        if (isInMessage) {
            if (isSameSender) {

                return R.layout.in_message_list_item_no_pointer;
            } else {

                return R.layout.in_message_list_item;
            }
        } else {
            if (isSameSender ) {

                return R.layout.out_message_list_item_no_pointer;
            } else {

                return R.layout.out_message_list_item;
            }
        }

    }

    private String formatDate(Date date){
        SimpleDateFormat time;
        SimpleDateFormat day;
        SimpleDateFormat fullDate;


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
        TextView sender;
        TextView text;
        TextView time;


        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.message_text);
            time = itemView.findViewById(R.id.message_time);
            if (itemView.getId() == R.id.in_message_root) {
                sender = itemView.findViewById(R.id.message_sender);
            } else {
                sender = null;
            }

        }

    }

    private static final DiffUtil.ItemCallback<Message> DIFF_CALLBACK = new DiffUtil.ItemCallback<Message>() {
        @Override
        public boolean areItemsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return oldItem.id == newItem.id;
        }
    };


}
