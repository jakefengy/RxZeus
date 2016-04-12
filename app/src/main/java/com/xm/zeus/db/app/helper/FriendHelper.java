package com.xm.zeus.db.app.helper;

import android.text.TextUtils;

import com.xm.zeus.db.AppDbHelper;
import com.xm.zeus.db.app.dao.FriendDao;
import com.xm.zeus.db.app.entity.Friend;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * 好友数据库操作
 */
public class FriendHelper {

    private FriendDao friendDao;

    public FriendHelper() {
        friendDao = AppDbHelper.getInstance().getAppDaoSession().getFriendDao();
    }

    public void saveOrUpdate(Friend card) {
        if (card == null) {
            return;
        }

        Friend find = friendDao.load(card.getUid());
        if (find == null) {
            friendDao.insert(card);
        } else {
            friendDao.update(card);
        }


    }

    public void add(Friend card) {
        if (card == null) {
            return;
        }

        friendDao.insert(card);
    }

    public void update(Friend card) {
        if (card == null) {
            return;
        }

        friendDao.update(card);
    }

    public void deleteById(String id) {
        if (TextUtils.isEmpty(id)) {
            return;
        }

        friendDao.deleteByKey(id);
    }

    public Friend findById(String id) {
        if (TextUtils.isEmpty(id)) {
            return null;
        }

        return friendDao.load(id);
    }

    public List<Friend> findAll() {
        List<Friend> results = new ArrayList<>();
        List<Friend> find = friendDao.queryBuilder().orderAsc(FriendDao.Properties.Spelling).list();
        if (find != null && find.size() > 0) {
            results.addAll(find);
        }

        return results;
    }

}
