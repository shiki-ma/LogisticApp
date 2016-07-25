package com.std.logisticapp.presenter;

import com.anupcowkur.reservoir.Reservoir;
import com.shiki.utils.ReservoirUtils;
import com.shiki.utils.StringUtils;
import com.shiki.utils.coder.MD5Coder;
import com.std.logisticapp.bean.ResultBean;
import com.std.logisticapp.bean.UserBean;
import com.std.logisticapp.core.mvp.BasePresenter;
import com.std.logisticapp.logistic.LogisticApi;
import com.std.logisticapp.model.impl.LoginModel;
import com.std.logisticapp.presenter.iview.LoginView;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Maik on 2016/4/29.
 */
public class LoginPresenter extends BasePresenter<LoginView> {
    private LoginModel loginModel;

    public LoginPresenter() {
        this.loginModel = LoginModel.getInstance();
    }

    public void showUserAndPasswd() {
        this.mCompositeSubscription.add(Observable.create(
                new Observable.OnSubscribe<UserBean>() {
                    @Override
                    public void call(Subscriber<? super UserBean> subscriber) {
                        try {
                            if (Reservoir.contains("userInfo")) {
                                UserBean userBean = Reservoir.get("userInfo", UserBean.class);
                                subscriber.onNext(userBean);
                            }
                            subscriber.onCompleted();
                        } catch (Exception e) {
                            subscriber.onError(e);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserBean>() {
                    @Override
                    public void onCompleted() {
                        LoginPresenter.this.mCompositeSubscription.remove(this);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(UserBean userBean) {
                        LoginPresenter.this.getMvpView().setUserCode(userBean.getUsercode());
                        if (userBean.getIsRemember()) {
                            LoginPresenter.this.getMvpView().setPasswd(userBean.getUserPwd());
                            LoginPresenter.this.getMvpView().setRemeber(userBean.getIsRemember());
                        }
                    }
                }));
    }

    public void login() {
        final String usercode = this.getMvpView().getUserCode();
        final String passwd = this.getMvpView().getPasswd();
        if (StringUtils.isEmpty(usercode)||StringUtils.isEmpty(passwd)) {
            LoginPresenter.this.getMvpView().onFailure("账号或密码不能为空！");
            return;
        }
        final Boolean isRemember = this.getMvpView().getRemember();
        this.mCompositeSubscription.add(loginModel.login(usercode, MD5Coder.getMD5Code(passwd))
                .flatMap(new Func1<ResultBean<UserBean>, Observable<String>>() {
                    @Override
                    public Observable<String> call(final ResultBean<UserBean> resultBean) {
                        if (resultBean.getStatusCode().equals(LogisticApi.FAILURE_DATA)) {
                            return Observable.just(resultBean.getStatusMessage());
                        } else {
                            return Observable.create(new Observable.OnSubscribe<String>() {
                                @Override
                                public void call(Subscriber<? super String> subscriber) {
                                    try {
                                        resultBean.getResultData().setIsRemember(isRemember);
                                        ReservoirUtils.getInstance().refresh("userInfo", resultBean.getResultData());
                                        subscriber.onNext(null);
                                        subscriber.onCompleted();
                                    } catch (Exception e) {
                                        subscriber.onError(e);
                                    }
                                }
                            });
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        LoginPresenter.this.getMvpView().showLoginProgress();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        LoginPresenter.this.mCompositeSubscription.remove(this);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoginPresenter.this.getMvpView().hideLoginProgress();
                        LoginPresenter.this.getMvpView().onFailure(e.toString());
                    }

                    @Override
                    public void onNext(String resultError) {
                        if (resultError != null) {
                            LoginPresenter.this.getMvpView().hideLoginProgress();
                            LoginPresenter.this.getMvpView().onFailure(resultError);
                        } else {
                            LoginPresenter.this.getMvpView().enterMain();
                        }
                    }
                }));
    }
}
