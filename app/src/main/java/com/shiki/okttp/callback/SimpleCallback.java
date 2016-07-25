package com.shiki.okttp.callback;

import com.google.gson.Gson;
import com.std.logisticapp.bean.ResultBean;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by Maik on 2016/5/27.
 */
public abstract class SimpleCallback extends Callback<ResultBean> {

    @Override
    public ResultBean parseNetworkResponse(Response response) throws IOException {
        String str = response.body().string();
        ResultBean res = new Gson().fromJson(str, ResultBean.class);
        return res;
    }
}
