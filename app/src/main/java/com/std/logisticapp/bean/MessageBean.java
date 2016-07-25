package com.std.logisticapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Maik on 2016/5/9.
 */
public class MessageBean implements Parcelable {
    @SerializedName("messageId") private String messageId;
    @SerializedName("messageDate") private String messageDate;
    @SerializedName("messageTitle") private String messageTitle;
    @SerializedName("messageContent") private String messageContent;
    @SerializedName("messageName") private String messageName;
    @SerializedName("messageFlag") private String messageFlag;
    private int index;
    private int position;

    public String getMessageId() {
        return messageId;
    }
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    public String getMessageDate() {
        return messageDate;
    }
    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }
    public String getMessageTitle() {
        return messageTitle;
    }
    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }
    public String getMessageContent() {
        return messageContent;
    }
    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
    public String getMessageName() {
        return messageName;
    }
    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }
    public String getMessageFlag() {
        return messageFlag;
    }
    public void setMessageFlag(String messageFlag) {
        this.messageFlag = messageFlag;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.messageId);
        dest.writeString(this.messageDate);
        dest.writeString(this.messageTitle);
        dest.writeString(this.messageContent);
        dest.writeString(this.messageName);
        dest.writeString(this.messageFlag);
        dest.writeInt(this.index);
        dest.writeInt(this.position);
    }

    public MessageBean() {
    }

    protected MessageBean(Parcel in) {
        this.messageId = in.readString();
        this.messageDate = in.readString();
        this.messageTitle = in.readString();
        this.messageContent = in.readString();
        this.messageName = in.readString();
        this.messageFlag = in.readString();
        this.index = in.readInt();
        this.position = in.readInt();
    }

    public static final Parcelable.Creator<MessageBean> CREATOR = new Parcelable.Creator<MessageBean>() {
        @Override
        public MessageBean createFromParcel(Parcel source) {
            return new MessageBean(source);
        }

        @Override
        public MessageBean[] newArray(int size) {
            return new MessageBean[size];
        }
    };
}
