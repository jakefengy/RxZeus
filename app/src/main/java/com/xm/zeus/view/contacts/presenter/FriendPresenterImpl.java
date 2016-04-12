package com.xm.zeus.view.contacts.presenter;

import android.content.Context;

import com.xm.zeus.db.app.entity.Friend;
import com.xm.zeus.db.app.entity.TimeStamp;
import com.xm.zeus.db.app.entity.User;
import com.xm.zeus.db.app.helper.TimeStampHelper;
import com.xm.zeus.db.app.helper.UserHelper;
import com.xm.zeus.network.Network;
import com.xm.zeus.network.extend.ApiSubscriber;
import com.xm.zeus.view.contacts.interactor.FriendInteractorImpl;
import com.xm.zeus.view.contacts.interactor.IFriendInteractor;
import com.xm.zeus.view.contacts.iview.IFriendView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * 作者：小孩子xm on 2016-04-12 13:25
 * 邮箱：1065885952@qq.com
 */
public class FriendPresenterImpl implements IFriendPresenter {

    private Context context;
    private IFriendView friendView;
    private IFriendInteractor interactor;

    private UserHelper userHelper;
    private TimeStampHelper timeStampHelper;

    private boolean isLoading = false;

    public FriendPresenterImpl(Context context, IFriendView friendView) {
        this.context = context;
        this.friendView = friendView;
        this.interactor = new FriendInteractorImpl();
        userHelper = new UserHelper();
        timeStampHelper = new TimeStampHelper();
    }

    @Override
    public void getFriends() {
        interactor.getFriends(new Subscriber<List<Friend>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                error(e.toString());
            }

            @Override
            public void onNext(List<Friend> friends) {
                if (friendView != null) {
                    friendView.onGetFriendComplete(friends);
                }
            }
        });
    }

    @Override
    public void refreshFriends() {
        if (!Network.isAvailable(context)) {
            if (friendView != null) {
                friendView.error("网络未连接");
                friendView.onRefreshFail();
            }
            return;
        }

        if (isLoading) {
            return;
        }

        isLoading = true;

        User user = userHelper.getLastLoggedUser();
        long friendTS = timeStampHelper.findByKey(TimeStamp.TS_FRIEND, 0);
        interactor.refreshFriends(user, friendTS, new ApiSubscriber<List<Friend>>() {
            @Override
            public void onNext(List<Friend> friends) {
                if (friendView != null) {
                    friendView.onRefreshComplete(friends);
                }
                isLoading = false;
            }

            @Override
            protected void onCommonError(Throwable e) {
                error(e.toString());
                isLoading = false;
                if (friendView != null) {
                    friendView.onRefreshFail();
                }
            }

            @Override
            public void onCompleted() {
                isLoading = false;
            }
        });
    }

    @Override
    public void onDestroy() {
        friendView = null;
    }

    private void error(String msg) {
        if (friendView != null) {
            friendView.error(msg);
        }
    }
}
