package com.xm;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Generator GreenDao
 *
 * @author fengy
 */
public class UserGenerator {

    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(1, "com.xm.zeus.db.user.entity");

        addUser(schema);

        schema.setDefaultJavaPackageDao("com.xm.zeus.db.user.dao");
        schema.enableKeepSectionsByDefault();
        schema.enableActiveEntitiesByDefault();
        new DaoGenerator().generateAll(schema, "./dbgenerator/src/main/java");
    }

    /**
     * User
     *
     * @param schema
     */
    private static void addUser(Schema schema) {
        Entity user = schema.addEntity("User");

        user.setTableName("User");
        user.addStringProperty("UserId").primaryKey();
        user.addStringProperty("UserName");
        user.addStringProperty("Password");
        user.addStringProperty("Token");
        user.addStringProperty("Org");
        user.addBooleanProperty("Logged");
        user.addLongProperty("LoggedDate");
    }

}
