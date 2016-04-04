package com.xm.zeus.view.login.interactor;

import android.content.Context;
import android.text.TextUtils;

import com.xm.zeus.app.Constant;
import com.xm.zeus.db.app.entity.Colleague;
import com.xm.zeus.db.app.entity.Friend;
import com.xm.zeus.db.app.entity.Group;
import com.xm.zeus.db.app.entity.Org;
import com.xm.zeus.db.app.helper.ColleagueHelper;
import com.xm.zeus.db.app.helper.FriendHelper;
import com.xm.zeus.db.app.helper.GroupHelper;
import com.xm.zeus.db.app.helper.OrgHelper;
import com.xm.zeus.db.user.entity.User;
import com.xm.zeus.db.user.helper.UserHelper;
import com.xm.zeus.network.Network;
import com.xm.zeus.network.extend.ApiSubscriber;
import com.xm.zeus.network.extend.MapFunc1;
import com.xm.zeus.utils.Logger;
import com.xm.zeus.utils.PinYin;

import java.util.List;
import java.util.Locale;

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
    private ColleagueHelper personHelper;
    private FriendHelper friendHelper;
    private OrgHelper orgHelper;
    private GroupHelper groupHelper;

    private PinYin pinYin;

    public SplashInteractorImpl() {
        pinYin = new PinYin();
        userHelper = new UserHelper();
        personHelper = new ColleagueHelper();
        friendHelper = new FriendHelper();
        orgHelper = new OrgHelper();
        groupHelper = new GroupHelper();
    }

    @Override
    public void initGallery() {

        Logger.i(TAG, "SplashInteractorImpl.initGallery.Start");
        Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        subscriber.onNext("complete");
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.io())
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
    public void downloadContacts(User user, long colleagueTS, long friendTS, ApiSubscriber subscriber) {

        Observable<Boolean> obColleague = Network.getZeusApis().getColleague(user.getToken(), user.getUserId(), Constant.Organization, colleagueTS, "false")
                .map(new MapFunc1<List<Colleague>>())
                .map(new Func1<List<Colleague>, Boolean>() {
                    @Override
                    public Boolean call(List<Colleague> colleagues) {
                        return processColleague(colleagues);
                    }
                });

        Observable<Boolean> obFriend = Network.getZeusApis().getFriends(user.getToken(), user.getUserId(), Constant.Organization, friendTS, "false")
                .map(new MapFunc1<List<Friend>>())
                .map(new Func1<List<Friend>, Boolean>() {
                    @Override
                    public Boolean call(List<Friend> friends) {
                        return processFriend(friends);
                    }
                });

        Observable<Boolean> obOrg = Network.getZeusApis().getOrgs(user.getToken(), user.getUserId(), Constant.Organization)
                .map(new MapFunc1<Org>())
                .map(new Func1<Org, Boolean>() {
                    @Override
                    public Boolean call(Org org) {
                        return processOrg(org);
                    }
                });

        Observable<Boolean> obGroup = Network.getZeusApis().getGroup(user.getToken(), user.getUserId(), Constant.Organization)
                .map(new MapFunc1<List<Group>>())
                .map(new Func1<List<Group>, Boolean>() {
                    @Override
                    public Boolean call(List<Group> groups) {
                        return processGroup(groups);
                    }
                });

        Observable.concat(obColleague, obFriend, obGroup, obOrg)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    private boolean processColleague(List<Colleague> colleagues) {

        try {

            if (colleagues == null) {
                return true;
            }

            for (Colleague colleague : colleagues) {

                if (colleague.getType() == 1) {// 删除

                    personHelper.deleteById(colleague.getUid());

                } else { // 新增或修改

                    String selling = pinYin.toPinYin(colleague.getUsername());
                    String firstLetter = selling.substring(0, 1).toUpperCase(Locale.getDefault());
                    colleague.setSpelling(selling);
                    colleague.setFirstletter(firstLetter);
                    colleague.setTimestamp(System.currentTimeMillis());
                    colleague.setHeadName("同事");
                    colleague.setIsCheck(false);
                    colleague.setDataType(Colleague.DATATYPE_COLLEAGUE);

                    personHelper.saveOrUpdate(colleague);
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    private boolean processFriend(List<Friend> friends) {

        try {

            if (friends == null) {
                return true;
            }

            for (Friend friend : friends) {

                if (friend.getType() == 1) {
                    friendHelper.deleteById(friend.getUid());
                } else {
                    String selling = pinYin.toPinYin(friend.getUsername());
                    String firstLetter = selling.substring(0, 1).toUpperCase(Locale.getDefault());
                    friend.setSpelling(selling);
                    friend.setFirstLetter(firstLetter);
                    friend.setTimestamp(System.currentTimeMillis());
                    friend.setHeadName("名片夹");
                    friend.setDataType(Friend.DATATYPE_FRIEND);
                    friendHelper.saveOrUpdate(friend);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean processOrg(Org org) {
        try {
            if (org == null) {
                return true;
            }

            if (TextUtils.isEmpty(org.getPid())) {
                org.setPid(Org.TopLevel);
            }

            orgHelper.add(org);
            if (org.getChildren() != null && org.getChildren().size() > 0) {
                for (Org child : org.getChildren())
                    processOrg(child);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean processGroup(List<Group> groups) {
        try {

            if (groups != null) {
                return true;
            }

            for (Group group : groups) {
                group.setDataType(Group.DATATYPE_GROUP);
                group.setHeadName("讨论组");
                group.setTimestamp(System.currentTimeMillis());
            }
            if (groups.size() > 0) {
                groupHelper.addGroups(groups);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
