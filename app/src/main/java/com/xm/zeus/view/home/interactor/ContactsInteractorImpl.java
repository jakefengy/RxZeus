package com.xm.zeus.view.home.interactor;

import com.xm.zeus.db.app.entity.Colleague;
import com.xm.zeus.db.app.helper.ColleagueHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ContactsInteractorImpl implements IContactsInteractor {

    private ColleagueHelper colleagueHelper;

    public ContactsInteractorImpl() {
        colleagueHelper = new ColleagueHelper();
    }

    @Override
    public void loadContacts(Subscriber<List<Colleague>> subscriber) {
        Observable.create(new Observable.OnSubscribe<List<Colleague>>() {
            @Override
            public void call(Subscriber<? super List<Colleague>> subscriber) {

                try {

                    List<Colleague> results = getContacts();
                    if (results.size() == 0) {
                        subscriber.onError(new Throwable("通讯录是空的"));
                    } else {
                        subscriber.onNext(getContacts());
                        subscriber.onCompleted();
                    }
                } catch (Exception e) {
                    subscriber.onError(e);
                }

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    private List<Colleague> getContacts() {
        List<Colleague> colleagueList = new ArrayList<>();
        colleagueList.addAll(colleagueHelper.findAll());
        return colleagueList;
    }


}
