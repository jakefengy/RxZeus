package com.xm.zeus.db.app.helper;

import android.database.Cursor;
import android.text.TextUtils;

import com.xm.zeus.db.AppDbHelper;
import com.xm.zeus.db.app.dao.ColleagueDao;
import com.xm.zeus.db.app.dao.ColleagueDeptDao;
import com.xm.zeus.db.app.entity.Colleague;
import com.xm.zeus.db.app.entity.ColleagueDept;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by lvxia on 2016-03-29.
 */
public class ColleagueHelper {

    private ColleagueDao personDao;
    private ColleagueDeptDao personDeptDao;

    public ColleagueHelper() {

        personDao = AppDbHelper.getInstance().getAppDaoSession().getColleagueDao();
        personDeptDao = AppDbHelper.getInstance().getAppDaoSession().getColleagueDeptDao();

    }

    public void saveOrUpdate(Colleague person) {

        if (person == null) {
            return;
        }

        Colleague find = personDao.load(person.getUid());

        if (find == null) {
            personDao.insert(person);
        } else {
            personDao.update(person);
        }

        personDeptDao.queryBuilder()
                .where(ColleagueDeptDao.Properties.PersonId.eq(person.getUid()))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();

        List<ColleagueDept> depts = person.getPersonDepts();
        if (depts != null && depts.size() > 0) {
            for (ColleagueDept dept : depts) {
                dept.setPersonId(person.getUid());
                personDeptDao.insert(dept);
            }
        }

    }

    public void update(Colleague person) {
        if (person == null) {
            return;
        }

        personDao.update(person);
    }

    public void deleteById(String id) {
        if (TextUtils.isEmpty(id)) {
            return;
        }
        personDao.deleteByKey(id);
    }

    public Colleague findById(String id) {
        if (TextUtils.isEmpty(id)) {
            return null;
        }

        return personDao.load(id);
    }

    public List<Colleague> findAll() {
        List<Colleague> results = new ArrayList<>();

        List<Colleague> find = personDao.loadAll();
        if (find != null && find.size() > 0)
            results.addAll(find);

        return results;

    }

    public List<Colleague> findAll(List<String> ids) {
        List<Colleague> results = new ArrayList<>();
        if (ids == null || ids.size() == 0) {
            return results;
        }

        QueryBuilder qb = personDao.queryBuilder();
        qb.where(ColleagueDao.Properties.Uid.in(ids));
        return qb.list();

    }

    public List<Colleague> findByOrgId(String orgId) {
        if (TextUtils.isEmpty(orgId)) {
            return null;
        }

        String deptTbName = ColleagueDeptDao.TABLENAME;
        String deptTbId = ColleagueDeptDao.Properties.PersonId.columnName;
        String deptTbNo = ColleagueDeptDao.Properties.No.columnName;

        String sql = " select distinct " + deptTbId + " from " + deptTbName + " where " + deptTbNo + " = '" + orgId + "'";
        Cursor cursor = personDao.getDatabase().rawQuery(sql, new String[]{});

        List<String> uids = new ArrayList<>();
        while (cursor.moveToNext()) {
            uids.add(cursor.getString(0));
        }
        cursor.close();
        return personDao.queryBuilder().where(ColleagueDao.Properties.Uid.in(uids)).list();

    }

}
