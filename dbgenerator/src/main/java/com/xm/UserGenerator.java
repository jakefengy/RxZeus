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
public class UserGenerator {

    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(1, "com.xm.zeus.db.user.entity");

        addBizTimeStamp(schema);

        schema.setDefaultJavaPackageDao("com.xm.zeus.db.user.dao");
        schema.enableKeepSectionsByDefault();
        schema.enableActiveEntitiesByDefault();
        new DaoGenerator().generateAll(schema, "./dbgenerator/src/main/java");
    }

    /**
     * TimeStamp
     *
     * @param schema
     */
    private static void addBizTimeStamp(Schema schema) {
        Entity user = schema.addEntity("User");

        user.setTableName("User");
        user.addStringProperty("UserId").primaryKey();
        user.addStringProperty("UserName");
        user.addStringProperty("Password");
        user.addStringProperty("Token");
        user.addStringProperty("Status");
        user.addBooleanProperty("IsAutoLogin");
        user.addLongProperty("LoggedDate");
    }

}
