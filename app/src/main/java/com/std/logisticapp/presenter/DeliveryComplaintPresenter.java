package com.std.logisticapp.presenter;

import com.std.logisticapp.bean.OrderBean;
import com.std.logisticapp.bean.ResultBean;
import com.std.logisticapp.core.mvp.BasePresenter;
import com.std.logisticapp.logistic.LogisticApi;
import com.std.logisticapp.model.impl.DeliveryModel;
import com.std.logisticapp.presenter.iview.DeliveryComplaintView;
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
public class DeliveryComplaintPresenter extends BasePresenter<DeliveryComplaintView> {
    private DeliveryModel mDeliveryModel;
    private OrderBean mOrder;

    @Inject
    public DeliveryComplaintPresenter(OrderBean order) {
        this.mDeliveryModel = DeliveryModel.getInstance();
        this.mOrder = order;
    }

    public OrderBean getOrder() {
        return mOrder;
    }

    public void loadComplaintData() {
        this.getDeliveryComplaint();
    }

    private void getDeliveryComplaint() {
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
                        return mDeliveryModel.getDeliveryComplaint(deliveryCode);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultBean<OrderBean>>() {
                    @Override
                    public void onCompleted() {
                        DeliveryComplaintPresenter.this.mCompositeSubscription.remove(this);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e.toString());
                    }

                    @Override
                    public void onNext(ResultBean<OrderBean> resultBean) {
                        DeliveryComplaintPresenter.this.getMvpView().renderOrderComplaint(resultBean.getResultData());
                    }
                }));
    }

    public void contactOrder() {
        this.mCompositeSubscription.add(
                mDeliveryModel.contactOrder(mOrder.getOrdId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<ResultBean>() {
                            @Override
                            public void onCompleted() {
                                DeliveryComplaintPresenter.this.mCompositeSubscription.remove(this);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(ResultBean result) {

                            }
                        }));
    }

    public void submitHanding() {
        final String complaintBack = this.getMvpView().getComplaintBack();
        this.mCompositeSubscription.add(mDeliveryModel.dealComplaint(mOrder.getOrdId(), complaintBack)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        DeliveryComplaintPresenter.this.getMvpView().showSubmitProgress();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultBean>() {
                    @Override
                    public void onCompleted() {
                        DeliveryComplaintPresenter.this.mCompositeSubscription.remove(this);
                    }

                    @Override
                    public void onError(Throwable e) {
                        DeliveryComplaintPresenter.this.getMvpView().closeSubmitProgress();
                        DeliveryComplaintPresenter.this.getMvpView().onFailure(e.toString());
                    }

                    @Override
                    public void onNext(ResultBean resultBean) {
                        DeliveryComplaintPresenter.this.getMvpView().closeSubmitProgress();
                        if (resultBean.getStatusCode().equals(LogisticApi.FAILURE_DATA)) {
                            DeliveryComplaintPresenter.this.getMvpView().onFailure(resultBean.getStatusMessage());
                        }else{
                            DeliveryComplaintPresenter.this.getMvpView().submitDone(resultBean.getStatusMessage(),mOrder);
                        }
                    }
                }));
    }
}
