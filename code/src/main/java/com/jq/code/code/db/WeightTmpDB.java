package com.jq.code.code.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.jq.code.code.util.LogUtil;
import com.jq.code.model.WeightTmpEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * 测量到体重数据，还没匹配到某个角色的临时数据
 */

public class WeightTmpDB {
    public static final String TABLE_NAME = "cs_tmp_weight_data";
    /**
     * 角色数据建表
     */
    public static final String CREATE_TABLE = "create table if not exists "
            + TABLE_NAME
            + "(id integer primary key, "
            + "weight float null,"
            + "weight_time date not null,"
            + "impedance float null,"
            + "scaleweight varchar(20) null,"
            + "scaleproperty integer null,"
            + "account_id bigint not null, "
            + "unique(weight_time) on conflict replace)";

    private DB mDBUtil;

    private WeightTmpDB(Context context) {
        mDBUtil = DB.getInstance(context.getApplicationContext());
    }

    public static WeightTmpDB getInstance(Context context) {
        WeightTmpDB instance = new WeightTmpDB(context);
        return instance;
    }

    public void create(WeightTmpEntity info) {
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            mDBUtil.insert(db, "cs_tmp_weight_data", creatContentValue(info), SQLiteDatabase.CONFLICT_REPLACE);
            mDBUtil.closeDB();
        } finally {
            writeLock.unlock();
        }
    }

    public void create(List<WeightTmpEntity> putBases) {
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            db.beginTransaction();
            for (WeightTmpEntity kindInfo : putBases) {
                mDBUtil.insert(db, "cs_tmp_weight_data", creatContentValue(kindInfo), SQLiteDatabase.CONFLICT_REPLACE);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            mDBUtil.closeDB();
        } finally {
            writeLock.unlock();
        }
    }

    public void remove(ArrayList<WeightTmpEntity> deleted) {
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            db.beginTransaction();
            for (WeightTmpEntity entity : deleted) {
                mDBUtil.delete(db, "cs_tmp_weight_data", "id=?", new String[]{String.valueOf(entity.getId())});
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            mDBUtil.closeDB();
        } finally {
            writeLock.unlock();
        }
    }


    public int getCount(int account_id) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            Cursor cursor = db.rawQuery("select count(*) from cs_tmp_weight_data where account_id=?", new String[]{String.valueOf(account_id)});
            int count = 0;
            while (cursor.moveToNext()) {
                count = cursor.getInt(0);
            }
            cursor.close();
            mDBUtil.closeDB();
            return count;
        } finally {
            readLock.unlock();
        }
    }

    public ArrayList<WeightTmpEntity> findALL(int account_id) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from cs_tmp_weight_data where account_id=? order by weight_time desc", new String[]{String.valueOf(account_id)});
            ArrayList<WeightTmpEntity> entities = new ArrayList<>();
            while (cursor.moveToNext()) {
                entities.add(getContentValue(cursor));
            }
            LogUtil.e("findALL", "entities:" + entities);
            cursor.close();
            mDBUtil.closeDB();
            return entities;
        } finally {
            readLock.unlock();
        }
    }

    private ContentValues creatContentValue(WeightTmpEntity info) {
        ContentValues values = new ContentValues();
        values.put("weight", info.getWeight());
        values.put("weight_time", info.getWeight_time());
        values.put("account_id", info.getAccount_id());
        values.put("impedance", info.getImpedance());
        values.put("scaleproperty", info.getScaleproperty());
        values.put("scaleweight", info.getScaleweight());
        return values;
    }

    private WeightTmpEntity getContentValue(Cursor c) {
        WeightTmpEntity info = new WeightTmpEntity();
        info.setId(c.getInt(c.getColumnIndex("id")));
        info.setWeight(c.getFloat(c.getColumnIndex("weight")));
        info.setWeight_time(c.getString(c.getColumnIndex("weight_time")));
        info.setAccount_id(c.getLong(c.getColumnIndex("account_id")));
        info.setImpedance(c.getFloat(c.getColumnIndex("impedance")));
        info.setScaleproperty((byte) c.getInt(c.getColumnIndex("scaleproperty")));
        info.setScaleweight(c.getString(c.getColumnIndex("scaleweight")));
        return info;
    }
}
