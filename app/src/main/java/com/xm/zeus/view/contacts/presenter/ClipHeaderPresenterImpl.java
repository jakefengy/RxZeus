package com.xm.zeus.view.contacts.presenter;

import android.content.Context;

import com.xm.zeus.db.app.entity.Colleague;
import com.xm.zeus.network.Network;
import com.xm.zeus.network.extend.ApiSubscriber;
import com.xm.zeus.utils.RxBus;
import com.xm.zeus.utils.entity.rxbus.ColleagueChange;
import com.xm.zeus.view.contacts.interactor.ClipHeaderInteractorImpl;
import com.xm.zeus.view.contacts.interactor.IClipHeaderInteractor;
import com.xm.zeus.view.contacts.iview.IClipHeaderView;

import java.io.File;

/**
 * Created by lvxia on 2016-04-12.
 */
public class ClipHeaderPresenterImpl implements IClipHeaderPresenter {

    private Context context;
    private IClipHeaderInteractor interactor;
    private IClipHeaderView headerView;

    public ClipHeaderPresenterImpl(IClipHeaderView headerView, Context context) {
        this.headerView = headerView;
        this.context = context;
        this.interactor = new ClipHeaderInteractorImpl();
    }

    @Override
    public void updateAvatar(File file) {
        if (file == null && !file.exists()) {
            error("头像文件不存在");
            return;
        }

        if (!Network.isAvailable(context)) {
            error("网络不可用");
            return;
        }

        if (headerView != null) {
            headerView.onUpdateStart();
        }

        interactor.updateAvatar(file, new ApiSubscriber<Colleague>() {
            @Override
            public void onNext(Colleague colleague) {
                ColleagueChange colleagueChange = new ColleagueChange();
                colleagueChange.setColleague(colleague);
                RxBus.getInstance().post(colleagueChange);

                if (headerView != null) {
                    headerView.onComplete();
                }

            }

            @Override
            protected void onCommonError(Throwable e) {
                error(e.getMessage());
            }
        });
    }

    private void error(String msg) {
        if (headerView != null) {
            headerView.onError(msg);
        }
    }

    @Override
    public void onDestroy() {
        headerView = null;
    }
}
