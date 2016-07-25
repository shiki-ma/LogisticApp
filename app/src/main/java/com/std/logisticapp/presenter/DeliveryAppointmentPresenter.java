package com.std.logisticapp.presenter;

import com.std.logisticapp.bean.OrderBean;
import com.std.logisticapp.bean.ResultBean;
import com.std.logisticapp.core.mvp.BasePresenter;
import com.std.logisticapp.logistic.LogisticApi;
import com.std.logisticapp.model.impl.DeliveryModel;
import com.std.logisticapp.presenter.iview.DeliveryAppointmentView;
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
public class DeliveryAppointmentPresenter extends BasePresenter<DeliveryAppointmentView> {
    private DeliveryModel mDeliveryModel;
    private OrderBean mOrder;

    @Inject
    public DeliveryAppointmentPresenter(OrderBean order) {
        this.mDeliveryModel = DeliveryModel.getInstance();
        this.mOrder = order;
    }

    public void loadAppointment() {
        DeliveryAppointmentPresenter.this.getMvpView().loadAppointment(mOrder);
    }

    public void submitdAppointment() {
        final String appointmentTime = this.getMvpView().getAppointmentTime();
        final String appointmentReason = this.getMvpView().getAppointmentReason();
        this.mCompositeSubscription.add(mDeliveryModel.modifyAppointment(mOrder.getOrdId(), appointmentTime, appointmentReason)
                .flatMap(new Func1<ResultBean, Observable<String>>() {
                    @Override
                    public Observable<String> call(final ResultBean resultBean) {
                        if (resultBean.getStatusCode().equals(LogisticApi.FAILURE_DATA)) {
                            return Observable.just(resultBean.getStatusMessage());
                        } else {
                            return Observable.create(new Observable.OnSubscribe<String>() {
                                @Override
                                public void call(Subscriber<? super String> subscriber) {
                                    subscriber.onNext(null);
                                    subscriber.onCompleted();
                                }
                            });
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        DeliveryAppointmentPresenter.this.getMvpView().showSubmitProgress();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        DeliveryAppointmentPresenter.this.mCompositeSubscription.remove(this);
                    }

                    @Override
                    public void onError(Throwable e) {
                        DeliveryAppointmentPresenter.this.getMvpView().closeSubmitProgress();
                        DeliveryAppointmentPresenter.this.getMvpView().onFailure(e.toString());
                    }

                    @Override
                    public void onNext(String resultError) {
                        DeliveryAppointmentPresenter.this.getMvpView().closeSubmitProgress();
                        if (resultError != null) {
                            DeliveryAppointmentPresenter.this.getMvpView().onFailure(resultError);
                        } else {

                            DeliveryAppointmentPresenter.this.getMvpView().submitDone();
                        }
                    }
                }));
    }
}
