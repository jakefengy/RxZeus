package com.xm.zeus.network;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by lvxia on 2016-03-29.
 */
public class Test {

    private static void filter() {

        Observable.from(new Integer[]{1, 2, 3, 4, 5})
                .doOnNext(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println("First doOnNext (Integer) " + integer);
                    }
                })
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer > 5;
                    }
                })
                .doOnNext(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println("Second doOnNext (Integer) " + integer);
                    }
                })
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return String.valueOf(integer);
                    }
                })
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String string) {
                        System.out.println("Third doOnNext (String) " + string);
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("subscribe.onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("subscribe.onError " + e.toString());
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("subscribe.onNext (String) " + s);
                    }
                });


    }

    public static void main(String[] args) {
        filter();
    }


}
