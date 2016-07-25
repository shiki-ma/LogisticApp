package com.std.logisticapp.presenter;

import com.anupcowkur.reservoir.Reservoir;
import com.std.logisticapp.R;
import com.std.logisticapp.bean.MessageBean;
import com.std.logisticapp.bean.OrderBean;
import com.std.logisticapp.bean.ResultBean;
import com.std.logisticapp.bean.UserBean;
import com.std.logisticapp.core.mvp.BasePresenter;
import com.std.logisticapp.model.impl.OrderModel;
import com.std.logisticapp.presenter.iview.OrderReceiptView;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Maik on 2016/5/16.
 */
public class OrderReceiptPresenter extends BasePresenter<OrderReceiptView> {
    private OrderModel orderModel;

    @Inject
    public OrderReceiptPresenter() {
        this.orderModel = OrderModel.getInstance();
    }

    public void loadOrderData(String searchKey) {
        this.getOrderList(searchKey);
    }

    private void getOrderList(final String searchKey) {
        this.mCompositeSubscription.add(Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        try {
                            if (Reservoir.contains("userInfo")) {
                                UserBean userBean = Reservoir.get("userInfo", UserBean.class);
                                subscriber.onNext(userBean.getUserId());
                            }
                            subscriber.onCompleted();
                        } catch (Exception e) {
                            subscriber.onError(e);
                        }
                    }
                })
                .flatMap(new Func1<String, Observable<ResultBean<List<OrderBean>>>>() {
                    @Override
                    public Observable<ResultBean<List<OrderBean>>> call(String expressId) {
                        return orderModel.receiptOrderList(expressId, searchKey);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultBean<List<OrderBean>>>() {
                    @Override
                    public void onCompleted() {
                        OrderReceiptPresenter.this.mCompositeSubscription.remove(this);
                    }

                    @Override
                    public void onError(Throwable e) {
                        OrderReceiptPresenter.this.getMvpView().onFailure(String.valueOf(R.string.load_error));
                        OrderReceiptPresenter.this.getMvpView().setRefreshState(false);
                    }

                    @Override
                    public void onNext(ResultBean<List<OrderBean>> resultBean) {
                        OrderReceiptPresenter.this.getMvpView().renderOrderList(resultBean.getResultData());
                    }
                }));
    }

    public void receiptSelected(String deliveryCodes) {
        receipt(deliveryCodes);
    }

    private void receipt(String deliveryCodes) {
        if (deliveryCodes.equals("")) {
            OrderReceiptPresenter.this.getMvpView().onFailure(String.valueOf(R.string.no_select));
            return;
        }
        this.mCompositeSubscription.add(orderModel.receiptOrders(deliveryCodes)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<ResultBean>() {
                            @Override
                            public void onCompleted() {
                                OrderReceiptPresenter.this.mCompositeSubscription.remove(this);
                            }

                            @Override
                            public void onError(Throwable e) {
                                OrderReceiptPresenter.this.getMvpView().onFailure(e.toString());
                            }

                            @Override
                            public void onNext(ResultBean resultBean) {
                                OrderReceiptPresenter.this.getMvpView().onFailure(resultBean.getStatusMessage());
                                OrderReceiptPresenter.this.getMvpView().refreshOrderList();
                            }
                        })
        );
    }
}
