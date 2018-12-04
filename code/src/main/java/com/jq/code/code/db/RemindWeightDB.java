package com.jq.code.code.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jq.code.model.RemindeWeightTimeInfo;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

/**
 * 设置里面的-称重提醒
 **/
public class RemindWeightDB {

    /**
     * 称重提醒建表
     */
    public static final String TABLE_NAME = "cs_weight_remind";
    public static final String CREATE_TABLE_WEIGHT_REMIND = "create table if not exists "
            + TABLE_NAME
            + "(id integer not null, "
            + "account_id integer not null, "
            + "remind_time varchar(20) null, "
            + "is_open integer not null, "
            + "mon_open integer, "
            + "tue_open integer, "
            + "wed_open integer, "
            + "thu_open integer, "
            + "fri_open integer, "
            + "sat_open integer, "
            + "sun_open integer, " + "once_open integer)";

    public static final String ALARM_INTENT = "alarm_okok";
    private DB mDBUtil;

    public RemindWeightDB(Context context) {
        mDBUtil = DB.getInstance(context.getApplicationContext());

    }

    public static RemindWeightDB getInstance(Context context) {
        RemindWeightDB instance = new RemindWeightDB(context);
        return instance;
    }

    /**
     * 创建提醒信息
     *
     * @param info
     * @return
     */
    public void createWeightRemind(RemindeWeightTimeInfo info) {
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            mDBUtil.insert(db,TABLE_NAME, creatContentValue(info), SQLiteDatabase.CONFLICT_IGNORE);
            mDBUtil.closeDB();
        } finally {
            writeLock.unlock();
        }
    }

    public void removeRemindByAccountId(long accountId){
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            mDBUtil.delete(mDBUtil.getWritableDatabase(),TABLE_NAME,"account_id=?",new String[]{accountId + ""});
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 修改提醒信息
     *
     * @param info
     * @return
     */
    public void modifyWeightRemind(RemindeWeightTimeInfo info) {
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            mDBUtil.update(db,TABLE_NAME, creatContentValue(info),"id=?", new String[]{String.valueOf(info.getId())});
            mDBUtil.closeDB();
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 查找数据库中所有的提醒信息
     *
     * @return
     */
    public ArrayList<RemindeWeightTimeInfo> findWeightRemindAllByAccountId(
            int account_id) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            ArrayList<RemindeWeightTimeInfo> roleInfos = new ArrayList<RemindeWeightTimeInfo>();
            Cursor c = db.rawQuery("select * from " + TABLE_NAME
                    + " where account_id=" + account_id,null);
            while (c.moveToNext()) {
                roleInfos.add(getContentValue(c));
            }
            c.close();
            mDBUtil.closeDB();
            return roleInfos;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 查找数据库中特定id角色信息
     *
     * @return
     */
    public RemindeWeightTimeInfo findWeightRemindById(int id) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            RemindeWeightTimeInfo info = new RemindeWeightTimeInfo();
            Cursor c = db.rawQuery("select * from " + TABLE_NAME + " where "
                    + "id=?", new String[]{"" + id});
            while (c.moveToNext()) {
                info = getContentValue(c);
            }
            c.close();
            mDBUtil.closeDB();
            return info;
        } finally {
            readLock.unlock();
        }
    }

    private ContentValues creatContentValue(RemindeWeightTimeInfo info) {
        ContentValues values = new ContentValues();
        values.put("id", info.getId());
        values.put("account_id", info.getAccount_id());
        values.put("remind_time", info.getRemind_time());
        values.put("is_open", info.getIs_open());
        values.put("mon_open", info.getMon_open());
        values.put("tue_open", info.getTue_open());
        values.put("wed_open", info.getWed_open());
        values.put("thu_open", info.getThu_open());
        values.put("fri_open", info.getFri_open());
        values.put("sat_open", info.getSat_open());
        values.put("sun_open", info.getSun_open());
        values.put("once_open", info.getOnce_open());
        return values;
    }

    private RemindeWeightTimeInfo getContentValue(Cursor c) {
        RemindeWeightTimeInfo info = new RemindeWeightTimeInfo();
        info.setId(c.getInt(0));
        info.setAccount_id(c.getInt(1));
        info.setRemind_time(c.getString(2));
        info.setIs_open(c.getInt(3));
        info.setMon_open(c.getInt(4));
        info.setTue_open(c.getInt(5));
        info.setWed_open(c.getInt(6));
        info.setThu_open(c.getInt(7));
        info.setFri_open(c.getInt(8));
        info.setSat_open(c.getInt(9));
        info.setSun_open(c.getInt(10));
        info.setOnce_open(c.getInt(11));
        return info;
    }
}
