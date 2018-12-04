package com.jq.code.code.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jq.code.model.RoleInfo;

import java.util.ArrayList;
import java.util.List;

public class RoleDB {

    public static final String TABLE_NAME = "cs_role";
    /**
     * 角色建表
     */
    public static final String CREATE_TABLE_ROLE = "create table if not exists "
            + TABLE_NAME
            + " (id integer UNIQUE not null, "
            + "account_id integer not null, "
            + "nickname varchar(20) not null, "
            + "height integer not null,"
            + "sex varchar(2) not null,"
            + "birthday date not null,"
            + "create_time varchar(32),"
            + "modify_time varhcar(32),"
            + "current_state tinyint,"
            + "period_time varhcar(32),"
            + "icon_image_path varhcar(32),"
            + "icon_image_create_time varhcar(32),"
            + "sync_time varhcar(32),"
            + "weight_goal float null,"
            + "weight_init float null,"
            + "role_type integer null,"
            + "unique(id) on conflict replace)";

    private DB mDBUtil;
    private static RoleDB instance;

    /**
     * 析构函数
     */
    private RoleDB(Context context) {
        mDBUtil = DB.getInstance(context);
    }

    /**
     * 得到当前实体类
     */
    public static RoleDB getInstance(Context context) {
        if (instance == null) {
            instance = new RoleDB(context);
        }
        return instance;
    }

    /**
     * 添加角色信息
     *
     * @param info
     * @return
     */
    public void createRole(RoleInfo info) {
        synchronized (mDBUtil) {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            db.insertWithOnConflict("cs_role", null, creatContentValue(info),
                    SQLiteDatabase.CONFLICT_REPLACE);
            
        }
    }

    public void createRole(List<RoleInfo> infos) {
        synchronized (mDBUtil) {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            db.beginTransaction();
            for (RoleInfo kindInfo : infos) {
                db.insertWithOnConflict("cs_role", null, creatContentValue(kindInfo),
                        SQLiteDatabase.CONFLICT_REPLACE);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            
        }
    }

    /**
     * 删除角色
     *
     * @param info
     * @return
     */
    public int removeRole(RoleInfo info) {
        return mDBUtil.delete(mDBUtil.getWritableDatabase(), "cs_role", info.getId());
    }

    /**
     * 清除角色表
     *
     * @return
     */
    public int clearRole() {
        return mDBUtil.delete(mDBUtil.getWritableDatabase(), "cs_role", null, null);
    }

    public void removeRoleByAccountId(long accountId) {
        mDBUtil.delete(mDBUtil.getWritableDatabase(), "cs_role", "account_id=?", new String[]{accountId + ""});
    }

    /**
     * 修改角色信息
     *
     * @param info
     * @return
     */
    public void modifyRole(RoleInfo info) {
        synchronized (mDBUtil) {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            mDBUtil.update(db, "cs_role", creatContentValue(info),"id=?", new String[]{String.valueOf(info.getId())});
            
        }
    }

    public int getRoleCount(long account_id) {
        synchronized (mDBUtil) {
            int count = 0;
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            Cursor c = null;
            c = db.rawQuery("select count(*) from " + "cs_role where account_id="
                    + account_id, null);
            c.moveToFirst();
            count = c.getInt(0);
            c.close();
            
            return count;
        }
    }

    /**
     * 查找数据库中所有的角色信息
     *
     * @return
     */
    public ArrayList<RoleInfo> findRoleAllByAccountId(int account_id) {
        synchronized (mDBUtil) {
            ArrayList<RoleInfo> roleInfos = new ArrayList<RoleInfo>();
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            Cursor c = null;
            c = db.rawQuery("select * from cs_role where account_id=? order by create_time asc", new String[]{String.valueOf(account_id)});
            while (c.moveToNext()) {
                roleInfos.add(getContentValue(c));
            }
            c.close();
            
            return roleInfos;
        }
    }

    /**
     * 查找数据库中特定id角色信息
     *
     * @return
     */
    public RoleInfo findRoleById(long account_id, long id) {

        synchronized (mDBUtil) {
            RoleInfo info = null;
            Cursor c = null;
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            String sql = "select * from " + "cs_role" + " where " + "account_id=? and id=?";
            c = db.rawQuery(sql, new String[]{String.valueOf(account_id), String.valueOf(id)});

            if (c.moveToNext()) {
                info = getContentValue(c);
            }
            c.close();
            
            return info;
        }
    }

    /**
     * 查找数据库中特定id角色信息
     *
     * @return
     */
    public RoleInfo findRoleById(long id) {

        synchronized (mDBUtil) {
            RoleInfo info = null;
            Cursor c = null;
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            String sql = "select * from " + "cs_role" + " where " + "id=?";
            c = db.rawQuery(sql, new String[]{"" + id});

            if (c.moveToNext()) {
                info = getContentValue(c);
            }
            c.close();
            
            return info;
        }
    }

    private ContentValues creatContentValue(RoleInfo info) {
        ContentValues values = new ContentValues();
        values.put("id", info.getId());
        values.put("account_id", info.getAccount_id());
        values.put("nickname", info.getNickname());
        values.put("height", info.getHeight());
        values.put("sex", "" + info.getSex());
        values.put("birthday", info.getBirthday());
        values.put("create_time", info.getCreate_time());
        values.put("modify_time", info.getModify_time());
        values.put("current_state", info.getCurrent_state());
        values.put("period_time", info.getPeriod_time());
        values.put("icon_image_path", info.getIcon_image_path());
        values.put("icon_image_create_time", info.getIcon_image_create_time());
        values.put("sync_time", info.getSync_time());
        values.put("weight_goal", info.getWeight_goal());
        values.put("weight_init", info.getWeight_init());
        values.put("role_type",info.getRole_type()) ;
        return values;
    }

    private RoleInfo getContentValue(Cursor c) {
        RoleInfo info = new RoleInfo();
        info.setId(c.getInt(c.getColumnIndex("id")));
        info.setAccount_id(c.getInt(c.getColumnIndex("account_id")));
        info.setNickname(c.getString(c.getColumnIndex("nickname")));
        info.setHeight(c.getInt(c.getColumnIndex("height")));
        info.setSex(c.getString(c.getColumnIndex("sex")));
        info.setBirthday(c.getString(c.getColumnIndex("birthday")));
        info.setCreate_time(c.getString(c.getColumnIndex("create_time")));
        info.setModify_time(c.getString(c.getColumnIndex("modify_time")));
        info.setCurrent_state(c.getInt(c.getColumnIndex("current_state")));
        info.setPeriod_time(c.getString(c.getColumnIndex("period_time")));
        info.setIcon_image_path(c.getString(c.getColumnIndex("icon_image_path")));
        info.setIcon_image_create_time(c.getString(c.getColumnIndex("icon_image_create_time")));
        info.setSync_time(c.getString(c.getColumnIndex("sync_time")));
        info.setWeight_goal(c.getFloat(c.getColumnIndex("weight_goal")));
        info.setWeight_init(c.getFloat(c.getColumnIndex("weight_init")));
        info.setRole_type(c.getInt(c.getColumnIndex("role_type")));
        return info;
    }
}
