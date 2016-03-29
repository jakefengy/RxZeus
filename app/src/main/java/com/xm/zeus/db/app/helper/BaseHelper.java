package com.xm.zeus.db.app.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xm.zeus.db.app.dao.DaoMaster;
import com.xm.zeus.db.app.dao.DaoSession;

/**
 * Created by lvxia on 2016-03-29.
 */
public class BaseHelper {

    protected final static String APP_DB_NAME = "ZeusDB";
    protected DaoSession daoSession;

    public BaseHelper(Context appContext) {

        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(appContext, APP_DB_NAME, null);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        daoSession = new DaoMaster(db).newSession();

    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
