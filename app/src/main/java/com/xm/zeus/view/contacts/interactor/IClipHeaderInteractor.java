package com.xm.zeus.view.contacts.interactor;

import com.xm.zeus.db.app.entity.Colleague;
import com.xm.zeus.network.extend.ApiSubscriber;

import java.io.File;

/**
 * Created by lvxia on 2016-04-12.
 */
public interface IClipHeaderInteractor {

    void updateAvatar(File file, ApiSubscriber<Colleague> subscriber);

}
