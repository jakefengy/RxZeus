package com.xm.zeus.db.app.helper;

import android.text.TextUtils;

import com.xm.zeus.db.AppDbHelper;
import com.xm.zeus.db.app.dao.TimeStampDao;
import com.xm.zeus.db.app.entity.TimeStamp;

import java.util.List;

/**
 * Created by lvxia on 2016-03-29.
 */
public class TimeStampHelper {

    private TimeStampDao timeStampDao;

    public TimeStampHelper() {
        timeStampDao = AppDbHelper.getInstance().getAppDaoSession().getTimeStampDao();
    }

    /**
     * 新增
     *
     * @param key
     * @param timeStamp
     */
    public void saveOrUpdate(String key, long timeStamp) {

        if (TextUtils.isEmpty(key)) {
            return;
        }

        TimeStamp find = timeStampDao.load(key);
        if (find != null) {
            timeStampDao.update(new TimeStamp(key, timeStamp));
        } else {
            timeStampDao.insert(new TimeStamp(key, timeStamp));
        }

    }

    public void update(String key, long timeStamp) {

        if (TextUtils.isEmpty(key)) {
            return;
        }

        timeStampDao.update(new TimeStamp(key, timeStamp));

    }

    /**
     * 查询
     *
     * @param key
     * @param defTimeStamp
     * @return
     */
    public long findByKey(String key, long defTimeStamp) {
        if (TextUtils.isEmpty(key)) {
            return defTimeStamp;
        }

        TimeStamp timeStamp = timeStampDao.load(key);
        if (timeStamp != null) {
            return timeStamp.getTimeStamp();
        }

        return defTimeStamp;
    }

    /**
     * 查询全部
     *
     * @return
     */
    public List<TimeStamp> findAll() {
        return timeStampDao.loadAll();
    }

    /**
     * 删除
     *
     * @param key
     */
    public void deleteByKey(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }

        timeStampDao.deleteByKey(key);
    }

}
