package com.std.logisticapp.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Maik on 2016/4/28.
 */
public class ResultBean<U> {
    @SerializedName("statusCode") private String statusCode;
    @SerializedName("statusMessage") private String statusMessage;
    @SerializedName("resultData") private U resultData;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public U getResultData() {
        return resultData;
    }

    public void setResultData(U resultData) {
        this.resultData = resultData;
    }
}
