package com.xm.zeus.network;

import com.xm.zeus.db.user.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Notification;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
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

    // 过滤 Observable http://blog.chinaunix.net/uid-20771867-id-5194384.html

    // 去重，只要重复就都去掉
    private static void distinct() {
        Observable.just(1, 2, 3, 4, 1, 2, 3, 4).distinct()
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println(integer);
                    }
                });
    }

    // 去重，n == n+1  remove n+1
    private static void distinctUntilChanged() {
        Observable.just(1, 2, 3, 3, 1, 2, 3, 4, 4, 5).distinctUntilChanged()
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println(integer);
                    }
                });
    }

    // 得到index元素
    private static void elementAt() {
        Observable.just(1, 2, 3, 4, 5).elementAt(2)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println("position 2 = " + integer);
                    }
                });
    }

    // 过滤获得符合条件的数据
    private static void filter() {
        Observable.just(1, 2, 3, 4, 5)
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer % 2 == 0;
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println(integer);
                    }
                });
    }

    // 返回满足条件的第一个元素
    private static void first() {
        Observable.just(1, 2, 3, 4, 5)
                .first(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer > 1;
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext " + integer);
                    }
                });
    }

    // 返回满足条件的第一个元素，不会输出符合条件之前的元素 如输出｛1,2,3,4,5｝中大于1的元素为 2
    private static void block() {
        Observable.just(1, 2, 3, 4, 5)
                .first(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer > 1;
                    }
                })
                .toBlocking()
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext " + integer);
                    }
                });
    }

    // 跳过n取m个元素
    private static void skipAndTake() {

        Observable.just(1, 2, 3, 4, 5).skip(2).take(2)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println(integer);
                    }
                });

    }

    // Sample操作符会定时地发射源Observable最近发射的数据，其他的都会被过滤掉
    private static void sample() {
        Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        try {
                            for (int i = 1; i <= 20; i++) {
                                Thread.sleep(200);
                                subscriber.onNext(i);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .sample(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println("Sample " + integer);
                    }
                });
    }

    // ThrottleLast操作符会定时地发射源Observable最近发射的数据，其他的都会被过滤掉
    private static void throttleLast() {
        Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        try {
                            for (int i = 1; i <= 20; i++) {
                                Thread.sleep(200);
                                subscriber.onNext(i);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .throttleLast(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println("throttleLast " + integer);
                    }
                });
    }

    // ThrottleFirst操作符则会定期发射这个时间段里源Observable发射的第一个数据
    private static void throttleFirst() {
        Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        try {
                            for (int i = 1; i <= 20; i++) {
                                Thread.sleep(200);
                                subscriber.onNext(i);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println("throttleFirst " + integer);
                    }
                });
    }

    // 合并 Observable http://blog.chinaunix.net/uid-20771867-id-5197584.html

    /**
     * 将多个Observable合并
     */
    private static void merge() {
        Observable<User> ob2 = Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(Subscriber<? super User> subscriber) {

                User user = new User();
                user.setUserId("111");
                subscriber.onNext(user);
                user.setUserId("222");
                subscriber.onNext(user);

            }
        });
        Observable<Integer> ob1 = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 1; i < 5; i++) {
                    if (i % 2 == 0) {
                        subscriber.onNext(i);
                    } else {
                        subscriber.onError(new Throwable(i + "不是偶数"));
                    }
                }
            }
        });

        System.out.println("----------Observable.merge");

        Observable.merge(ob1, ob2)
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError " + e.toString());
                    }

                    @Override
                    public void onNext(Object o) {
                        if (o instanceof User) {
                            System.out.println("User " + ((User) o).getUserId());
                        } else {
                            System.out.println(o);
                        }
                    }
                });

        System.out.println("----------Observable.mergeDelayError");
        Observable.mergeDelayError(ob1, ob2)
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError " + e.toString());
                    }

                    @Override
                    public void onNext(Object o) {
                        if (o instanceof User) {
                            System.out.println("User " + ((User) o).getUserId());
                        } else {
                            System.out.println(o);
                        }
                    }
                });

        System.out.println("----------Observable.concat");
        Observable.concat(ob1, ob2)
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError " + e.toString());
                    }

                    @Override
                    public void onNext(Object o) {
                        if (o instanceof User) {
                            System.out.println("User " + ((User) o).getUserId());
                        } else {
                            System.out.println(o);
                        }
                    }
                });

    }

    private static void startWith() {
        Observable.just(1, 2, 3).startWith(-1, 0)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println(integer);
                    }
                });
    }

    private static void zip() {
        Observable<Integer> ob1 = Observable.just(1, 2, 3);
        Observable<String> ob2 = Observable.just("A", "B", "C", "D");

        Observable.zip(ob1, ob2, new Func2<Integer, String, String>() {
            @Override
            public String call(Integer integer, String s) {
                return "ob1." + integer + " , ob2." + s;
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println(s);
            }
        });
    }

    private static void zipWith() {
        Observable<Integer> ob1 = Observable.just(1, 2, 3);
        Observable<String> ob2 = Observable.just("A", "B", "C", "D");

        ob1.zipWith(ob2, new Func2<Integer, String, String>() {
            @Override
            public String call(Integer integer, String s) {
                return "ob1." + integer + " , ob2." + s;
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println(s);
            }
        });
    }

    // 异常处理 Error Handling http://blog.chinaunix.net/uid-20771867-id-5201914.html

    private static void onErrorReturn() {
        Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        for (int i = 0; i < 2; i++) {
                            if (i > 0) {
                                subscriber.onError(new Throwable("i > 0 Error"));
                            } else {
                                subscriber.onNext(i);
                            }
                        }
                    }
                })
                .onErrorReturn(new Func1<Throwable, Integer>() {
                    @Override
                    public Integer call(Throwable throwable) {
                        return 100;
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError " + e.toString());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println(integer);
                    }
                });

    }

    private static void onErrorResumeNext() {
        Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        for (int i = 0; i < 2; i++) {
                            if (i > 0) {
                                subscriber.onError(new Throwable("i > 0 Error"));
                            } else {
                                subscriber.onNext(i);
                            }
                        }
                    }
                })
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Integer>>() {
                    @Override
                    public Observable<? extends Integer> call(Throwable throwable) {
                        return Observable.just(100);
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError " + e.toString());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println(integer);
                    }
                });
    }

    private static void onExceptionResumeNext() {
        Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        for (int i = 0; i < 2; i++) {
                            if (i > 0) {
                                // 如果Throw Exception 则触发onExceptionResumeNext里面的Observable，否则触发Subscriber.onError

//                                subscriber.onError(new Throwable(i + " > 0 Error")); // Subscriber.onError

                                subscriber.onError(new NullPointerException(i + " > 0 Error")); // Observable.onExceptionResumeNext
                            } else {
                                subscriber.onNext(i);
                            }
                        }
                    }
                })
                .onExceptionResumeNext(Observable.just(100))
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError " + e.toString());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println(integer);
                    }
                });
    }

    private static void retry() {
        Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        for (int i = 0; i < 3; i++) {
                            if (i == 2) {
                                subscriber.onError(new Throwable("Error i == 2"));
                            } else {
                                subscriber.onNext(i);
                            }
                        }
                    }
                })
                .retry(2)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError " + e.toString());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println(integer);
                    }
                });
    }

    private static void retryWhen() {
        Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        for (int i = 0; i < 3; i++) {
                            if (i == 2) {
                                subscriber.onError(new Throwable("Error i == 2"));
                            } else {
                                subscriber.onNext(i);
                            }
                        }
                    }
                })
                .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Throwable> observable) {
                        return observable.zipWith(Observable.just(1, 2, 3), new Func2<Throwable, Integer, Integer>() {
                            @Override
                            public Integer call(Throwable throwable, Integer integer) {
                                return 100;
                            }
                        });
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError " + e.toString());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println(integer);
                    }
                });
    }

    // 其他工具类的操作 http://blog.chinaunix.net/uid-20771867-id-5206187.html

    private static void delay() {

        Observable.just(1, 2, 3)
                .delay(2, TimeUnit.SECONDS)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError " + e.toString());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println(integer);
                    }
                });

    }

    private static void delaySubscription() {

        Observable.just(1, 2, 3)
                .delaySubscription(2, TimeUnit.SECONDS)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError " + e.toString());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println(integer);
                    }
                });

    }

    private static void dooComplete() {
        Observable.just(1, 2, 3)
                .doOnEach(new Action1<Notification<? super Integer>>() {
                    @Override
                    public void call(Notification<? super Integer> notification) {
                        System.out.println("doOnEach kind = " + notification.getKind() + " ' value = " + notification.getValue());
                    }
                })
                .doOnNext(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println("doOnNext " + integer);
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        System.out.println("doOnCompleted");
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        System.out.println("doOnSubscribe");
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        System.out.println("doOnUnsubscribe");
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        System.out.println("doOnTerminate");
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError " + e.toString());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println(integer);
                    }
                });
    }

    private static void dooError() {
        Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        for (int i = 0; i < 3; i++) {
                            if (i == 2) {
                                subscriber.onError(new Throwable("Error i == 2"));
                            } else {
                                subscriber.onNext(i);
                            }
                        }
                    }
                })
                .doOnEach(new Action1<Notification<? super Integer>>() {
                    @Override
                    public void call(Notification<? super Integer> notification) {
                        System.out.println("doOnEach kind = " + notification.getKind() + " ' value = " + notification.getValue());
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        System.out.println("doOnError " + throwable.toString());
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        System.out.println("doOnTerminate");
                    }
                })
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        System.out.println("doAfterTerminate");
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError " + e.toString());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println(integer);
                    }
                });

    }

    private static void timeout() {
        Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        for (int i = 0; i <= 3; i++) {
                            try {
                                Thread.sleep(i * 100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            subscriber.onNext(i);
                        }
                        subscriber.onCompleted();
                    }
                })
                .timeout(200, TimeUnit.MILLISECONDS, Observable.just(8, 9))
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError " + e.toString());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println(integer);
                    }
                });
    }

    // 条件 Conditional and Boolean http://blog.chinaunix.net/uid-20771867-id-5208237.html

    private static void all() {
        // 集合中所有元素同时满足某个条件的时候 返回true，否则false 相当于&&
        Observable.just(1, 2, 3).all(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer < 3;
            }
        }).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                System.out.println("onNext " + aBoolean);
            }
        });
    }

    private static void amb() {
        Observable<Integer> ob1 = Observable.just(1, 2, 3).delay(3, TimeUnit.SECONDS);
        Observable<Integer> ob2 = Observable.just(4, 5, 6).delay(2, TimeUnit.SECONDS);
        Observable<Integer> ob3 = Observable.just(7, 8, 9).delay(1, TimeUnit.SECONDS);

        Observable.amb(ob1, ob2, ob3)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println("onNext " + integer);
                    }
                });

    }

    private static void contains() {
        Observable.just(1, 2, 3).contains(2)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean integer) {
                        System.out.println("onNext " + integer);
                    }
                });
    }

    private static void isEmpty() {
        Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        subscriber.onCompleted();
                    }
                }).isEmpty()
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError " + e.toString());
                    }

                    @Override
                    public void onNext(Boolean integer) {
                        System.out.println(integer);
                    }
                });
    }

    private static void defaultEmpty() {
        Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        subscriber.onCompleted();
                    }
                }).defaultIfEmpty(100)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError " + e.toString());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println(integer);
                    }
                });
    }

    private static void sequenceEqual() {
        // 判断是否为完全相同的序列
        Observable<Integer> ob1 = Observable.just(1, 2, 3);
        Observable<Integer> ob2 = Observable.just(1, 2, 3);

        Observable.sequenceEqual(ob1, ob2)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        System.out.println("onNext " + aBoolean);
                    }
                });

    }

    private static void skipWhile() {
        Observable.just(1, 2, 3, 4, 5)
                .skipWhile(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer <= 3;
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println("onNext " + integer);
                    }
                });
    }

    private static void skipUntil() {
        Observable.just(1, 2, 3, 4, 5)
                .skipUntil(Observable.just(1, 2, 3))
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println("onNext " + integer);
                    }
                });
    }

    private static void takeWhile() {
        Observable.just(1, 2, 3, 4, 5)
                .takeWhile(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer <= 3;
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println("onNext " + integer);
                    }
                });
    }

    private static void takeUntil() {
        Observable.just(1, 2, 3, 4, 5)
                .takeUntil(Observable.just(1, 2, 3))
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println("onNext " + integer);
                    }
                });
    }

    // 聚合操作符 Aggregate

    private static void concat() {

        // 合并多个Observable，并严格按照顺序执行，区别于merge

        Observable<Integer> ob1 = Observable.just(1, 2, 3);
        Observable<Integer> ob2 = Observable.just(7, 8, 9);

        Observable.concat(ob1, ob2).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("onNext " + integer);
            }
        });

    }

    private static void count() {

        // 计算Observable一共发射出多少次onNext，若中途出错则返回onError

        Observable.just(1, 2, 3).count().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("Observable.just.count = " + integer);
            }
        });
    }

    private static void reduce() {

        // 类似scan，Reduce操作符应用一个函数接收Observable发射的数据和函数的计算结果作为下次计算的参数，输出最后的结果，但不输出过程

        Observable.just(1, 2, 3).reduce(new Func2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("Observable.just.reduce = " + integer);
            }
        });

    }

    private static void collect() {

        // 将元素收集到一个集合中。
        Observable.just(1, 2, 3).collect(new Func0<List<Integer>>() {
            @Override
            public List<Integer> call() {
                return new ArrayList<>();
            }
        }, new Action2<List<Integer>, Integer>() {
            @Override
            public void call(List<Integer> integers, Integer integer) {
                integers.add(integer);
            }
        }).subscribe(new Action1<List<Integer>>() {
            @Override
            public void call(List<Integer> integers) {
                System.out.println("Observable.just.reduce = " + integers);
            }
        });

    }

    public static void main(String[] args) {

        collect();
    }


}
