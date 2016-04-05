package com.xm.zeus.view.home.interactor;

import com.xm.zeus.db.app.entity.Colleague;
import com.xm.zeus.network.extend.ApiSubscriber;
import com.xm.zeus.view.home.entity.CheckVersionResult;

/**
 * Created by lvxia on 2016-04-05.
 */
public interface IMeInteractor {

    void getUserInfo(ApiSubscriber<Colleague> subscriber);

    void checkAppVersion(ApiSubscriber<CheckVersionResult> subscriber);

    void loginOut(ApiSubscriber<Boolean> subscriber);

}
