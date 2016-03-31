package com.xm.zeus.view.login.interactor;

import android.content.Context;
import android.text.TextUtils;

import com.xm.zeus.Constant;
import com.xm.zeus.db.app.entity.Colleague;
import com.xm.zeus.db.app.entity.Friend;
import com.xm.zeus.db.app.entity.Group;
import com.xm.zeus.db.app.entity.Org;
import com.xm.zeus.db.app.helper.ColleagueHelper;
import com.xm.zeus.db.app.helper.FriendHelper;
import com.xm.zeus.db.app.helper.OrgHelper;
import com.xm.zeus.db.user.entity.User;
import com.xm.zeus.db.user.helper.UserHelper;
import com.xm.zeus.network.Network;
import com.xm.zeus.network.extend.CancelSubscriber;
import com.xm.zeus.network.extend.MapFunc1;
import com.xm.zeus.utils.Logger;
import com.xm.zeus.utils.PinYin;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func4;
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

    private PinYin pinYin;

    public SplashInteractorImpl() {
        pinYin = new PinYin();
    }

    @Override
    public void initAppDB(Context context) {
        Logger.i(TAG, "SplashInteractorImpl.initAppDB");
        userHelper = new UserHelper(context);
        personHelper = new ColleagueHelper(context);
        friendHelper = new FriendHelper(context);
        orgHelper = new OrgHelper(context);
    }

    @Override
    public void initGallery() {

        Logger.i(TAG, "SplashInteractorImpl.initGallery.Start");
        Observable
                .create(new Observable.OnSubscribe<Object>() {
                    @Override
                    public void call(Subscriber<? super Object> subscriber) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
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
    public void downloadContacts(User user, long timestamp, CancelSubscriber subscriber) {

        Observable<Colleague> obColleague = Network.getZeusApis().getColleague(user.getToken(), user.getUserId(), Constant.Organization, timestamp, "false")
                .map(new MapFunc1<List<Colleague>>())
                .flatMap(new Func1<List<Colleague>, Observable<Colleague>>() {
                    @Override
                    public Observable<Colleague> call(List<Colleague> colleagues) {
                        return Observable.from(colleagues);
                    }
                });

        Observable<Friend> obFriend = Network.getZeusApis().getFriends(user.getToken(), user.getUserId(), Constant.Organization, timestamp, "false")
                .map(new MapFunc1<List<Friend>>())
                .flatMap(new Func1<List<Friend>, Observable<Friend>>() {
                    @Override
                    public Observable<Friend> call(List<Friend> friends) {
                        return Observable.from(friends);
                    }
                });

        Observable<Org> obOrg = Network.getZeusApis().getOrgs(user.getToken(), user.getUserId(), Constant.Organization)
                .map(new MapFunc1<Org>());

        Observable<Group> obGroup = Network.getZeusApis().getGroup(user.getToken(), user.getUserId(), Constant.Organization)
                .map(new MapFunc1<List<Group>>())
                .flatMap(new Func1<List<Group>, Observable<Group>>() {
                    @Override
                    public Observable<Group> call(List<Group> groups) {
                        return Observable.from(groups);
                    }
                });

        Observable.zip(obColleague, obFriend, obOrg, obGroup, new Func4<Colleague, Friend, Org, Group, Boolean>() {
            @Override
            public Boolean call(Colleague colleague, Friend friend, Org org, Group group) {
                return null;
            }
        });


    }

    private void processColleague(Colleague colleague) {

        try {

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

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void processFriend(Friend friend) {

        try {
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
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }

    }

    private void processOrg(Org org) {
        try {
            if (org == null) {
                return;
            }

            if (TextUtils.isEmpty(org.getPid())) {
                org.setPid(Org.TopLevel);
            }

            orgHelper.add(org);
            if (org.getChildren() != null && org.getChildren().size() > 0) {
                for (Org child : org.getChildren())
                    processOrg(child);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
