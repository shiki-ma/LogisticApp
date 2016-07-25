package com.std.logisticapp.presenter;

import com.anupcowkur.reservoir.Reservoir;
import com.shiki.utils.StringUtils;
import com.std.logisticapp.bean.OrderBean;
import com.std.logisticapp.bean.ResultBean;
import com.std.logisticapp.bean.UserBean;
import com.std.logisticapp.core.mvp.BasePresenter;
import com.std.logisticapp.model.impl.DeliveryModel;
import com.std.logisticapp.presenter.iview.DeliveryComplaintListView;
import com.std.logisticapp.presenter.iview.DeliveryListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Eric on 2016/5/19.
 */
public class DeliveryComplaintListPresenter extends BasePresenter<DeliveryComplaintListView> {
    private DeliveryModel mDeliveryModel;
    private List<OrderBean> mOrderList;

    @Inject
    public DeliveryComplaintListPresenter() {
        this.mDeliveryModel = DeliveryModel.getInstance();
    }

    public void loadMessageData(String search) {
        this.getDeliveryList(search);
    }

    private void getDeliveryList(final String search) {
        mOrderList = new ArrayList<>();
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
                            //subscriber.onNext("1");
                        } catch (Exception e) {
                            subscriber.onError(e);
                        }
                    }
                })
                .flatMap(new Func1<String, Observable<ResultBean<List<OrderBean>>>>() {
                    @Override
                    public Observable<ResultBean<List<OrderBean>>> call(String expressId) {
                        return mDeliveryModel.getDeliveryListSearch(expressId, StringUtils.nullStrToEmpty(search));
                    }
                })
                .map(new Func1<ResultBean<List<OrderBean>>, List<OrderBean>>() {

                    @Override
                    public List<OrderBean> call(ResultBean<List<OrderBean>> resultBean) {
                        return resultBean.getResultData();
                    }
                })
                .flatMap(new Func1<List<OrderBean>, Observable<OrderBean>>() {
                    @Override
                    public Observable<OrderBean> call(List<OrderBean> orderBeen) {
                        return Observable.from(orderBeen);
                    }
                })
                .filter(new Func1<OrderBean, Boolean>() {
                    @Override
                    public Boolean call(OrderBean orderBean) {
                        boolean result = false;
                        if(orderBean.getComplaintNum()>0){
                            result = true;
                        }
                        return result;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<OrderBean>() {
                    @Override
                    public void onCompleted() {
                        DeliveryComplaintListPresenter.this.getMvpView().renderDeliveryList(mOrderList);
                        DeliveryComplaintListPresenter.this.mCompositeSubscription.remove(this);
                    }

                    @Override
                    public void onError(Throwable e) {
                        DeliveryComplaintListPresenter.this.getMvpView().onFailure(e.toString());
                        DeliveryComplaintListPresenter.this.getMvpView().setRefreshState(false);
                    }

                    @Override
                    public void onNext(OrderBean order) {
                        mOrderList.add(order);
                    }
                }));
    }

    public void onDeliveryClicked(OrderBean order) {
        this.getMvpView().viewDelivery(order);
    }

    public void OnComplaintClicked(OrderBean order) {
        this.getMvpView().complaintHandingDelivery(order);
    }

    public void contactOrder(OrderBean order) {
        this.mCompositeSubscription.add(
                mDeliveryModel.contactOrder(order.getOrdId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<ResultBean>() {
                            @Override
                            public void onCompleted() {
                                DeliveryComplaintListPresenter.this.mCompositeSubscription.remove(this);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(ResultBean result) {

                            }
                        }));
    }
}
