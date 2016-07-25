package com.std.logisticapp.logistic;

/**
 * Created by Maik on 2016/4/19.
 */
public class LogisticApi {
    public static final String BASE_URL = "http://61.129.251.232:8080/wlms/api/";
    //public static final String BASE_URL = "http://10.0.3.2:8080/wlms/api/";
    public static final String LOGISTIC_DATA_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String SIGN_URL = BASE_URL + "dispatch/sign/";
    public static final String APK_NAME = "logistic.apk";

    public static final String SUCCESS_DATA = "00001";
    public static final String FAILURE_DATA = "00000";

    public static final int RESULT_OK = 1;
    public static final int RESULT_DEL = 2;
    public static final int INTENT_REQUEST_MESSAGE_CODE = 10001;
    public static final int INTENT_REQUEST_ORDER_SCAN_CODE = 20001;
    public static final int INTENT_REQUEST_ORDER_DETAIL_CODE = 30001;
    public static final int INTENT_REQUEST_ORDER_COMPLAINT_CODE = 30002;
    public static final int INTENT_REQUEST_ORDER_APPOINTMENT_CODE = 30003;
    public static final int INTENT_REQUEST_ORDER_SIGN_CODE = 30004;
    public static final String INTENT_EXTRA_PARAM_MESSAGE = "org.shiki.INTENT_PARAM_MESSAGE_ID";
    public static final String INTENT_EXTRA_PARAM_DELIVERY = "org.shiki.INTENT_PARAM_DELIVERY_ID";
}
