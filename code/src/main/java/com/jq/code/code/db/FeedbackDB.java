package com.jq.code.code.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jq.code.code.business.Account;
import com.jq.code.model.FeedbackEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * 用户反馈信息
 */
public class FeedbackDB {
    public static final String TABLE_NAME = "cs_feedback_data";
    /**
     * 角色建表
     */
    public static final String CREATE_TABLE = "create table if not exists "
            + TABLE_NAME
            + "(id bigint UNIQUE, "
            + "account_id bigint, "
            + "content text, "
            + "ts bigint,"
            + "nts bigint,"
            + "appid text,"
            + "ua text,"
            + "status text,"
            + "auid text,"
            + "reply integer,"
            + "unique(id) on conflict replace)";

    private DB mDBUtil;
    private Context context;

    /**
     * 析构函数
     */
    private FeedbackDB(Context context) {
        this.context = context.getApplicationContext();
        mDBUtil = DB.getInstance(context.getApplicationContext());
    }

    /**
     * 得到当前实体类
     */
    public static FeedbackDB getInstance(Context context) {
        FeedbackDB instance = new FeedbackDB(context);
        return instance;
    }

    /**
     * 添加角色信息
     *
     * @param info
     * @return
     */
    public void create(FeedbackEntity info) {
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            mDBUtil.insert(db, "cs_feedback_data", creatContentValue(info), SQLiteDatabase.CONFLICT_REPLACE);
            mDBUtil.closeDB();
        } finally {
            // 一定要在finally子句调用解锁，否则一直锁住
            writeLock.unlock();
        }
    }

    public void create(ArrayList<FeedbackEntity> entities) {
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            db.beginTransaction();
            for (FeedbackEntity entity : entities) {
                mDBUtil.insert(db, "cs_feedback_data", creatContentValue(entity), SQLiteDatabase.CONFLICT_REPLACE);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            mDBUtil.closeDB();
        } finally {
            // 一定要在finally子句调用解锁，否则一直锁住
            writeLock.unlock();
        }
    }

    /**
     * 清除角色表
     *
     * @return
     */
    public int clear() {
        return mDBUtil.delete(mDBUtil.getWritableDatabase(), "cs_feedback_data", null, null);
    }

    /**
     * 修改角色信息
     *
     * @param info
     * @return
     */
    public void modify(FeedbackEntity info) {
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            mDBUtil.update(db, "cs_feedback_data", creatContentValue(info), "account_id=? and id=?",
                    new String[]{String.valueOf(info.getAccount_id()), String.valueOf(info.getId())});
            mDBUtil.closeDB();
        } finally {
            // 一定要在finally子句调用解锁，否则一直锁住
            writeLock.unlock();
        }
    }


    public void modify(List<FeedbackEntity> entities) {
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            db.beginTransaction();
            for (FeedbackEntity entity : entities) {
                mDBUtil.update(db, "cs_feedback_data", creatContentValue(entity), "account_id=? and id=?",
                        new String[]{String.valueOf(entity.getAccount_id()), String.valueOf(entity.getId())});
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            mDBUtil.closeDB();
        } finally {
            // 一定要在finally子句调用解锁，否则一直锁住
            writeLock.unlock();
        }
    }


    /**
     * 查找数据库中特定id角色信息
     *
     * @return
     */
    public ArrayList<FeedbackEntity> find(long account_id, long endts, int cnt) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            ArrayList<FeedbackEntity> s = new ArrayList<FeedbackEntity>();
            Cursor c = null;
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            String sql = "select * from cs_feedback_data where account_id=? and ts<" + endts + " order by ts desc limit " + cnt;
            c = db.rawQuery(sql, new String[]{"" + account_id});
            while (c.moveToNext()) {
                s.add(getContentValue(c));
            }
            findReply(s);
            c.close();
            mDBUtil.closeDB();
            return s;
        } finally {
            readLock.unlock();
        }
    }


    public FeedbackEntity find(int account_id, long id) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            FeedbackEntity s = null;
            Cursor c = null;
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            String sql = "select * from cs_feedback_data where account_id=? and id=?";
            c = db.rawQuery(sql, new String[]{"" + account_id, "" + id});
            while (c.moveToNext()) {
                s = getContentValue(c);
            }
            c.close();
            mDBUtil.closeDB();
            return s;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 查找数据库中特定id角色信息
     *
     * @return
     */
    public ArrayList<FeedbackEntity> findAll(long account_id) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            ArrayList<FeedbackEntity> s = new ArrayList<FeedbackEntity>();
            Cursor c = null;
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            String sql = "select * from cs_feedback_data where account_id=? order by ts desc";
            c = db.rawQuery(sql, new String[]{"" + account_id});
            while (c.moveToNext()) {
                s.add(getContentValue(c));
            }
            findReply(s);
            c.close();
            mDBUtil.closeDB();
            return s;
        } finally {
            readLock.unlock();
        }
    }

    private void findReply(ArrayList<FeedbackEntity> entities) {
        for (FeedbackEntity entity : entities) {
            entity.setReplies(FeedbackReplyDB.getInstance(context).findAll(entity.getAccount_id(), entity.getId()));
        }
    }

    private ContentValues creatContentValue(FeedbackEntity info) {
        ContentValues values = new ContentValues();
        values.put("id", info.getId());
        values.put("account_id", Account.getInstance(context).getAccountInfo().getId());
        values.put("content", info.getContent());
        values.put("ts", info.getTs());
        values.put("nts", info.getNts());
        values.put("appid", info.getAppid());
        values.put("ua", info.getUa());
        values.put("status", info.getStatus());
        values.put("auid", info.getAuid());
        values.put("reply", info.getReply());
        return values;
    }

    private FeedbackEntity getContentValue(Cursor c) {
        FeedbackEntity info = new FeedbackEntity();
        info.setId(c.getLong(c.getColumnIndex("id")));
        info.setAccount_id(c.getLong(c.getColumnIndex("account_id")));
        info.setContent(c.getString(c.getColumnIndex("content")));
        info.setTs(c.getLong(c.getColumnIndex("ts")));
        info.setNts(c.getLong(c.getColumnIndex("nts")));
        info.setAppid(c.getString(c.getColumnIndex("appid")));
        info.setUa(c.getString(c.getColumnIndex("ua")));
        info.setStatus(c.getString(c.getColumnIndex("status")));
        info.setAuid(c.getString(c.getColumnIndex("auid")));
        info.setReply(c.getInt(c.getColumnIndex("reply")));
        return info;
    }
}
