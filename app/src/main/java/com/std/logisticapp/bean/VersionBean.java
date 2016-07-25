package com.std.logisticapp.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Maik on 2016/5/3.
 */
public class VersionBean {
    @SerializedName("versionName") private String versionName;
    @SerializedName("versionCode") private Integer versionCode;
    @SerializedName("versionDesc") private String versionDesc;
    @SerializedName("versionUrl") private String versionUrl;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
    }
}
