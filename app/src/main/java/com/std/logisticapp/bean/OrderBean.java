package com.std.logisticapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Maik on 2016/5/16.
 */
public class OrderBean implements Parcelable {
    private String ordId;
    private String ordCode;
    private String deliveryCode;
    private String logisticCode;
    private String deliveryAddr;
    private String recipient;
    private String exprItem;
    private String appointment;
    private String deliveryFlag;
    private String ordTel;
    private String ordRemark;
    private String complaintBack;
    private Integer remindNum;
    private Integer complaintNum;
    private Integer lateNum;
    private Boolean isSelected;
    private int index;
    private int position;

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

    public String getOrdId() {
        return ordId;
    }

    public void setOrdId(String ordId) {
        this.ordId = ordId;
    }

    public String getOrdCode() {
        return ordCode;
    }

    public void setOrdCode(String ordCode) {
        this.ordCode = ordCode;
    }

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
    }

    public String getLogisticCode() {
        return logisticCode;
    }

    public void setLogisticCode(String logisticCode) {
        this.logisticCode = logisticCode;
    }

    public String getDeliveryAddr() {
        return deliveryAddr;
    }

    public void setDeliveryAddr(String deliveryAddr) {
        this.deliveryAddr = deliveryAddr;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getExprItem() {
        return exprItem;
    }

    public void setExprItem(String exprItem) {
        this.exprItem = exprItem;
    }

    public String getAppointment() {
        return appointment;
    }

    public void setAppointment(String appointment) {
        this.appointment = appointment;
    }

    public String getDeliveryFlag() {
        return deliveryFlag;
    }

    public void setDeliveryFlag(String deliveryFlag) {
        this.deliveryFlag = deliveryFlag;
    }

    public String getOrdTel() {
        return ordTel;
    }

    public void setOrdTel(String ordTel) {
        this.ordTel = ordTel;
    }

    public String getOrdRemark() {
        return ordRemark;
    }

    public void setOrdRemark(String ordRemark) {
        this.ordRemark = ordRemark;
    }

    public String getComplaintBack() {
        return complaintBack;
    }

    public void setComplaintBack(String complaintBack) {
        this.complaintBack = complaintBack;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Integer getRemindNum() {
        return remindNum;
    }

    public void setRemindNum(Integer remindNum) {
        this.remindNum = remindNum;
    }

    public Integer getComplaintNum() {
        return complaintNum;
    }

    public void setComplaintNum(Integer complaintNum) {
        this.complaintNum = complaintNum;
    }

    public Integer getLateNum() {
        return lateNum;
    }

    public void setLateNum(Integer lateNum) {
        this.lateNum = lateNum;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public OrderBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ordId);
        dest.writeString(this.ordCode);
        dest.writeString(this.deliveryCode);
        dest.writeString(this.logisticCode);
        dest.writeString(this.deliveryAddr);
        dest.writeString(this.recipient);
        dest.writeString(this.exprItem);
        dest.writeString(this.appointment);
        dest.writeString(this.deliveryFlag);
        dest.writeString(this.ordTel);
        dest.writeString(this.ordRemark);
        dest.writeString(this.complaintBack);
        dest.writeValue(this.remindNum);
        dest.writeValue(this.complaintNum);
        dest.writeValue(this.lateNum);
        dest.writeValue(this.isSelected);
        dest.writeInt(this.index);
        dest.writeInt(this.position);
    }

    protected OrderBean(Parcel in) {
        this.ordId = in.readString();
        this.ordCode = in.readString();
        this.deliveryCode = in.readString();
        this.logisticCode = in.readString();
        this.deliveryAddr = in.readString();
        this.recipient = in.readString();
        this.exprItem = in.readString();
        this.appointment = in.readString();
        this.deliveryFlag = in.readString();
        this.ordTel = in.readString();
        this.ordRemark = in.readString();
        this.complaintBack = in.readString();
        this.remindNum = (Integer) in.readValue(Integer.class.getClassLoader());
        this.complaintNum = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lateNum = (Integer) in.readValue(Integer.class.getClassLoader());
        this.isSelected = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.index = in.readInt();
        this.position = in.readInt();
    }

    public static final Creator<OrderBean> CREATOR = new Creator<OrderBean>() {
        @Override
        public OrderBean createFromParcel(Parcel source) {
            return new OrderBean(source);
        }

        @Override
        public OrderBean[] newArray(int size) {
            return new OrderBean[size];
        }
    };
}
