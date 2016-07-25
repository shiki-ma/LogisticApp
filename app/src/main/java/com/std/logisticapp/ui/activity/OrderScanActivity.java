package com.std.logisticapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.client.android.CaptureActivity;
import com.std.logisticapp.logistic.LogisticApi;

/**
 * Created by Maik on 2016/5/25.
 */
public class OrderScanActivity extends CaptureActivity {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, OrderScanActivity.class);
    }

    @Override
    protected void processCode(String codeText) {
        Intent data = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("ordId", codeText);
        data.putExtras(bundle);
        setResult(LogisticApi.INTENT_REQUEST_ORDER_SCAN_CODE, data);
        finish();
    }

    @Override
    protected void initData() {
    }
}
