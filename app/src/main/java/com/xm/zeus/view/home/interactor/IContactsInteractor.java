package com.xm.zeus.view.home.interactor;

import com.xm.zeus.db.app.entity.Colleague;

import java.util.List;

import rx.Subscriber;

public interface IContactsInteractor {

    void loadContacts(Subscriber<List<Colleague>> subscriber);

}
