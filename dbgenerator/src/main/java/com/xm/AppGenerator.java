package com.xm;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Generator GreenDao
 *
 * @author fengy
 */
public class AppGenerator {

    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(1, "com.xm.zeus.db.app.entity");

//        addUser(schema);
        addColleague(schema);
//        addFriend(schema);
//        addGroupAndMembers(schema);
//        addTimeStamp(schema);
//        addOrg(schema);
//        addSetting(schema);

        schema.setDefaultJavaPackageDao("com.xm.zeus.db.app.dao");
        schema.enableKeepSectionsByDefault();
        schema.enableActiveEntitiesByDefault();
        new DaoGenerator().generateAll(schema, "./dbgenerator/src/main/java");
    }

    private static void addUser(Schema schema) {
        Entity user = schema.addEntity("User");

        user.setTableName("User");
        user.addStringProperty("UserId").primaryKey();
        user.addStringProperty("UserName");
        user.addStringProperty("Password");
        user.addStringProperty("Token");
        user.addStringProperty("Org");
        user.addBooleanProperty("Logged");
        user.addBooleanProperty("AutoLogin");
        user.addLongProperty("LoggedDate");
    }

    /**
     * Colleague
     *
     * @param schema
     */
    private static void addColleague(Schema schema) {
        Entity parent = schema.addEntity("Colleague");

        parent.setTableName("Colleague");
        parent.addStringProperty("uid").primaryKey();
        parent.addStringProperty("username");
        parent.addStringProperty("sex");
        parent.addStringProperty("mobile");
        parent.addStringProperty("tel");
        parent.addStringProperty("email");
        parent.addStringProperty("avatarid");
        parent.addIntProperty("type");
        parent.addLongProperty("timestamp");

        parent.addStringProperty("spelling");
        parent.addStringProperty("firstletter");

        Entity dept = schema.addEntity("ColleagueDept");
        dept.setTableName("ColleagueDept");
        dept.addStringProperty("no");
        dept.addStringProperty("name");
        dept.addStringProperty("title");
        dept.addIntProperty("isdefault");
        Property personId = dept.addStringProperty("personId").notNull()
                .getProperty();

        ToMany personDepts = parent.addToMany(dept, personId);
        personDepts.setName("dept");

    }

    /**
     * Friend
     *
     * @param schema
     */
    private static void addFriend(Schema schema) {
        Entity parent = schema.addEntity("Friend");

        parent.setTableName("Friend");
        parent.addStringProperty("uid").primaryKey();
        parent.addStringProperty("username");
        parent.addStringProperty("sex");
        parent.addStringProperty("mobile");
        parent.addStringProperty("tel");
        parent.addStringProperty("email");
        parent.addStringProperty("company");
        parent.addStringProperty("dept");
        parent.addStringProperty("post");
        parent.addStringProperty("avatarid");
        parent.addIntProperty("type");
        parent.addStringProperty("spelling");
        parent.addStringProperty("firstLetter");
        parent.addStringProperty("headName");
        parent.addIntProperty("dataType");
        parent.addLongProperty("timestamp");

    }

    /**
     * GroupAndMembers
     *
     * @param schema
     */
    private static void addGroupAndMembers(Schema schema) {

        Entity group = schema.addEntity("Group");

        group.setTableName("Group");
        group.addStringProperty("uid").primaryKey().getProperty();
        group.addStringProperty("name");
        group.addStringProperty("avatarid");
        group.addStringProperty("headName");
        group.addIntProperty("dataType");
        group.addLongProperty("timestamp");

        Entity members = schema.addEntity("GroupMemberId");
        members.setTableName("GroupMember");
        members.addIdProperty();
        Property groupId = members.addStringProperty("groupId").notNull().getProperty();
        members.addStringProperty("memberId").notNull();

        members.addToOne(group, groupId);

        ToMany groupToMembers = group.addToMany(members, groupId);
        groupToMembers.setName("memberIds");

    }

    /**
     * TimeStamp
     *
     * @param schema
     */
    private static void addTimeStamp(Schema schema) {
        Entity timeStamp = schema.addEntity("TimeStamp");

        timeStamp.setTableName("TimeStamp");
        timeStamp.addStringProperty("ModuleName").primaryKey();
        timeStamp.addLongProperty("TimeStamp").notNull();
    }

    /**
     * Org
     *
     * @param schema
     */
    private static void addOrg(Schema schema) {
        Entity entity = schema.addEntity("Org");

        entity.setTableName("Org");
        entity.addStringProperty("uid").primaryKey();
        entity.addStringProperty("pid").notNull();
        entity.addStringProperty("name");
    }

    /**
     * Setting
     *
     * @param schema
     */
    private static void addSetting(Schema schema) {
        Entity entity = schema.addEntity("Setting");

        entity.setTableName("Setting");
        entity.addStringProperty("key").primaryKey();
        entity.addStringProperty("content");
        entity.addStringProperty("remark");
    }

}
