package com.jq.code.code.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jq.code.code.business.Account;
import com.jq.code.model.FeedbackReplyEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * 反馈回复表
 */
public class FeedbackReplyDB {
    public static final String TABLE_NAME = "cs_feedback_reply_data";
    /**
     * 角色建表
     */
    public static final String CREATE_TABLE = "create table if not exists "
            + TABLE_NAME
            + "(id bigint UNIQUE, "
            + "account_id bigint, "
            + "fid bigint,"
            + "content text, "
            + "ua text,"
            + "ts bigint,"
            + "nts bigint,"
            + "rtype text,"
            + "adminid bigint,"
            + "auid text,"
            + "unique(id) on conflict replace)";

    private DB mDBUtil;
    private Context context;

    /**
     * 析构函数
     */
    private FeedbackReplyDB(Context context) {
        mDBUtil = DB.getInstance(context.getApplicationContext());
        this.context = context.getApplicationContext();
    }

    /**
     * 得到当前实体类
     */
    public static FeedbackReplyDB getInstance(Context context) {
        FeedbackReplyDB instance = new FeedbackReplyDB(context);
        return instance;
    }

    /**
     * 添加角色信息
     *
     * @param info
     * @return
     */
    public void create(FeedbackReplyEntity info) {
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            mDBUtil.insert(db, "cs_feedback_reply_data", creatContentValue(info), SQLiteDatabase.CONFLICT_REPLACE);
            mDBUtil.closeDB();
        } finally {
            writeLock.unlock();
        }
    }

    public void create(List<FeedbackReplyEntity> entities) {
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            db.beginTransaction();
            for (FeedbackReplyEntity entity : entities) {
                mDBUtil.insert(db, "cs_feedback_reply_data", creatContentValue(entity), SQLiteDatabase.CONFLICT_REPLACE);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            mDBUtil.closeDB();
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 清除角色表
     *
     * @return
     */
    public int clear() {
        return mDBUtil.delete(mDBUtil.getWritableDatabase(), "cs_feedback_reply_data", null, null);
    }


    /**
     * 查找数据库中特定id角色信息
     *
     * @return
     */
    public ArrayList<FeedbackReplyEntity> findAll(long account_id, long fid) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            ArrayList<FeedbackReplyEntity> s = new ArrayList<FeedbackReplyEntity>();
            Cursor c = null;
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            String sql = "select * from cs_feedback_reply_data where account_id=? and fid=? order by ts desc";
            c = db.rawQuery(sql, new String[]{"" + account_id, "" + fid});
            while (c.moveToNext()) {
                s.add(getContentValue(c));
            }
            c.close();
            mDBUtil.closeDB();
            return s;
        } finally {
            readLock.unlock();
        }
    }

    private ContentValues creatContentValue(FeedbackReplyEntity info) {
        ContentValues values = new ContentValues();
        values.put("id", info.getId());
        values.put("account_id", Account.getInstance(context).getAccountInfo().getId());
        values.put("content", info.getContent());
        values.put("ts", info.getTs());
        values.put("nts", info.getNts());
        values.put("adminid", info.getAdminid());
        values.put("ua", info.getUa());
        values.put("rtype", info.getRtype());
        values.put("auid", info.getAuid());
        values.put("fid", info.getFid());
        return values;
    }

    private FeedbackReplyEntity getContentValue(Cursor c) {
        FeedbackReplyEntity info = new FeedbackReplyEntity();
        info.setId(c.getLong(c.getColumnIndex("id")));
        info.setAccount_id(c.getLong(c.getColumnIndex("account_id")));
        info.setContent(c.getString(c.getColumnIndex("content")));
        info.setTs(c.getLong(c.getColumnIndex("ts")));
        info.setNts(c.getLong(c.getColumnIndex("nts")));
        info.setRtype(c.getString(c.getColumnIndex("rtype")));
        info.setUa(c.getString(c.getColumnIndex("ua")));
        info.setAdminid(c.getLong(c.getColumnIndex("adminid")));
        info.setAuid(c.getString(c.getColumnIndex("auid")));
        info.setFid(c.getLong(c.getColumnIndex("fid")));
        return info;
    }
}
