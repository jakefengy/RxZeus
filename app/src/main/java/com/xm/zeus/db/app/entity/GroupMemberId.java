package com.xm.zeus.db.app.entity;

import com.xm.zeus.db.app.dao.DaoSession;
import com.xm.zeus.db.app.dao.GroupDao;
import com.xm.zeus.db.app.dao.GroupMemberIdDao;

import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "GroupMember".
 */
public class GroupMemberId {

    private Long id;
    /** Not-null value. */
    private String groupId;
    /** Not-null value. */
    private String memberId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient GroupMemberIdDao myDao;

    private Group group;
    private String group__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public GroupMemberId() {
    }

    public GroupMemberId(Long id) {
        this.id = id;
    }

    public GroupMemberId(Long id, String groupId, String memberId) {
        this.id = id;
        this.groupId = groupId;
        this.memberId = memberId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGroupMemberIdDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getGroupId() {
        return groupId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /** Not-null value. */
    public String getMemberId() {
        return memberId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    /** To-one relationship, resolved on first access. */
    public Group getGroup() {
        String __key = this.groupId;
        if (group__resolvedKey == null || group__resolvedKey != __key) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GroupDao targetDao = daoSession.getGroupDao();
            Group groupNew = targetDao.load(__key);
            synchronized (this) {
                group = groupNew;
            	group__resolvedKey = __key;
            }
        }
        return group;
    }

    public void setGroup(Group group) {
        if (group == null) {
            throw new DaoException("To-one property 'groupId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.group = group;
            groupId = group.getUid();
            group__resolvedKey = groupId;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
