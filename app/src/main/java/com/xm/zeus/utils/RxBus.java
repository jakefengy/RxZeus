package com.xm.zeus.utils;


import com.xm.zeus.db.app.entity.Friend;
import com.xm.zeus.db.user.entity.User;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * RxBus
 */
public class RxBus {

    public final static String ACTION_TOKEN_ERROR = "ACTION_TOKEN_ERROR";

    private static class SingletonHolder {
        private static final RxBus INSTANCE = new RxBus();
    }

    //获取单例
    public static RxBus getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private final Subject bus;

    private RxBus() {
        bus = new SerializedSubject<>(PublishSubject.create());
    }

    // 发射一个事件
    public void post(Object o) {
        bus.onNext(o);
    }

    public <T> Observable<T> toObservable(final Class<T> eventType) {
        return bus.ofType(eventType);
    }

    // eg
    public static void main(String[] args) {

        // 先订阅，才能接收到消息
        RxBus.getInstance().toObservable(User.class)
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        System.out.println(user.getUserId());
                    }
                });

        // 发送
        User user = new User();
        user.setUserId("AAA");
        RxBus.getInstance().post(user);

        Friend friend = new Friend();
        friend.setUid("Friend");
        RxBus.getInstance().post(friend);

    }

}
