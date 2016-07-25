package com.std.logisticapp.presenter;

import com.anupcowkur.reservoir.Reservoir;
import com.std.logisticapp.bean.ProfileBean;
import com.std.logisticapp.bean.ResultBean;
import com.std.logisticapp.bean.UserBean;
import com.std.logisticapp.core.mvp.BasePresenter;
import com.std.logisticapp.model.impl.ProfileModel;
import com.std.logisticapp.presenter.iview.ProfileView;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Maik on 2016/5/18.
 */
public class ProfilePresenter extends BasePresenter<ProfileView> {
    private ProfileModel profileModel;

    @Inject
    public ProfilePresenter() {
        this.profileModel = ProfileModel.getInstance();
    }

    public void loadProfileData() {
        this.getProfileData();
    }

    private void getProfileData() {
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
                .flatMap(new Func1<UserBean, Observable<ResultBean<ProfileBean>>>() {
                    @Override
                    public Observable<ResultBean<ProfileBean>> call(final UserBean userBean) {
                        return profileModel.getProfileData(userBean.getUserId())
                                .map(new Func1<ResultBean<ProfileBean>, ResultBean<ProfileBean>>() {
                                    @Override
                                    public ResultBean<ProfileBean> call(ResultBean<ProfileBean> profileBeanResultBean) {
                                        profileBeanResultBean.getResultData().setUserBean(userBean);
                                        return profileBeanResultBean;
                                    }
                                });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultBean<ProfileBean>>() {
                    @Override
                    public void onCompleted() {
                        ProfilePresenter.this.mCompositeSubscription.remove(this);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e.toString());
                    }

                    @Override
                    public void onNext(ResultBean<ProfileBean> resultBean) {
                        ProfilePresenter.this.getMvpView().renderProfileData(resultBean.getResultData());
                    }
                }));
    }
}
