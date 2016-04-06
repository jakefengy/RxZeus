package com.xm.zeus.view.home.interactor;

import com.xm.zeus.BuildConfig;
import com.xm.zeus.app.Constant;
import com.xm.zeus.db.app.entity.Colleague;
import com.xm.zeus.db.app.helper.ColleagueHelper;
import com.xm.zeus.db.user.entity.User;
import com.xm.zeus.db.user.helper.UserHelper;
import com.xm.zeus.network.Network;
import com.xm.zeus.network.extend.ApiSubscriber;
import com.xm.zeus.view.home.entity.CheckVersionResult;
import com.xm.zeus.view.home.entity.LoginOutResult;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lvxia on 2016-04-05.
 */
public class MeInteractorImpl implements IMeInteractor {

    private UserHelper userHelper;
    private ColleagueHelper colleagueHelper;
    private User user;

    public MeInteractorImpl() {
        userHelper = new UserHelper();
        colleagueHelper = new ColleagueHelper();
        user = userHelper.getLastLoggedUser();
    }

    @Override
    public void getUserInfo(ApiSubscriber<Colleague> subscriber) {
        Observable
                .create(new Observable.OnSubscribe<User>() {
                    @Override
                    public void call(Subscriber<? super User> subscriber) {
                        if (user != null) {
                            subscriber.onNext(user);
                        } else {
                            subscriber.onError(new Throwable("Not logged in user"));
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .map(new Func1<User, Colleague>() {
                    @Override
                    public Colleague call(User user) {
                        return colleagueHelper.findById(user.getUserId());
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    @Override
    public void checkAppVersion(ApiSubscriber<CheckVersionResult> subscriber) {
        Network.getZeusApis().checkVersion(Constant.Platform, BuildConfig.VERSION_NAME)
                .compose(Network.<CheckVersionResult>check())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    @Override
    public void loginOut(ApiSubscriber<Boolean> subscriber) {
        Network.getZeusApis().loginOut(user.getToken(), user.getUserId(), user.getOrg(), "")
                .compose(Network.<LoginOutResult>check())
                .subscribeOn(Schedulers.io())
                .map(new Func1<LoginOutResult, Boolean>() {
                    @Override
                    public Boolean call(LoginOutResult loginOutResult) {
                        return loginOutResult != null && loginOutResult.isok();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
