package com.xm.zeus.view.home.presenter;


import com.xm.zeus.db.app.entity.Colleague;
import com.xm.zeus.view.home.interactor.ContactsInteractorImpl;
import com.xm.zeus.view.home.interactor.IContactsInteractor;
import com.xm.zeus.view.home.iview.IContactsView;

import java.util.List;

import rx.Subscriber;

public class ContactsPresenterImpl implements IContactsPresenter {

    private IContactsView contactsView;
    private IContactsInteractor interactor;

    public ContactsPresenterImpl(IContactsView contactsView) {
        this.contactsView = contactsView;
        this.interactor = new ContactsInteractorImpl();
    }

    @Override
    public void getContacts() {
        interactor.loadContacts(new Subscriber<List<Colleague>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (contactsView != null) {
                    contactsView.loadError(e);
                }
            }

            @Override
            public void onNext(List<Colleague> colleagueList) {
                if (contactsView != null) {
                    contactsView.loadContacts(colleagueList);
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        contactsView = null;
    }
}
