package com.std.logisticapp.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Maik on 2016/4/25.
 */
public class UserBean {
    @SerializedName("userId") private String userId;
    @SerializedName("usercode") private String usercode;
    @SerializedName("userName") private String userName;
    @SerializedName("userPhoto") private String userPhoto;
    @SerializedName("userOrga") private String userOrga;
    @SerializedName("userPwd") private String userPwd;
    @SerializedName("userTel") private String userTel;
    private Boolean isRemember;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public Boolean getIsRemember() {
        return isRemember;
    }

    public void setIsRemember(Boolean isRemember) {
        this.isRemember = isRemember;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserOrga() {
        return userOrga;
    }

    public void setUserOrga(String userOrga) {
        this.userOrga = userOrga;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }
}
