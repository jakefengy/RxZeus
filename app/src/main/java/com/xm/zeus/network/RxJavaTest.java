package com.xm.zeus.network;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.internal.schedulers.ScheduledAction;
import rx.schedulers.Schedulers;

/**
 * Created by lvxia on 2016-03-29.
 */
public class RxJavaTest {

    // 创建 Observable http://blog.chinaunix.net/uid-20771867-id-5187376.html

    /**
     * Observable.create just from
     */
    private static void create(Subscriber<String> subscriber) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("AAA");
                subscriber.onCompleted();
            }
        }).subscribe(subscriber);

        Observable.just(1, 2, 3);
        Observable.from(new Integer[]{1, 2, 3});
    }

    /**
     * 生成连续数，range start to start + count -1
     * <p>
     * range(2,5)  -> 2,3,4,5,6
     */
    private static void range(Subscriber<Integer> subscriber) {
        Observable.range(2, 5).subscribe(subscriber);
    }

    /**
     * 每次subscribe(订阅)的时候，都会产生新的Observable，区别于Observable.just(input)
     * <p>
     * onNext 1459321021710
     * onCompleted
     * onNext 1459321021717
     * onCompleted
     */
    private static void defer(Subscriber<Long> subscriber) {
        Observable.defer(new Func0<Observable<Long>>() {
            @Override
            public Observable<Long> call() {
                return Observable.just(System.currentTimeMillis());
            }
        }).subscribe(subscriber);
    }

    /**
     * 间隔 从0开始，每隔固定时间，发射一次数据 i++，运行在computation线程中，需到主线程中显示。
     */
    private static void interval(Subscriber<Long> subscriber) {

        Observable.interval(1, 5, TimeUnit.SECONDS)
                .observeOn(Schedulers.immediate())
                .subscribe(subscriber);

    }

    /**
     * 重复执行同一个Observable  只重复onNext执行次数，不会创建新的Observable
     */
    private static void repeat(Subscriber<Integer> subscriber) {
        Observable.just(1).repeat(3).subscribe(subscriber);
    }

    /**
     * 等待m秒后发射一个0，需指定主线程才能显示
     */
    private static void timer(Subscriber<Long> subscriber) {
        Observable.timer(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    // 转换 Observable  http://blog.chinaunix.net/uid-20771867-id-5192193.html

    /**
     * 缓存。缓存count数量的时候，发出一次订阅，skip为跳过数
     */
    private static void buffer(Subscriber<List<Integer>> subscriber) {
        // result： (1,2)，(4,5)，(7,8) / (1,2)，(4,5)，(7)
        Observable.just(1, 2, 3, 4, 5, 6, 7)
                .buffer(2, 3)
                .subscribe(subscriber);
    }

    /**
     * 自定义转换 T -> R (不保证输出顺序)
     */
    private static void flatMap(Subscriber<String> subscriber) {
        Observable.just(1, 2, 3)
                .flatMap(new Func1<Integer, Observable<String>>() {
                    @Override
                    public Observable<String> call(Integer integer) {
                        return Observable.just("flatMap " + integer);
                    }
                })
                .subscribe(subscriber);
    }

    /**
     * 自定义转换 T -> R (保证输出顺序)
     */
    private static void concatmap(Subscriber<String> subscriber) {
        Observable.just(1, 2, 3)
                .concatMap(new Func1<Integer, Observable<String>>() {
                    @Override
                    public Observable<String> call(Integer integer) {
                        return Observable.just("concatMap " + integer);
                    }
                }).subscribe(subscriber);
    }

    // TODO: Observable GroupBy

    /**
     * 直接转换成另一种类型，不产生新的Observable
     */
    private static void map() {
        Observable.just(1, 2, 3)
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return "map " + integer;
                    }
                }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("onNext map " + s);
            }
        });
    }

    /**
     * 对具有继承关系的类进行转换，强制父类转成子类
     */
    private static void cast() {
        Observable.just(1, 2, 3)
                .cast(Object.class)
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        System.out.println("onNext Cast Integer to Object " + o);
                    }
                });
    }

    /**
     * 递归
     */
    private static void scan() {
        Observable.just(1, 2, 3)
                .scan(new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer s, Integer s2) {
                        return s + s2;
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer s) {
                        System.out.println(s);
                    }
                });
    }

    private static void window() {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .window(4)
                .subscribe(new Action1<Observable<Integer>>() {
                    @Override
                    public void call(Observable<Integer> integerObservable) {

                        System.out.println("Group " + integerObservable);

                        integerObservable.subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                System.out.println("Group item " + integer);
                            }
                        });
                    }
                });
    }

    private static void method() {

    }


    public static void main(String[] args) {

        window();
    }


}
