package com.std.logisticapp.logistic;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Maik on 2016/4/27.
 */
public class LoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        com.orhanobut.logger.Logger.d(chain.request().urlString());
        return response;
    }
}
