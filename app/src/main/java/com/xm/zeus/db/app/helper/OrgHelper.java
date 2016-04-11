package com.xm.zeus.db.app.helper;

import android.text.TextUtils;

import com.xm.zeus.db.AppDbHelper;
import com.xm.zeus.db.app.dao.OrgDao;
import com.xm.zeus.db.app.entity.Org;

import java.util.List;


public class OrgHelper {

    private OrgDao orgDao;

    public OrgHelper() {
        orgDao = AppDbHelper.getInstance().getAppDaoSession().getOrgDao();
    }

    public void add(Org org) {
        if (org == null) {
            return;
        }

        orgDao.insertOrReplace(org);
    }

    /**
     * 修改
     *
     * @param org
     */
    public void update(Org org) {
        if (org == null) {
            return;
        }

        orgDao.insertOrReplace(org);
    }

    /**
     * 查找
     *
     * @param id
     * @return
     */
    public Org findById(String id) {
        if (TextUtils.isEmpty(id)) {
            return null;
        }

        return orgDao.load(id);
    }

    /**
     * 根据父节点ID查找第一个组织
     *
     * @param pid
     * @return
     */
    public Org findFirstByPid(String pid) {
        if (TextUtils.isEmpty(pid)) {
            return null;
        }

        List<Org> orgs = findByPid(pid);
        if (orgs != null && orgs.size() > 0) {
            return orgs.get(0);
        }

        return null;

    }

    /**
     * 根据父节点id查询
     *
     * @param pid
     * @return
     */
    public List<Org> findByPid(String pid) {
        if (TextUtils.isEmpty(pid)) {
            return null;
        }

        return orgDao.queryBuilder().where(OrgDao.Properties.Pid.eq(pid)).list();
    }

    /**
     * 查询全部
     *
     * @return
     */
    public List<Org> findAll() {
        return orgDao.loadAll();
    }


    /**
     * 根据id查询
     *
     * @param ids
     * @return
     */
    public List<Org> findByIds(List<String> ids) {
        if (ids == null || ids.size() == 0) {
            return null;
        }

        return orgDao.queryBuilder().where(OrgDao.Properties.Uid.in(ids)).list();
    }

}
