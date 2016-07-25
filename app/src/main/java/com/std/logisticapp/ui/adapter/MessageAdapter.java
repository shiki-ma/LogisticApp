package com.std.logisticapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shiki.recyclerview.FGORecyclerViewAdapter;
import com.std.logisticapp.R;
import com.std.logisticapp.bean.MessageBean;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Maik on 2016/5/9.
 */
public class MessageAdapter extends FGORecyclerViewAdapter<MessageAdapter.MessageViewHolder> {
    public interface OnItemClickListener {
        void onMessageItemClicked(MessageBean messageBean);
    }

    private List<MessageBean> messageData;
    private final LayoutInflater layoutInflater;

    private OnItemClickListener onItemClickListener;

    @Inject
    public MessageAdapter(Context context) {
        this.layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.messageData = Collections.emptyList();
    }

    public void updateMessage(MessageBean message) {
        MessageBean newMessage = this.messageData.get(message.getIndex());
        newMessage.setMessageFlag(message.getMessageFlag());
        this.notifyItemChanged(message.getPosition());
    }

    public void setMessageData(List<MessageBean> messageData) {
        this.messageData = messageData;
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MessageViewHolder getViewHolder(View view) {
        return new MessageViewHolder(view);
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent) {
        final View view = this.layoutInflater.inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public int getAdapterItemCount() {
        return messageData.size();
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, final int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= messageData.size() : position < messageData.size()) && (customHeaderView != null ? position > 0 : true)) {
            final int index = customHeaderView != null ? position - 1 : position;
            final MessageBean message = messageData.get(index);
            holder.tvMsgDate.setText("发布时间：" + message.getMessageDate());
            holder.tvMsgTitle.setText("公告标题：" + message.getMessageTitle());
            holder.tvMsgUser.setText("发布人：" + message.getMessageName());
            if (message.getMessageFlag().equals("1")) {
                holder.ivMsgFlag.setVisibility(View.VISIBLE);
            } else {
                holder.ivMsgFlag.setVisibility(View.GONE);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MessageAdapter.this.onItemClickListener != null) {
                        message.setIndex(index);
                        message.setPosition(position);
                        MessageAdapter.this.onItemClickListener.onMessageItemClicked(message);
                    }
                }
            });
        }
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_msgDate) TextView tvMsgDate;
        @Bind(R.id.tv_msgUser) TextView tvMsgUser;
        @Bind(R.id.tv_msgTitle) TextView tvMsgTitle;
        @Bind(R.id.tv_MsgFlag) ImageView ivMsgFlag;

        public MessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
