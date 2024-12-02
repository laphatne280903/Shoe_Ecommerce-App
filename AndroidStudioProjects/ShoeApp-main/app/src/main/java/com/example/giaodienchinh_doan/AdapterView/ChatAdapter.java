package com.example.giaodienchinh_doan.AdapterView;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giaodienchinh_doan.Model.ChatMessage;
import com.example.giaodienchinh_doan.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<ChatMessage> chatMessages;
    private final String senderId;
    private final String ReceiverId;
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;
    private static final int VIEW_TYPE_SENT_IMAGE = 3;
    private static final int VIEW_TYPE_RECEIVED_IMAGE = 4;

    public ChatAdapter(List<ChatMessage> chatMessages, String senderId,String Receiver) {
        this.chatMessages = chatMessages;
        this.senderId = senderId;
        this.ReceiverId=Receiver;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_sent_message, parent, false);
            return new SentMessageViewHolder(view);
        } else if (viewType == VIEW_TYPE_SENT_IMAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_sent, parent, false);
            return new SentImageViewHolder(view);
        } else if (viewType == VIEW_TYPE_RECEIVED) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_receive_mess, parent, false);
            return new ReceivedMessageViewHolder(view);
        } else if (viewType == VIEW_TYPE_RECEIVED_IMAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_receive, parent, false);
            return new ReceivedImageViewHolder(view);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);

        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            SentMessageViewHolder sentViewHolder = (SentMessageViewHolder) holder;
            sentViewHolder.textMessage.setText(chatMessage.message);
            sentViewHolder.textDateTime.setText(getReadableDateTime(chatMessage.dateTime));

        } else if (getItemViewType(position) == VIEW_TYPE_RECEIVED) {
            ReceivedMessageViewHolder receivedViewHolder = (ReceivedMessageViewHolder) holder;
            receivedViewHolder.textMessage.setText(chatMessage.message);
            receivedViewHolder.textDateTime.setText(getReadableDateTime(chatMessage.dateTime));

        }else if (getItemViewType(position) == VIEW_TYPE_SENT_IMAGE) {
            SentImageViewHolder sentViewHolder = (SentImageViewHolder) holder;
            Picasso.get().load(chatMessage.img).into(sentViewHolder.imageView); // Thay imageView bằng view tương ứng
            sentViewHolder.textDateTime.setText(getReadableDateTime(chatMessage.dateTime));

        }else if (getItemViewType(position) == VIEW_TYPE_RECEIVED_IMAGE) {
            ReceivedImageViewHolder receivedImageViewHolder = (ReceivedImageViewHolder) holder;
            Picasso.get().load(chatMessage.img).into(receivedImageViewHolder.imageView); // Thay imageView bằng view tương ứng
            receivedImageViewHolder.textDateTime.setText(getReadableDateTime(chatMessage.dateTime));

        }
    }



    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).img != null) {
            if (chatMessages.get(position).senderId.equals(senderId)) {
                return VIEW_TYPE_SENT_IMAGE;
            } else {
                return VIEW_TYPE_RECEIVED_IMAGE;
            }
        } else {
            if (chatMessages.get(position).senderId.equals(senderId)) {
                return VIEW_TYPE_SENT;
            } else {
                return VIEW_TYPE_RECEIVED;
            }
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage, textDateTime;

        SentMessageViewHolder(View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessageSent);
            textDateTime = itemView.findViewById(R.id.textDateTimeSent);
        }
    }


    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage, textDateTime;

        ReceivedMessageViewHolder(View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessageReceive);
            textDateTime = itemView.findViewById(R.id.textDateTimeReceive);
        }


    }
    static class SentImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textDateTime;

        SentImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ImageMessSent); // Thay R.id.imageViewSent bằng id tương ứng trong layout XML
            textDateTime = itemView.findViewById(R.id.textDateTimeSent); // Thay R.id.textDateTimeSent bằng id tương ứng trong layout XML
        }
    }
    static class ReceivedImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textDateTime;

        ReceivedImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ImageMessReceive); // Thay R.id.imageViewReceive bằng id tương ứng trong layout XML
            textDateTime = itemView.findViewById(R.id.textDateTimeReceive); // Thay R.id.textDateTimeReceive bằng id tương ứng trong layout XML
        }
    }

    public String getReadableDateTime(Date date) {
        return new SimpleDateFormat("MMM dd, yyy - hh:mm a", Locale.getDefault()).format(date);
    }
}