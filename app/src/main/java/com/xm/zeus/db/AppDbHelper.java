package com.xm.zeus.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xm.zeus.db.app.dao.DaoMaster;
import com.xm.zeus.db.app.dao.DaoSession;

/**
 * Created by lvxia on 2016-03-29.
 */
public class AppDbHelper {

    protected final static String APP_DB_NAME = "ZeusDB.db";
    private Context appContext;
    private DaoSession daoSession;

    private AppDbHelper() {

    }

    private static class SingletonHolder {
        private static final AppDbHelper INSTANCE = new AppDbHelper();
    }

    //获取单例
    public static AppDbHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init(Context context) {
        this.appContext = context;
    }

    public DaoSession getAppDaoSession() {
        if (daoSession == null) {
            createDaoSession();
        }
        return daoSession;
    }

    private void createDaoSession() {
        if (appContext == null) {
            throw new NullPointerException("GreenDao.appContext == null , call init(Context) first .");
        }

        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(appContext, APP_DB_NAME, null);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        daoSession = new DaoMaster(db).newSession();

    }

    public void resetDB() {
        daoSession = null;
    }
}
