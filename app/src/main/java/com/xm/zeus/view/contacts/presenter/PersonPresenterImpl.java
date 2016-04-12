package com.xm.zeus.view.contacts.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.xm.zeus.db.app.entity.Colleague;
import com.xm.zeus.db.app.entity.Friend;
import com.xm.zeus.db.app.entity.User;
import com.xm.zeus.db.app.helper.UserHelper;
import com.xm.zeus.network.Network;
import com.xm.zeus.network.extend.ApiSubscriber;
import com.xm.zeus.view.contacts.interactor.IPersonInteractor;
import com.xm.zeus.view.contacts.interactor.PersonInteractorImpl;
import com.xm.zeus.view.contacts.iview.IPersonView;

import rx.Subscriber;

/**
 * 作者：小孩子xm on 2016-04-12 21:16
 * 邮箱：1065885952@qq.com
 */
public class PersonPresenterImpl implements IPersonPresenter {

    private Context context;
    private IPersonInteractor interactor;
    private IPersonView personView;

    private UserHelper userHelper;

    public PersonPresenterImpl(Context context, IPersonView personView) {
        this.context = context;
        this.personView = personView;
        this.interactor = new PersonInteractorImpl();
        this.userHelper = new UserHelper();
    }

    @Override
    public void getFriend(String friendId) {
        interactor.getFriend(friendId, new Subscriber<Friend>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                error(e.getMessage());
            }

            @Override
            public void onNext(Friend friend) {
                if (personView != null) {
                    personView.onGetFriendComplete(friend);
                }
            }
        });
    }

    @Override
    public void deleteFriend(String friendId) {
        if (TextUtils.isEmpty(friendId)) {
            error("friendId is null");
            return;
        }

        if (!Network.isAvailable(context)) {
            error("网络不可用");
            return;
        }

        interactor.deleteFriend(userHelper.getLastLoggedUser(), friendId, new ApiSubscriber<String>() {
            @Override
            public void onNext(String s) {
                if (personView != null) {
                    personView.onDeleteFriendComplete();
                }
            }

            @Override
            protected void onCommonError(Throwable e) {
                error(e.getMessage());
            }
        });
    }

    @Override
    public void getColleague(String colleagueId) {
        interactor.getColleague(colleagueId, new Subscriber<Colleague>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                error(e.getMessage());
            }

            @Override
            public void onNext(Colleague colleague) {
                if (personView != null) {
                    personView.onGetColleagueComplete(colleague);
                }
            }
        });
    }

    private void error(String msg) {
        if (personView != null) {
            personView.onError(msg);
        }
    }

    @Override
    public void onDestroy() {
        personView = null;
    }
}
