package com.jq.code.code.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.jq.code.model.PutBase;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.sport.SubmitSportEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * 所有账号所有角色的运动记录表
 *
 * @author hfei
 */
public class SportDB {

    public static final String TABLE_NAME = "cs_sport";
    /**
     * 角色数据建表
     */
    public static final String CREATE_TABLE = "create table if not exists "
            + TABLE_NAME
            + "(_id integer primary key,"
            + "id integer unique null, "
            + "account_id bigint not null, "
            + "role_id bigint not null,"
            + "name text null,"
            + "duration integer null,"
            + "ex_id integer null,"
            + "date date null,"
            + "upload_time date null,"
            + "calory integer null,"
            + "metabolism integer null,"
            + "isdelete integer null,"
            + "mtype varchar(20) null,"
            + "unique(id) on conflict replace)";

    private DB mDBUtil;

    private SportDB(Context context) {
        mDBUtil = DB.getInstance(context.getApplicationContext());
    }

    public static SportDB getInstance(Context context) {
        SportDB instance = new SportDB(context);
        return instance;
    }


    public void create(SubmitSportEntity info) {
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            insert(db, info);
            mDBUtil.closeDB();
        } finally {
            writeLock.unlock();
        }
    }

    public void create(List<SubmitSportEntity> sportEntities) {
        if(null == sportEntities || sportEntities.isEmpty()) {
            return;
        }
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            db.beginTransaction();
            for (int i = 0; i < sportEntities.size(); i++) {
                insert(db, sportEntities.get(i));
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            mDBUtil.closeDB();
        } finally {
            writeLock.unlock();
        }
    }

    private void insert(SQLiteDatabase db, SubmitSportEntity sportEntity) {
        int update_id = mDBUtil.update(db, "cs_sport", creatContentValue(sportEntity), "id=? and role_id=?", new String[]{String.valueOf(sportEntity.getId()), String.valueOf(sportEntity.getRole_id())});
        if (update_id <= 0) {
            long food_id = mDBUtil.insert(db, "cs_sport", creatContentValue(sportEntity),SQLiteDatabase.CONFLICT_REPLACE);
            sportEntity.set_id(food_id);
        }
    }


    public long remove(SubmitSportEntity info) {
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            int flag = mDBUtil.delete(db, "cs_sport", "role_id=? and _id=?", new String[]{String.valueOf(info.getRole_id()), info.get_id() + ""});
            mDBUtil.closeDB();
            return flag;
        } finally {
            writeLock.unlock();
        }
    }

    private void remove(SQLiteDatabase db, SubmitSportEntity info) {
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            mDBUtil.delete(db, "cs_sport", "_id=? and role_id=?", new String[]{info.get_id() + "", String.valueOf(info.getRole_id())});
        } finally {
            writeLock.unlock();
        }
    }

    public void remove(long role_id, long id) {
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            mDBUtil.delete(db, "cs_sport", "role_id=? and id=?", new String[]{String.valueOf(role_id), String.valueOf(id)});
            mDBUtil.closeDB();
        } finally {
            writeLock.unlock();
        }
    }

    public void remove(List<PutBase> putBases) {
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            db.beginTransaction();
            for (PutBase kindInfo : putBases) {
                remove(db, (SubmitSportEntity) kindInfo);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            mDBUtil.closeDB();
        } finally {
            writeLock.unlock();
        }
    }


    /**
     * 修改角色数据信息
     *
     * @param info
     * @return
     */
    public void modify(SubmitSportEntity info) {
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            mDBUtil.update(db, "cs_sport", creatContentValue(info), "_id=? and role_id=?", new String[]{info.get_id() + "", info.getRole_id() + ""});
            mDBUtil.closeDB();
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 获取指定日期的数据
     *
     * @param role_id
     * @param date
     * @return
     */
    public ArrayList<SubmitSportEntity> findBydate(long role_id, String date) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            ArrayList<SubmitSportEntity> infos = new ArrayList<SubmitSportEntity>();
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            Cursor c = null;
            c = db.rawQuery("select * from cs_sport where role_id=? and date=? and isdelete=0 order by upload_time desc", new String[]{role_id + "", date});
            while (c.moveToNext()) {
                infos.add(getContentValue(c));
            }
            c.close();
            mDBUtil.closeDB();
            return infos;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 查询待删除数据
     *
     * @param info
     * @return
     */
    public ArrayList<SubmitSportEntity> findDeleteeDatas(RoleInfo info) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            ArrayList<SubmitSportEntity> infos = new ArrayList<SubmitSportEntity>();
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            Cursor c = null;
            c = db.rawQuery("select * from cs_sport where account_id=? and role_id=? and isdelete=1", new String[]{info.getAccount_id() + "", info.getId() + ""});
            while (c.moveToNext()) {
                infos.add(getContentValue(c));
            }
            c.close();
            mDBUtil.closeDB();
            return infos;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 查找当前角色下，所有未同步的角色数据
     *
     * @return
     */
    public ArrayList<SubmitSportEntity> findUnSyncDatas(RoleInfo info) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            ArrayList<SubmitSportEntity> infos = new ArrayList<SubmitSportEntity>();
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            Cursor c = null;
            c = db.rawQuery("select * from cs_sport where role_id=? and account_id=? and id is null order by upload_time asc", new String[]{info.getId() + "", info.getAccount_id() + ""});
            while (c.moveToNext()) {
                infos.add(getContentValue(c));
            }
            c.close();
            mDBUtil.closeDB();
            return infos;
        } finally {
            readLock.unlock();
        }
    }

    public boolean isEmpty(long role_id, String date) {
        boolean isEmpty = true;
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            isEmpty(db, role_id, date);
            mDBUtil.closeDB();
        } finally {
            readLock.unlock();
        }
        return isEmpty;
    }

    public boolean isEmpty(SQLiteDatabase db, long role_id, String date) {
        boolean isEmpty = true;
        Cursor c = null;
        c = db.rawQuery("select count(*) from cs_sport where role_id=? and date=? and isdelete=0", new String[]{role_id + "", date});
        while (c.moveToNext()) {
            isEmpty = c.getInt(0) == 0;
        }
        c.close();
        return isEmpty;
    }

    private ContentValues creatContentValue(SubmitSportEntity info) {
        ContentValues values = new ContentValues();
        if (info.getId() > 0) {
            values.put("id", info.getId());
        }
        if (info.get_id() > 0) {
            values.put("_id", info.get_id());
        }
        values.put("account_id", info.getAccount_id());
        values.put("role_id", info.getRole_id());
        values.put("metabolism", info.getMetabolism());
        values.put("mtype", info.getMtype());
        values.put("name", info.getName());
        values.put("date", info.getDate());
        values.put("calory", info.getCalory());
        values.put("ex_id", info.getEx_id());
        values.put("duration", info.getDuration());
        values.put("isdelete", info.getDelete());
        values.put("upload_time", info.getUpload_time());
        return values;
    }

    public SubmitSportEntity getContentValue(Cursor c) {
        SubmitSportEntity info = new SubmitSportEntity();
        info.set_id(c.getInt(c.getColumnIndex("_id")));
        info.setId(c.getInt(c.getColumnIndex("id")));
        info.setAccount_id(c.getLong(c.getColumnIndex("account_id")));
        info.setRole_id(c.getLong(c.getColumnIndex("role_id")));
        info.setMetabolism(c.getInt(c.getColumnIndex("metabolism")));
        info.setMtype(c.getString(c.getColumnIndex("mtype")));
        info.setName(c.getString(c.getColumnIndex("name")));
        info.setDuration(c.getInt(c.getColumnIndex("duration")));
        info.setDate(c.getString(c.getColumnIndex("date")));
        info.setEx_id(c.getInt(c.getColumnIndex("ex_id")));
        info.setCalory(c.getInt(c.getColumnIndex("calory")));
        info.setDelete(c.getInt(c.getColumnIndex("isdelete")));
        info.setUpload_time(c.getString(c.getColumnIndex("upload_time")));
        return info;
    }

}
