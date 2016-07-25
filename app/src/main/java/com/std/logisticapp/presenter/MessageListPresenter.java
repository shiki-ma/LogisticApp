package com.std.logisticapp.presenter;

import com.anupcowkur.reservoir.Reservoir;
import com.std.logisticapp.R;
import com.std.logisticapp.bean.MessageBean;
import com.std.logisticapp.bean.ResultBean;
import com.std.logisticapp.bean.UserBean;
import com.std.logisticapp.core.mvp.BasePresenter;
import com.std.logisticapp.logistic.LogisticService;
import com.std.logisticapp.model.IMessageModel;
import com.std.logisticapp.model.impl.MessageModel;
import com.std.logisticapp.presenter.iview.MessageListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Maik on 2016/5/9.
 */
public class MessageListPresenter extends BasePresenter<MessageListView> {
    private MessageModel messageModel;

    @Inject
    public MessageListPresenter() {
        this.messageModel = MessageModel.getInstance();
    }

    public void loadMessageData(String searchKey) {
        this.getMessageList(searchKey);
    }

    private void getMessageList(final String searchKey) {
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
                .flatMap(new Func1<String, Observable<ResultBean<List<MessageBean>>>>() {
                    @Override
                    public Observable<ResultBean<List<MessageBean>>> call(String expressId) {
                        return messageModel.getMessageList(expressId + "," + searchKey);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultBean<List<MessageBean>>>() {
                    @Override
                    public void onCompleted() {
                        MessageListPresenter.this.mCompositeSubscription.remove(this);
                    }

                    @Override
                    public void onError(Throwable e) {
                        MessageListPresenter.this.getMvpView().onFailure(String.valueOf(R.string.load_error));
                        MessageListPresenter.this.getMvpView().setRefreshState(false);
                    }

                    @Override
                    public void onNext(ResultBean<List<MessageBean>> resultBean) {
                        MessageListPresenter.this.getMvpView().renderMessageList(resultBean.getResultData());
                    }
                }));
    }

    public void onMessageClicked(MessageBean message) {
        this.getMvpView().viewMessage(message);
    }
}
