package com.xm.zeus.view.contacts.presenter;

import android.content.Context;

import com.xm.zeus.db.app.entity.Friend;
import com.xm.zeus.db.app.helper.UserHelper;
import com.xm.zeus.network.Network;
import com.xm.zeus.network.extend.ApiSubscriber;
import com.xm.zeus.view.contacts.interactor.FriendEditInteractorImpl;
import com.xm.zeus.view.contacts.interactor.IFriendEditInteractor;
import com.xm.zeus.view.contacts.iview.IFriendEditView;

import rx.Subscriber;

/**
 * 作者：小孩子xm on 2016-04-12 15:28
 * 邮箱：1065885952@qq.com
 */
public class FriendEditPresenterImpl implements IFriendEditPresenter {

    private Context context;
    private IFriendEditInteractor interactor;
    private IFriendEditView friendEditView;

    private UserHelper userHelper;

    public FriendEditPresenterImpl(Context context, IFriendEditView friendEditView) {
        this.context = context;
        this.friendEditView = friendEditView;
        interactor = new FriendEditInteractorImpl();
        userHelper = new UserHelper();
    }

    @Override
    public void addFriend(Friend friend) {

        if (friend == null) {
            error("friend is null");
            return;
        }

        if (!Network.isAvailable(context)) {
            error("网络不可用");
            return;
        }

        interactor.addFriend(userHelper.getLastLoggedUser(), friend, new ApiSubscriber<Friend>() {
            @Override
            public void onNext(Friend friend) {
                if (friendEditView != null) {
                    friendEditView.onAddComplete(friend);
                }
            }

            @Override
            protected void onCommonError(Throwable e) {
                error(e.toString());
            }
        });
    }

    @Override
    public void getFriend(String friendId) {
        interactor.getFriend(friendId, new Subscriber<Friend>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                error(e.toString());
            }

            @Override
            public void onNext(Friend friend) {
                if (friendEditView != null) {
                    friendEditView.onGetComplete(friend);
                }
            }
        });
    }

    @Override
    public void updateFriend(Friend friend) {
        interactor.updateFriend(userHelper.getLastLoggedUser(), friend, new ApiSubscriber<Friend>() {
            @Override
            public void onNext(Friend friend) {
                if (friendEditView != null) {
                    friendEditView.onUpdateComplete(friend);
                }
            }

            @Override
            protected void onCommonError(Throwable e) {
                error(e.toString());
            }
        });
    }

    @Override
    public void deleteFriend(String friendId) {
        interactor.deleteFriend(userHelper.getLastLoggedUser(), friendId, new ApiSubscriber<String>() {
            @Override
            public void onNext(String friendId) {
                if (friendEditView != null) {
                    friendEditView.onDeleteComplete(friendId);
                }
            }

            @Override
            protected void onCommonError(Throwable e) {
                error(e.toString());
            }
        });
    }

    private void error(String msg) {
        if (friendEditView != null) {
            friendEditView.error(msg);
        }
    }

    @Override
    public void onDestroy() {
        friendEditView = null;
    }
}
