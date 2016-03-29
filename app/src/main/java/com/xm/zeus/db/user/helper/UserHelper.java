package com.xm.zeus.db.user.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.xm.zeus.db.user.dao.DaoMaster;
import com.xm.zeus.db.user.dao.DaoSession;
import com.xm.zeus.db.user.dao.UserDao;
import com.xm.zeus.db.user.entity.User;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by lvxia on 2016-03-28.
 */
public class UserHelper {

    private final static String USER_DB_NAME = "UserDB";

    private UserDao userDao;

    public UserHelper(Context appContext) {
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(appContext, USER_DB_NAME, null);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        userDao = new DaoMaster(db).newSession().getUserDao();
    }

    public List<User> getUsers() {

        List<User> users = new ArrayList<>();

        QueryBuilder qb = userDao.queryBuilder();
        qb.orderDesc(UserDao.Properties.LoggedDate);

        users.addAll(qb.list());

        return users;

    }

    public User getLastLoggedUser() {
        List<User> users = getUsers();
        if (users.size() > 0) {
            return users.get(0);
        }
        return null;

    }

    public void saveUser(User user) {
        if (user == null) {
            return;
        }
        user.setLoggedDate(System.currentTimeMillis());
        userDao.insertOrReplace(user);
    }

    public void updateUser(String userId) {

        if (TextUtils.isEmpty(userId)) {
            return;
        }

        User user = userDao.load(userId);
        if (user != null) {
            user.setLoggedDate(System.currentTimeMillis());
            userDao.insertOrReplace(user);
        }
    }

    public void deleteUser(String userId) {
        if (TextUtils.isEmpty(userId)) {
            return;
        }

        userDao.deleteByKey(userId);
    }

    public void release() {
        userDao = null;
    }

}
