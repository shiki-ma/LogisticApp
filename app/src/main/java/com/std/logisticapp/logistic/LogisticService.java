package com.std.logisticapp.logistic;

import com.std.logisticapp.bean.MessageBean;
import com.std.logisticapp.bean.OrderBean;
import com.std.logisticapp.bean.ProfileBean;
import com.std.logisticapp.bean.ResultBean;
import com.std.logisticapp.bean.UserBean;
import com.std.logisticapp.bean.VersionBean;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Maik on 2016/4/25.
 */
public interface LogisticService {

    @GET("app/getVersion")
    Observable<ResultBean<VersionBean>> getVersion();

    @GET("user/oauth/{usercode}/{password}")
    Observable<ResultBean<UserBean>> login(@Path("usercode") String usercode, @Path("password") String password);

    @GET("message/{expressKey}")
    Observable<ResultBean<List<MessageBean>>> getMessageList(@Path("expressKey") String expressKey);

    @GET("message/{expressId}/{messageId}")
    Observable<ResultBean<MessageBean>> getMessageDetail(@Path("expressId") String expressId, @Path("messageId") String messageId);

    @GET("dispatch/receipt/{expressId}")
    Observable<ResultBean<List<OrderBean>>> receiptOrderList(@Path("expressId") String expressId, @Query("searchKey") String searchKey);

    @PUT("dispatch/receipt/{deliveryCodes}")
    Observable<ResultBean> receiptOrders(@Path("deliveryCodes") String deliveryCodes);

    @GET("user/profile/{expressId}")
    Observable<ResultBean<ProfileBean>> getProfileData(@Path("expressId") String expressId);

    @GET("dispatch/delivery/{expressId}")
    Observable<ResultBean<List<OrderBean>>> getDeliveryList(@Path("expressId") String expressId,@Query("searchKey") String searchKey);

    @GET("dispatch/{deliveryCode}")
    Observable<ResultBean<OrderBean>> getDeliveryDetail(@Path("deliveryCode") String deliveryCode);

    @PUT("dispatch/appointment/{deliveryCode}")
    Observable<ResultBean> modifyAppointment(@Path("deliveryCode") String deliveryCode, @Query("appoTime") String appoTime, @Query("remark") String remark);

    @GET("dispatch/complaint/{deliveryCode}")
    Observable<ResultBean<OrderBean>> getDeliveryComplaint(@Path("deliveryCode") String deliveryCode);

    @PUT("dispatch/complaint/{deliveryCode}")
    Observable<ResultBean> dealComplaint(@Path("deliveryCode") String deliveryCode, @Query("remark") String remark);

    @PUT("dispatch/contactOrder/{deliveryCode}")
    Observable<ResultBean> contactOrder(@Path("deliveryCode") String deliveryCodes);

    @PUT("dispatch/back/{deliveryCode}")
    Observable<ResultBean> backOrder(@Path("deliveryCode") String deliveryCodes, @Query("backReason") String backReason);
}
