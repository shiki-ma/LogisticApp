package com.std.logisticapp.presenter;

import com.std.logisticapp.bean.OrderBean;
import com.std.logisticapp.bean.ResultBean;
import com.std.logisticapp.core.mvp.BasePresenter;
import com.std.logisticapp.logistic.LogisticApi;
import com.std.logisticapp.model.impl.DeliveryModel;
import com.std.logisticapp.presenter.iview.DeliveryDetailView;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Eric on 2016/5/12.
 */
public class DeliveryDetailPresenter extends BasePresenter<DeliveryDetailView> {
    private DeliveryModel mDeliveryModel;

    private OrderBean mOrder;

    public OrderBean getOrder() {
        return mOrder;
    }

    @Inject
    public DeliveryDetailPresenter(OrderBean order) {
        this.mDeliveryModel = DeliveryModel.getInstance();
        this.mOrder = order;
    }

    public void changeOrderAppointment(String appointment) {
        mOrder.setAppointment(appointment);
    }

    public void loadDetailData() {
        this.getDeliveryDetail();
    }

    private void getDeliveryDetail() {
        this.mCompositeSubscription.add(Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext(mOrder.getOrdId());
                        subscriber.onCompleted();
                    }
                })
                .flatMap(new Func1<String, Observable<ResultBean<OrderBean>>>() {
                    @Override
                    public Observable<ResultBean<OrderBean>> call(String deliveryCode) {
                        return mDeliveryModel.getDeliveryDetail(deliveryCode);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultBean<OrderBean>>() {
                    @Override
                    public void onCompleted() {
                        DeliveryDetailPresenter.this.mCompositeSubscription.remove(this);
                    }

                    @Override
                    public void onError(Throwable e) {
                        DeliveryDetailPresenter.this.getMvpView().onFailure(e.toString());
                    }

                    @Override
                    public void onNext(ResultBean<OrderBean> resultBean) {
                        DeliveryDetailPresenter.this.getMvpView().renderOrderDetail(resultBean.getResultData());
                    }
                }));
    }

    public void changeAppointment() {
        DeliveryDetailPresenter.this.getMvpView().changeAppointment(mOrder);
    }

    public void sign() {
        DeliveryDetailPresenter.this.getMvpView().sign(mOrder);
    }

    public void contactOrder() {
        this.mCompositeSubscription.add(
                mDeliveryModel.contactOrder(mOrder.getOrdId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<ResultBean>() {
                            @Override
                            public void onCompleted() {
                                DeliveryDetailPresenter.this.mCompositeSubscription.remove(this);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(ResultBean result) {

                            }
                        }));
    }

    public void backOrder() {
        String backReason = this.getMvpView().getBackReason();
        this.mCompositeSubscription.add(
                mDeliveryModel.backOrder(mOrder.getOrdId(),backReason)
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                DeliveryDetailPresenter.this.getMvpView().showSubmitProgress();
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<ResultBean>() {
                            @Override
                            public void onCompleted() {
                                DeliveryDetailPresenter.this.mCompositeSubscription.remove(this);
                            }

                            @Override
                            public void onError(Throwable e) {
                                DeliveryDetailPresenter.this.getMvpView().closeSubmitProgress();
                                DeliveryDetailPresenter.this.getMvpView().onFailure(e.toString());
                            }

                            @Override
                            public void onNext(ResultBean resultBean) {
                                DeliveryDetailPresenter.this.getMvpView().closeSubmitProgress();
                                if (resultBean.getStatusCode().equals(LogisticApi.FAILURE_DATA)) {
                                    DeliveryDetailPresenter.this.getMvpView().onFailure(resultBean.getStatusMessage());
                                }else{
                                    DeliveryDetailPresenter.this.getMvpView().submitDone(mOrder);
                                }
                            }
                        }));
    }
}
