package com.xm.zeus.view.home.iview;


import com.xm.zeus.db.app.entity.Colleague;

import java.util.List;

public interface IContactsView {

    void loadContacts(List<Colleague> colleagueList);

    void loadError(Throwable e);

}
