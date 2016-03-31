package com.xm.zeus.db.user.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.xm.zeus.db.user.entity.User;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "User".
*/
public class UserDao extends AbstractDao<User, String> {

    public static final String TABLENAME = "User";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property UserId = new Property(0, String.class, "UserId", true, "USER_ID");
        public final static Property UserName = new Property(1, String.class, "UserName", false, "USER_NAME");
        public final static Property Password = new Property(2, String.class, "Password", false, "PASSWORD");
        public final static Property Token = new Property(3, String.class, "Token", false, "TOKEN");
        public final static Property Status = new Property(4, String.class, "Status", false, "STATUS");
        public final static Property Logged = new Property(5, Boolean.class, "Logged", false, "LOGGED");
        public final static Property LoggedDate = new Property(6, Long.class, "LoggedDate", false, "LOGGED_DATE");
    };

    private DaoSession daoSession;


    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"User\" (" + //
                "\"USER_ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: UserId
                "\"USER_NAME\" TEXT," + // 1: UserName
                "\"PASSWORD\" TEXT," + // 2: Password
                "\"TOKEN\" TEXT," + // 3: Token
                "\"STATUS\" TEXT," + // 4: Status
                "\"LOGGED\" INTEGER," + // 5: Logged
                "\"LOGGED_DATE\" INTEGER);"); // 6: LoggedDate
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"User\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
 
        String UserId = entity.getUserId();
        if (UserId != null) {
            stmt.bindString(1, UserId);
        }
 
        String UserName = entity.getUserName();
        if (UserName != null) {
            stmt.bindString(2, UserName);
        }
 
        String Password = entity.getPassword();
        if (Password != null) {
            stmt.bindString(3, Password);
        }
 
        String Token = entity.getToken();
        if (Token != null) {
            stmt.bindString(4, Token);
        }
 
        String Status = entity.getStatus();
        if (Status != null) {
            stmt.bindString(5, Status);
        }
 
        Boolean Logged = entity.getLogged();
        if (Logged != null) {
            stmt.bindLong(6, Logged ? 1L: 0L);
        }
 
        Long LoggedDate = entity.getLoggedDate();
        if (LoggedDate != null) {
            stmt.bindLong(7, LoggedDate);
        }
    }

    @Override
    protected void attachEntity(User entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // UserId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // UserName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // Password
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // Token
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // Status
            cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0, // Logged
            cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6) // LoggedDate
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.setUserId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setUserName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPassword(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setToken(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setStatus(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setLogged(cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0);
        entity.setLoggedDate(cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(User entity, long rowId) {
        return entity.getUserId();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(User entity) {
        if(entity != null) {
            return entity.getUserId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
