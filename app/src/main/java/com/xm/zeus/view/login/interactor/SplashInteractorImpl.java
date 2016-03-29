package com.xm.zeus.view.login.interactor;

import android.content.Context;

import com.xm.zeus.Constant;
import com.xm.zeus.db.app.entity.Colleague;
import com.xm.zeus.db.app.entity.TimeStamp;
import com.xm.zeus.db.app.helper.TimeStampHelper;
import com.xm.zeus.db.user.entity.User;
import com.xm.zeus.db.user.helper.UserHelper;
import com.xm.zeus.network.Network;
import com.xm.zeus.network.extend.ApiException;
import com.xm.zeus.network.extend.CancelSubscriber;
import com.xm.zeus.network.extend.MapFunc1;
import com.xm.zeus.utils.Logger;

import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lvxia on 2016-03-28.
 */
public class SplashInteractorImpl implements ISplashInteractor {

    private static final String TAG = "SplashInteractorTag";

    private UserHelper userHelper;
    private TimeStampHelper timeStampHelper;

    public SplashInteractorImpl() {

    }

    @Override
    public void initAppDB(Context context) {
        Logger.i(TAG, "SplashInteractorImpl.initAppDB");
        userHelper = new UserHelper(context);
        timeStampHelper = new TimeStampHelper(context);
    }

    @Override
    public void initGallery() {

        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        Logger.i(TAG, "SplashInteractorImpl.initGallery.Completed");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Logger.i(TAG, "SplashInteractorImpl.initGallery " + throwable.toString());
                    }
                });
    }

    @Override
    public User getLastLoggedUser() {
        if (userHelper == null) {
            return null;
        }

        return userHelper.getLastLoggedUser();
    }

    @Override
    public boolean checkNetwork(Context context) {
        return Network.isNetworkAvailable(context);
    }

    @Override
    public void downloadContacts(User user, long timestamp, CancelSubscriber subscriber) {

        Network.getZeusApis().getColleague(user.getToken(), user.getUserId(), Constant.Organization, timestamp, "false")
                .subscribeOn(Schedulers.io())
                .map(new MapFunc1<List<Colleague>>())
                .flatMap(new Func1<List<Colleague>, Observable<Colleague>>() {
                    @Override
                    public Observable<Colleague> call(List<Colleague> colleagues) {
                        return Observable.from(colleagues);
                    }
                })
                .doOnNext(new Action1<Colleague>() {
                    @Override
                    public void call(Colleague colleague) {

                        // save to app db
                        processColleague(colleague);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    private void processColleague(Colleague colleague) {

    }


}
