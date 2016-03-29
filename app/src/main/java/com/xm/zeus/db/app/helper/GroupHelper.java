package com.xm.zeus.db.app.helper;

import android.content.Context;
import android.text.TextUtils;

import com.xm.zeus.db.app.dao.ColleagueDao;
import com.xm.zeus.db.app.dao.FriendDao;
import com.xm.zeus.db.app.dao.GroupDao;
import com.xm.zeus.db.app.dao.GroupMemberIdDao;
import com.xm.zeus.db.app.entity.Colleague;
import com.xm.zeus.db.app.entity.Group;
import com.xm.zeus.db.app.entity.GroupMemberId;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by lvxia on 2016-03-29.
 */
public class GroupHelper extends BaseHelper {

    private GroupDao groupDao;
    private GroupMemberIdDao memberIdDao;
    private ColleagueDao colleagueDao;

    public GroupHelper(Context appContext) {
        super(appContext);

        groupDao = daoSession.getGroupDao();
        memberIdDao = daoSession.getGroupMemberIdDao();
        colleagueDao = daoSession.getColleagueDao();
    }

    /**
     * 添加讨论组
     *
     * @param groupList 组集合
     */
    public void addGroups(List<Group> groupList, List<String> memberIds) {

        if (groupList == null || groupList.size() <= 0) {
            return;
        }

        for (Group selfGroup : groupList) {
            deleteMembers(selfGroup.getUid());
            addOrUpdateGroup(selfGroup);
            addMembers(selfGroup.getUid(), memberIds);
        }
    }

    /**
     * @param groupId
     * @return
     */
    public Group findGroupById(String groupId) {
        if (TextUtils.isEmpty(groupId)) {
            return null;
        }

        return groupDao.load(groupId);
    }

    public List<Group> findAllGroup() {
        return groupDao.loadAll();
    }

    /**
     * 查找组员
     *
     * @param memberIds 组员Ids
     * @return
     */
    public List<Colleague> findGroupMembers(List<String> memberIds) {
        List<Colleague> results = new ArrayList<>();
        if (memberIds == null || memberIds.size() == 0) {
            return results;
        }

        List<Colleague> temps = colleagueDao.queryBuilder().where(ColleagueDao.Properties.Uid.in(memberIds)).list();

        if (temps != null && temps.size() > 0) {
            results.addAll(temps);
        }
        return results;
    }

    /**
     * 查找组员
     *
     * @param memberIds 组员Ids
     * @return
     */
    public List<Colleague> findMembers(List<GroupMemberId> memberIds) {
        List<Colleague> results = new ArrayList<>();
        if (memberIds == null || memberIds.size() == 0) {
            return results;
        }

        List<String> ids = new ArrayList<>();
        for (GroupMemberId item : memberIds) {
            ids.add(item.getMemberId());
        }

        return findGroupMembers(ids);

    }

    public List<String> getMemberIds(String groupId) {
        List<String> ids = new ArrayList<>();
        if (TextUtils.isEmpty(groupId)) {
            return ids;
        }

        QueryBuilder qb = memberIdDao.queryBuilder();
        qb.where(GroupMemberIdDao.Properties.GroupId.eq(groupId));
        List<GroupMemberId> memberIds = qb.list();
        if (memberIds != null && memberIds.size() > 0) {
            for (GroupMemberId memberId : memberIds) {
                ids.add(memberId.getMemberId());
            }
        }

        return ids;
    }

    //-----------------Group-------------------//

    public void addGroup(Group group) {
        groupDao.insert(group);
    }

    public void addOrUpdateGroup(Group group) {
        if (group == null || TextUtils.isEmpty(group.getUid())) {
            return;
        }

        if (isGroupExist(group.getUid())) {
            groupDao.update(group);
        } else {
            addGroup(group);
        }

    }

    public void quitGroup(String groupId) {
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        deleteMembers(groupId);
        groupDao.deleteByKey(groupId);

    }

    public boolean isGroupExist(String groupId) {
        if (TextUtils.isEmpty(groupId)) {
            return false;
        }
        Group group = groupDao.load(groupId);
        return group != null;
    }

    //-----------------Members-------------------//
    public void addMembers(String groupId, List<String> memberIds) {
        if (TextUtils.isEmpty(groupId) || memberIds == null || memberIds.size() <= 0) {
            return;
        }

        if (isGroupExist(groupId)) {
            for (String memberId : memberIds) {
                GroupMemberId id = new GroupMemberId();
                id.setMemberId(memberId);
                id.setGroupId(groupId);
                memberIdDao.insert(id);
            }
        }
    }

    public void addMember(GroupMemberId groupMember) {
        memberIdDao.insert(groupMember);
    }

    public void deleteMembers(String groupId) {
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        memberIdDao.queryBuilder().where(GroupMemberIdDao.Properties.GroupId.eq(groupId)).buildDelete().executeDeleteWithoutDetachingEntities();

    }

    public void deleteMembers(String groupId, List<String> memberIds) {
        if (memberIds == null || memberIds.size() <= 0) {
            return;
        }

        if (isGroupExist(groupId)) {
            QueryBuilder qb = memberIdDao.queryBuilder();
            qb.where(GroupMemberIdDao.Properties.GroupId.eq(groupId), GroupMemberIdDao.Properties.MemberId.in(memberIds));
            qb.buildDelete().executeDeleteWithoutDetachingEntities();
        }
    }

}
