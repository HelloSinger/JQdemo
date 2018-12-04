package com.jq.code.code.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jq.code.model.SyncDataInfo;

import java.util.concurrent.locks.Lock;

/**
 * 记录同步状态的表
 * */
public class SyncDataDB2 {

    /** 表名 */
    public static final String TABLE_NAME = "cs_sync_log2";
    /**
     * 同步数据日志表
     */
    public static final String CREATE_TABLE_SYNC = "create table if not exists "
            + TABLE_NAME
            + " (role_id integer not null, "
            + "account_id integer not null, "
            + "start bigint,"
            + "end bigint,"
            + "lastsync bigint,"
            + "mtype text,"
            + "primary key(role_id,mtype) on conflict replace)";
    private DB mDBUtil;

    /**
     * 析构函数
     */
    private SyncDataDB2(Context context) {
        mDBUtil = DB.getInstance(context.getApplicationContext());
    }

    /**
     * 得到当前实体类
     */
    public static SyncDataDB2 getInstance(Context context) {
        SyncDataDB2 instance = new SyncDataDB2(context);
        return instance;
    }

    /**
     * 按角色和类别读取同步记录
     * @param accountId 账号ID
     * @param roleId 角色ID
     * @param mtype 数据类型，如体重、运动、饮食
     * @return 一条同步记录
     */
    public SyncDataInfo findSyncRecord(long accountId, long roleId, String mtype) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            Cursor c = null;
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            String sql = "select * from " + TABLE_NAME + " where " + "account_id=? and role_id=? and mtype=?";
            c = db.rawQuery(sql, new String[]{"" + accountId, "" + roleId, mtype});
            SyncDataInfo syncDataInfo = null;
            if (c.moveToNext()) {
                syncDataInfo = getContentValue(c);
            }
            c.close();
            mDBUtil.closeDB();
            return syncDataInfo;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 创建一条同步数据
     *
     * @param info
     * @return
     */
    public void createSyncDataInfo(SyncDataInfo info) {
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            db.insertWithOnConflict(TABLE_NAME, null, createContentValue(info),
                    SQLiteDatabase.CONFLICT_REPLACE);
            mDBUtil.closeDB();
        } finally {
            writeLock.unlock();
        }
    }

    private SyncDataInfo getContentValue(Cursor c) {
        SyncDataInfo info = new SyncDataInfo();
        info.setRole_id(c.getInt(0));
        info.setAccount_id(c.getInt(1));
        info.setMtype(c.getString(c.getColumnIndex("mtype")));
        info.setStart(c.getLong(2));
        info.setEnd(c.getLong(3));
        info.setLastsync(c.getLong(4));
        return info;
    }

    private ContentValues createContentValue(SyncDataInfo info) {
        ContentValues values = new ContentValues();
        values.put("account_id", info.getAccount_id());
        values.put("role_id", info.getRole_id());
        values.put("start", info.getStart());
        values.put("end", info.getEnd());
        values.put("lastsync", info.getLastsync());
        values.put("mtype", info.getMtype());
        return values;
    }
}
