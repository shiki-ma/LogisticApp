package com.std.logisticapp.bean;

/**
 * Created by Maik on 2016/5/18.
 */
public class ProfileBean {
    private String dispatchNum;
    private String finishNum;
    private String remindNum;
    private String picPath;
    private UserBean userBean;

    public String getDispatchNum() {
        return dispatchNum;
    }

    public void setDispatchNum(String dispatchNum) {
        this.dispatchNum = dispatchNum;
    }

    public String getFinishNum() {
        return finishNum;
    }

    public void setFinishNum(String finishNum) {
        this.finishNum = finishNum;
    }

    public String getRemindNum() {
        return remindNum;
    }

    public void setRemindNum(String remindNum) {
        this.remindNum = remindNum;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}
