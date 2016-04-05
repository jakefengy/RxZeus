package com.xm.zeus.view.home.iview;

import com.xm.zeus.db.app.entity.Colleague;
import com.xm.zeus.view.home.entity.CheckVersionResult;

/**
 * Created by lvxia on 2016-04-05.
 */
public interface IMeView {

    void setUserInfo(Colleague userInfo);

    void updateApp(CheckVersionResult newVersion);

    void signedOutComplete();

    void signedOutFail();

    void error(String error);

}
