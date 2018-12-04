package com.jq.code.code.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.jq.code.model.PutBase;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.sport.SubmitFoodEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * 所有账号所有角色的饮食记录表
 *
 * @author hfei
 */
public class FoodDB {

    public static final String TABLE_NAME = "cs_food";
    /**
     * 角色数据建表
     */
    public static final String CREATE_TABLE = "create table if not exists "
            + TABLE_NAME
            + "(_id integer primary key,"
            + "id integer unique null, "
            + "account_id bigint not null, "
            + "role_id bigint not null,"
            + "name text not null,"
            + "quantity float null,"
            + "food_id integer null,"
            + "date date null,"
            + "upload_time date null,"
            + "unit text null,"
            + "calory integer null,"
            + "metabolism integer null,"
            + "isdelete integer null,"
            + "ftype text null,"
            + "mtype text null,"
            + "unique(id) on conflict replace)";

    private DB mDBUtil;
    private FoodDB(Context context) {
        mDBUtil = DB.getInstance(context.getApplicationContext());
    }

    public static FoodDB getInstance(Context context) {
        FoodDB instance = new FoodDB(context);
        return instance;
    }

    public void create(SubmitFoodEntity info) {
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

    public void create(List<SubmitFoodEntity> bPressEntities) {
        if(null == bPressEntities || bPressEntities.isEmpty()) {
            return;
        }
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            db.beginTransaction();
            for (int i = 0; i < bPressEntities.size(); i++) {
                insert(db, bPressEntities.get(i));
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            mDBUtil.closeDB();
        } finally {
            writeLock.unlock();
        }
    }

    private void insert(SQLiteDatabase db, SubmitFoodEntity foodEntity) {
        int update_id = mDBUtil.update(db, "cs_food", creatContentValue(foodEntity), "id=? and role_id=?", new String[]{String.valueOf(foodEntity.getId()), String.valueOf(foodEntity.getRole_id())});
        if(update_id <= 0){
            long food_id = mDBUtil.insert(db, "cs_food", creatContentValue(foodEntity), SQLiteDatabase.CONFLICT_REPLACE);
            foodEntity.set_id(food_id);
        }
    }

    public long remove(SubmitFoodEntity info) {
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            int flag = mDBUtil.delete(db, "cs_food", "role_id=? and _id=?",
                    new String[]{String.valueOf(info.getRole_id()), info.get_id() + ""});
            mDBUtil.closeDB();
            return flag;
        } finally {
            writeLock.unlock();
        }
    }


    public void remove(long role_id, long id) {
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
//            SubmitFoodEntity info = findByServerID(db, role_id, id);
//            if (info == null) return;
            mDBUtil.delete(db, "cs_food", "role_id=? and id=?", new String[]{String.valueOf(role_id), String.valueOf(id)});
            mDBUtil.closeDB();
        } finally {
            writeLock.unlock();
        }
    }

    private void remove(SQLiteDatabase db, SubmitFoodEntity info) {
        mDBUtil.delete(db, "cs_food", "_id=? and role_id=?", new String[]{info.get_id() + "", String.valueOf(info.getRole_id())});
    }

    public void remove(List<PutBase> putBases) {
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            db.beginTransaction();
            for (PutBase kindInfo : putBases) {
                remove(db, (SubmitFoodEntity) kindInfo);
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
    public void modify(SubmitFoodEntity info) {
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            mDBUtil.update(db, "cs_food", creatContentValue(info), "_id=? and role_id=?", new String[]{info.get_id() + "", info.getRole_id() + ""});
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
    public ArrayList<SubmitFoodEntity> findByDate(long role_id, String date) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            ArrayList<SubmitFoodEntity> infos = new ArrayList<SubmitFoodEntity>();
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            Cursor c = null;
            c = db.rawQuery("select * from cs_food where role_id=? and date=? and isdelete=0 order by upload_time desc", new String[]{role_id + "", date});
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
     * 获取指定日期的数据
     *
     * @param role_id
     * @return
     */
    public ArrayList<SubmitFoodEntity> findByDate(long role_id) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            ArrayList<SubmitFoodEntity> infos = new ArrayList<SubmitFoodEntity>();
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            Cursor c = null;
            c = db.rawQuery("select * from cs_food where role_id=? and isdelete=0 order by upload_time desc", new String[]{role_id + ""});
           String data = null;
            while (c.moveToNext()) {
                if(infos.size() > 0){
                    int size = infos.size();
                    if(infos.get(size-1).getDate().equals(getContentValue(c).getDate())){
                        infos.get(size-1).setCalory(infos.get(size-1).getCalory() + getContentValue(c).getCalory());
                    }else {
                        infos.add(getContentValue(c));
                    }
                }else {
                    infos.add(getContentValue(c));
                }
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
    public ArrayList<SubmitFoodEntity> findDeleteeDatas(RoleInfo info) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            ArrayList<SubmitFoodEntity> infos = new ArrayList<SubmitFoodEntity>();
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            Cursor c = null;
            c = db.rawQuery("select * from " + "cs_food where account_id=? and role_id=? and isdelete=1", new String[]{info.getAccount_id() + "", info.getId() + ""});
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
    public ArrayList<SubmitFoodEntity> findUnSyncDatas(RoleInfo info) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            ArrayList<SubmitFoodEntity> infos = new ArrayList<SubmitFoodEntity>();
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            Cursor c = null;
            c = db.rawQuery("select * from " + "cs_food where role_id=? and account_id=? and id is null order by upload_time asc", new String[]{info.getId() + "", info.getAccount_id() + ""});
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
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        boolean isEmpty = true;
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
        c = db.rawQuery("select count(*) from " + "cs_food where role_id=? and date=? and isdelete=0", new String[]{role_id + "", date});
        while (c.moveToNext()) {
            isEmpty = c.getInt(0) == 0;
        }
        c.close();
        return isEmpty;
    }

    private ContentValues creatContentValue(SubmitFoodEntity info) {
        ContentValues values = new ContentValues();
        if (info.get_id() > 0) {
            values.put("_id", info.get_id());
        }
        if (info.getId() > 0) {
            values.put("id", info.getId());
        }
        values.put("account_id", info.getAccount_id());
        values.put("role_id", info.getRole_id());
        values.put("metabolism", info.getMetabolism());
        values.put("mtype", info.getMtype());
        values.put("name", info.getName());
        values.put("quantity", info.getQuantity());
        values.put("date", info.getDate());
        values.put("food_id", info.getFood_id());
        values.put("calory", info.getCalory());
        values.put("unit", info.getUnit());
        values.put("ftype", info.getFtype());
        values.put("isdelete", info.getDelete());
        values.put("upload_time", info.getUpload_time());
        return values;
    }

    private SubmitFoodEntity getContentValue(Cursor c) {
        SubmitFoodEntity info = new SubmitFoodEntity();
        info.set_id(c.getInt(c.getColumnIndex("_id")));
        info.setId(c.getInt(c.getColumnIndex("id")));
        info.setAccount_id(c.getLong(c.getColumnIndex("account_id")));
        info.setRole_id(c.getLong(c.getColumnIndex("role_id")));
        info.setMetabolism(c.getInt(c.getColumnIndex("metabolism")));
        info.setMtype(c.getString(c.getColumnIndex("mtype")));
        info.setName(c.getString(c.getColumnIndex("name")));
        info.setQuantity(c.getFloat(c.getColumnIndex("quantity")));
        info.setDate(c.getString(c.getColumnIndex("date")));
        info.setFood_id(c.getInt(c.getColumnIndex("food_id")));
        info.setCalory(c.getInt(c.getColumnIndex("calory")));
        info.setUnit(c.getString(c.getColumnIndex("unit")));
        info.setFtype(c.getString(c.getColumnIndex("ftype")));
        info.setDelete(c.getInt(c.getColumnIndex("isdelete")));
        info.setUpload_time(c.getString(c.getColumnIndex("upload_time")));
        return info;
    }
}
