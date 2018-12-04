package com.jq.code.code.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.jq.code.model.PushInfo;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

/** 从服务端下载的推送数据，就是显示的“百科”数据 */
public class PushDataDB {

    public static final String TABLE_NAME = "cs_push_data";
    /**
     * 角色建表
     */
    public static final String CREATE_TABLE_PUSH = "create table if not exists "
            + TABLE_NAME
            + " (id integer UNIQUE, "
            + "categories integer, "
            + "uri varchar(32), "
            + "cover varchar(32),"
            + "title varchar(32),"
            + "ts bigint,"
            + "localUrl varchar(32),"
            + "company_id integer,"
            + "sex varchar(32),"
            + "fav text,"
            + "ncomments integer, "
            + "nlikes integer,"
            + "unique(id) on conflict replace)";

    private DB mDBUtil;

    /**
     * 析构函数
     */
    private PushDataDB(Context context) {
        mDBUtil = DB.getInstance(context.getApplicationContext());
    }

    /**
     * 得到当前实体类
     */
    public static PushDataDB getInstance(Context context) {
        PushDataDB instance = new PushDataDB(context);
        return instance;
    }

    /**
     * 添加角色信息
     *
     * @param info
     * @return
     */
    public void createPushInfo(PushInfo info) {
        if (info.getId() <= 0) return;
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            mDBUtil.insert(db, "cs_push_data", creatContentValue(info), SQLiteDatabase.CONFLICT_REPLACE);
            mDBUtil.closeDB();
        } finally {
            writeLock.unlock();
        }
    }


    /**
     * 删除角色
     *
     * @param info
     * @return
     */
    public int removePushInfo(PushInfo info) {
        return mDBUtil.delete(mDBUtil.getWritableDatabase(), "cs_push_data", info.getId());
    }

    /**
     * 清除角色表
     *
     * @return
     */
    public int clearPushInfo() {
        return mDBUtil.delete(mDBUtil.getWritableDatabase(), "cs_push_data", null, null);
    }

    /**
     * 修改角色信息
     *
     * @param info
     * @return
     */
    public void modifyPushInfo(PushInfo info) {
        if (info.getId() <= 0) return;
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            mDBUtil.update(db, "cs_push_data", creatContentValue(info), "id=?", new String[]{String.valueOf(info.getId())});
            mDBUtil.closeDB();
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 查找数据库中特定id角色信息
     *
     * @return
     */
    public PushInfo findPushInfo(int id) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            PushInfo pushInfo = null;
            Cursor c = null;
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            String sql = "select * from cs_push_data where id=?";
            c = db.rawQuery(sql, new String[]{"" + id});
            while (c.moveToNext()) {
                pushInfo = getContentValue(c);
            }
            c.close();
            mDBUtil.closeDB();
            return pushInfo;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 查找数据库中特定id角色信息
     *
     * @return
     */
    public ArrayList<PushInfo> findPushInfo(int categories, long time, long cnt, int company_id, String sex) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            ArrayList<PushInfo> pushInfos = new ArrayList<PushInfo>();
            Cursor c = null;
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            String sql = "select * from " + "cs_push_data" + " where " +
                    "categories=? and company_id=? and sex=? and ts<" + time + " order by ts desc limit " + cnt;
            c = db.rawQuery(sql, new String[]{"" + categories, "" + company_id, sex});
            while (c.moveToNext()) {
                pushInfos.add(getContentValue(c));
            }
            c.close();
            mDBUtil.closeDB();
            return pushInfos;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 改推送信息是否存在
     *
     * @param info
     * @return true 存在 false不存在
     */
    public boolean isExist(PushInfo info) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            Cursor c = null;
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            String sql = "select * from " + "cs_push_data" + " where id=?";
            c = db.rawQuery(sql, new String[]{"" + info.getId()});
            boolean b = c.getColumnCount() > 0;
            c.close();
            mDBUtil.closeDB();
            return b;
        } finally {
            readLock.unlock();
        }
    }

    private ContentValues creatContentValue(PushInfo info) {
        ContentValues values = new ContentValues();
        values.put("id", info.getId());
        values.put("categories", info.getCategories());
        values.put("fav", info.getFav());
        values.put("uri", info.getUri());
        values.put("cover", info.getCover());
        values.put("title", info.getTitle());
        values.put("ts", info.getTs());
        values.put("localUrl", info.getLocalUrl());
        values.put("company_id", info.getCompany_id());
        values.put("sex", info.getSex());
        values.put("ncomments", info.getNcomments());
        values.put("nlikes", info.getNlikes());
        return values;
    }

    private PushInfo getContentValue(Cursor c) {
        PushInfo info = new PushInfo();
        info.setId(c.getInt(c.getColumnIndex("id")));
        info.setCategories(c.getInt(c.getColumnIndex("categories")));
        info.setUri(c.getString(c.getColumnIndex("uri")));
        info.setCover(c.getString(c.getColumnIndex("cover")));
        info.setTitle(c.getString(c.getColumnIndex("title")));
        info.setTs(c.getLong(c.getColumnIndex("ts")));
        info.setLocalUrl(c.getString(c.getColumnIndex("localUrl")));
        info.setCompany_id(c.getInt(c.getColumnIndex("company_id")));
        info.setSex(c.getString(c.getColumnIndex("sex")));
        info.setNcomments(c.getInt(c.getColumnIndex("ncomments")));
        info.setNlikes(c.getInt(c.getColumnIndex("nlikes")));
        info.setFav(c.getString(c.getColumnIndex("fav")));
        return info;
    }
}
