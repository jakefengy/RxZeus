package com.xm.zeus.db.app.helper;

import com.xm.zeus.db.AppDbHelper;
import com.xm.zeus.db.app.dao.SettingDao;
import com.xm.zeus.db.app.entity.Setting;

/**
 * Created by lvxia on 2016-04-05.
 */
public class SettingHelper {

    public enum Keys {
        AppUpgrade {
            public String getCode() {
                return "app_upgrade";
            }

            public String getName() {
                return "应用升级";
            }
        };

        public abstract String getCode();

        public abstract String getName();

    }

    private SettingDao settingDao;

    public SettingHelper() {
        settingDao = AppDbHelper.getInstance().getAppDaoSession().getSettingDao();
    }

    public void insertOrUpdate(Setting setting) {
        if (setting == null) {
            return;
        }

        settingDao.insertOrReplace(setting);

    }

    public Setting getByKey(Keys key) {
        return settingDao.load(key.getCode());
    }

    public void deleteByKey(Keys key) {
        settingDao.deleteByKey(key.getCode());
    }


}
