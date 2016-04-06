package com.xm.zeus.view.home.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.xm.zeus.db.app.entity.Colleague;
import com.xm.zeus.db.app.entity.Setting;
import com.xm.zeus.db.app.helper.SettingHelper;
import com.xm.zeus.network.Network;
import com.xm.zeus.network.extend.ApiSubscriber;
import com.xm.zeus.view.home.entity.CheckVersionResult;
import com.xm.zeus.view.home.interactor.IMeInteractor;
import com.xm.zeus.view.home.interactor.MeInteractorImpl;
import com.xm.zeus.view.home.iview.IMeView;

/**
 * Created by lvxia on 2016-04-05.
 */
public class MePresenterImpl implements IMePresenter {

    private Context context;
    private IMeView meView;
    private IMeInteractor interactor;

    private SettingHelper settingHelper;

    public MePresenterImpl(Context context, IMeView meView) {
        this.context = context;
        this.meView = meView;

        interactor = new MeInteractorImpl();
        settingHelper = new SettingHelper();

    }

    @Override
    public void getUserInfo() {

        interactor.getUserInfo(new ApiSubscriber<Colleague>() {
            @Override
            protected void onCommonError(Throwable e) {
                callbackError(e);
            }

            @Override
            public void onNext(Colleague colleague) {
                if (meView != null) {
                    meView.setUserInfo(colleague);
                }
            }
        });
    }

    @Override
    public void checkAppVersion() {

        if (!Network.isAvailable(context)) {
            callbackError(new Throwable("网络不可用"));
            return;
        }

        interactor.checkAppVersion(new ApiSubscriber<CheckVersionResult>() {

            @Override
            public void onStart() {
                settingHelper.deleteByKey(SettingHelper.Keys.AppUpgrade);
            }

            @Override
            protected void onCommonError(Throwable e) {
                callbackError(e);
            }

            @Override
            public void onNext(CheckVersionResult newVersion) {

                if (newVersion == null) {
                    return;
                }

                Setting setting = new Setting();
                setting.setKey(SettingHelper.Keys.AppUpgrade.getCode());
                setting.setContent(new Gson().toJson(newVersion));
                setting.setRemark(SettingHelper.Keys.AppUpgrade.getName());
                settingHelper.insertOrUpdate(setting);

                if (meView != null) {
                    meView.updateApp(newVersion);
                }
            }
        });
    }

    @Override
    public void loginOut() {

        if (!Network.isAvailable(context)) {
            callbackError(new Throwable("网络不可用"));
            return;
        }

        interactor.loginOut(new ApiSubscriber<Boolean>() {
            @Override
            protected void onCommonError(Throwable e) {
                callbackError(e);
            }

            @Override
            public void onNext(Boolean isSignedOut) {
                if (meView != null) {
                    if (isSignedOut) {
                        meView.signedOutComplete();
                    } else {
                        meView.signedOutFail();
                    }
                }
            }
        });
    }

    private void callbackError(Throwable e) {
        if (meView != null) {
            meView.error(e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        meView = null;
    }
}
