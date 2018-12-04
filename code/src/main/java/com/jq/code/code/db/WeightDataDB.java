package com.jq.code.code.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jq.code.code.util.TimeUtil;
import com.jq.code.model.PutBase;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.WeightEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * 所有账号所有角色的体重、体脂数据
 *
 * @author hfei
 */
public class WeightDataDB {

    public static final String TABLE_NAME = "cs_role_data";
    /**
     * 角色数据建表
     */
    public static final String CREATE_TABLE_ROLE_DATA = "create table if not exists "
            + TABLE_NAME
            + " (id bigint not null, "
            + "weight float null,"
            + "weight_time date not null,"
            + "bmi float null,"
            + "axunge float null,"
            + "bone integer null,"
            + "muscle float null,"
            + "water float null,"
            + "metabolism float null,"
            + "body_age float null,"
            + "viscera float null,"
            + "r1 float null,"
            + "account_id bigint not null, "
            + "role_id bigint not null,"
            + "sync_time date null,"
            + "isdelete integer not null,"
            + "scaleweight varchar(20) null,"
            + "scaleproperty integer null,"
            + "productid integer not null,"
            + "mtype varchar(20) null,"
            + "weight_date varchar(20),"
            + "score integer null,"
            + "bw float null,"
            + "height integer null,"
            + "sex integer null,"
            + "age integer null,"
            + "rn8 text,"
            + "primary key(role_id,weight_time) on conflict replace)";

    private DB mDBUtil;
    private Context mContext;
    private WeightDataDB(Context context) {
        mContext = context.getApplicationContext();
        mDBUtil = DB.getInstance(mContext);
    }

    public static WeightDataDB getInstance(Context context) {
        WeightDataDB instance = new WeightDataDB(context);
        return instance;
    }

    public void create(PutBase info) {
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            insert(db, (WeightEntity) info);
            mDBUtil.closeDB();
        } finally {
            writeLock.unlock();
        }
    }

    public void create(List<PutBase> putBases) {
        if(null == putBases || putBases.isEmpty()) {
            return;
        }
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            db.beginTransaction();
            for (PutBase kindInfo : putBases) {
                insert(db, (WeightEntity) kindInfo);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            mDBUtil.closeDB();
        } finally {
            writeLock.unlock();
        }
    }

    private void insert(SQLiteDatabase db, WeightEntity info) {
        db.insertWithOnConflict("cs_role_data", null, creatContentValue(info), SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void remove(PutBase putBase) {
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            remove(db, putBase);
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
                remove(db, kindInfo);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            mDBUtil.closeDB();
        } finally {
            writeLock.unlock();
        }
    }

    public int remove(SQLiteDatabase db, PutBase info) {
        int flag = mDBUtil.delete(db, "cs_role_data", "role_id=? and weight_time=?", new String[]{String.valueOf(info.getRole_id()), info.getMeasure_time()});
        return flag;
    }

    public void remove(long role_id, long id) {
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            mDBUtil.delete(db, "cs_role_data", "role_id=? and id=?", new String[]{String.valueOf(role_id), String.valueOf(id)});
            mDBUtil.closeDB();
        } finally {
            writeLock.unlock();
        }
    }

    public void remove(long role_id, List<Long> ids) {
        if (ids == null || ids.isEmpty()) return;
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            db.beginTransaction();
            for (long id : ids) {
                mDBUtil.delete(db, "cs_role_data", "role_id=? and id=?", new String[]{String.valueOf(role_id), String.valueOf(id)});
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            mDBUtil.closeDB();
        } finally {
            writeLock.unlock();
        }
    }

    public void removeRoleDataByRoleId(int role_id) {
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            mDBUtil.delete(db, "cs_role_data", "role_id=?", new String[]{role_id + ""});
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
    public void modify(WeightEntity info) {
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            Cursor c = db.rawQuery("select isdelete from cs_role_data where weight_time=? and role_id=?", new String[]{info.getWeight_time(), info.getRole_id() + ""});
            int isdelete = -1;
            while (c.moveToNext()) {
                isdelete = c.getInt(0);
            }
            if (isdelete == 1) {
                info.setDelete(1);
            }
            mDBUtil.update(db, "cs_role_data", creatContentValue(info), "weight_time=? and role_id=?", new String[]{info.getWeight_time(), info.getRole_id() + ""});
            c.close();
            mDBUtil.closeDB();
        } finally {
            writeLock.unlock();
        }
    }

    public void setDeleted(WeightEntity entity) {
        Lock writeLock = mDBUtil.getWriteLock();
        writeLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();

            mDBUtil.update(db, "cs_role_data", creatContentValue(entity), "weight_time=? and role_id=?", new String[]{entity.getWeight_time(), entity.getRole_id() + ""});
            mDBUtil.closeDB();
        } finally {
            writeLock.unlock();
        }
    }

    public List<WeightEntity> loadWeightData(long account_id, long role_id, String mtype, int count, int start) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            List<WeightEntity> result = new ArrayList<>();
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            Cursor c = null;
            c = db.rawQuery("select * from cs_role_data where account_id=? and role_id=? and isdelete=0 and mtype=?" +
                    "order by weight_time desc limit ? offset ?",
                    new String[]{account_id + "", role_id + "", mtype, count + "", start + ""});
            while (c.moveToNext()) {
                result.add(getContentValue(c));
            }
            c.close();
            mDBUtil.closeDB();
            return result;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 获取最近的几条体重数据
     * @param account_id 账号id
     * @param role_id 角色id
     * @param count 条数
     * @return 几条体重数据
     */
    public List<WeightEntity> loadLatestWeight(long account_id, long role_id, int count) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            List<WeightEntity> result = new ArrayList<>();
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            Cursor c = null;
            c = db.rawQuery("select * from cs_role_data where account_id=? and role_id=? and isdelete=0 " +
                            "order by weight_time desc limit ?",
                    new String[]{account_id + "", role_id + "", count + ""});
            while (c.moveToNext()) {
                result.add(getContentValue(c));
            }
            c.close();
            mDBUtil.closeDB();
            return result;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 查询指定角色指定时间的数据
     *
     * @return
     */
    public WeightEntity findRoleDataByRoleIdAndTime(long role_id, String dataTime) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            WeightEntity info = null;
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            Cursor c = null;
            c = db.rawQuery("select * from " + "cs_role_data where role_id=" + role_id + " and weight_time=datetime('" + dataTime + "') and isdelete=0",
                    null);
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

    /**
     * 获取传入时间的上一天数据
     *
     * @param time
     * @return
     */
    public WeightEntity findLastRoleDataByTimeAndRoleId(String time, long role_id) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            WeightEntity info = null;
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            Cursor c = null;
            c = db.rawQuery("select * from cs_role_data" + " where " + "role_id=" + role_id + " and weight_time<datetime('"
                    + time + "')" + " order by weight_time desc limit 0,1", null);
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

    /**
     * 获取传最新的基础代谢值
     *
     * @param role_id
     * @return
     */
    public float findLastMetabolism(long role_id) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            float metabolism = 0;
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            Cursor c = null;
            c = db.rawQuery("select metabolism from cs_role_data where role_id=? and metabolism>0 order by weight_time desc limit 0,1", new String[]{role_id + ""});
            while (c.moveToNext()) {
                metabolism = c.getFloat(0);
            }
            c.close();
            mDBUtil.closeDB();
            return metabolism;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 获取传入时间的下一天数据
     *
     * @param time
     * @return
     */
    public WeightEntity findNextRoleDataByTimeAndRoleId(String time, long role_id) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            WeightEntity info = null;
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            Cursor c = null;
            c = db.rawQuery("select * from cs_role_data" + " where " + "role_id=" + role_id + " and weight_time>datetime('"
                    + time + "')" + " order by weight_time asc limit 0,1", null);
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


    /**
     * 查找数据库中特定id设备信息
     *
     * @return
     */
    public WeightEntity findLastRoleDataByRoleId(RoleInfo roleInfo) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            WeightEntity info = null;
            Cursor c = null;
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            String sql = "select * from cs_role_data where isdelete=0 and account_id=? and role_id=? order by weight_time desc limit 0,1";
            c = db.rawQuery(sql, new String[]{String.valueOf(roleInfo.getAccount_id()), String.valueOf(roleInfo.getId())});
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

    /**
     * 查找当前角色下，所有未同步的角色数据
     *
     * @return
     */
    public ArrayList<PutBase> find(long account_id, long role_id, int count, String endTime) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            ArrayList<PutBase> infos = new ArrayList<PutBase>();
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            Cursor c = null;
            c = db.rawQuery("select * from cs_role_data where account_id=? and role_id=? and isdelete=0 and weight_time<? " +
                    "order by weight_time desc limit " + count, new String[]{account_id + "", role_id + "", endTime});
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
    public ArrayList<WeightEntity> findDeleteeDatas(RoleInfo info) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            ArrayList<WeightEntity> infos = new ArrayList<WeightEntity>();
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            Cursor c = null;
            c = db.rawQuery("select * from " + "cs_role_data where account_id=? and role_id=? and isdelete=1", new String[]{info.getAccount_id() + "", info.getId() + ""});
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
    public ArrayList<WeightEntity> findRoleData(RoleInfo info) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            ArrayList<WeightEntity> infos = new ArrayList<WeightEntity>();
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            Cursor c = null;
            c = db.rawQuery("select * from " + "cs_role_data where account_id=? and role_id=? and isdelete=0 and sex!=2 order by weight_time desc", new String[]{info.getAccount_id() + "", info.getId() + ""});
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
    public ArrayList<WeightEntity> findUnSyncDatas(RoleInfo info) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            ArrayList<WeightEntity> infos = new ArrayList<WeightEntity>();
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            Cursor c = null;
            // id=0 代表该条数据没上传到服务器
            c = db.rawQuery("select * from " + "cs_role_data where role_id=? and account_id=? and id=0", new String[]{info.getId() + "", info.getAccount_id() + ""});
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
     * 查询表中一段时间的数据
     *
     * @param role_id
     * @param startTime
     * @param endTime
     * @return
     */
    public WeightEntity getAvg(long account_id, long role_id, String startTime, String endTime) {
        String[] types = new String[]{"weight", "bmi", "axunge", "bone", "muscle", "water", "metabolism", "viscera"};
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            float avg = 0;
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            WeightEntity entity = new WeightEntity();
            Cursor c = null;
            for (String type : types) {
                String sql = "select avg(" + type + ") from cs_role_data where isdelete=0 and " + type + ">0 and account_id=? and weight_time Between '" + startTime + "' and  '" + endTime
                        + "' and role_id=?";
                c = db.rawQuery(sql, new String[]{account_id + "", role_id + ""});
                c.moveToFirst();
                avg = (Math.round(c.getFloat(0) * 10)) / 10f;
                if (type.equals(types[0]) && avg == 0) {
                    entity = null;
                    break;
                } else {
                    if (type.equals(types[0])) {
                        entity.setWeight(avg);
                    } else if (type.equals(types[1])) {
                        entity.setBmi(avg);
                    } else if (type.equals(types[2])) {
                        entity.setAxunge(avg);
                    } else if (type.equals(types[3])) {
                        entity.setBone(avg);
                    } else if (type.equals(types[4])) {
                        entity.setMuscle(avg);
                    } else if (type.equals(types[5])) {
                        entity.setWater(avg);
                    } else if (type.equals(types[6])) {
                        entity.setMetabolism(avg);
                    } else if (type.equals(types[7])) {
                        entity.setViscera(avg);
                    }
                }
            }
            if (entity != null) {
                entity.setAccount_id(account_id);
                entity.setRole_id(role_id);
                entity.setWeight_time(endTime.split(" ")[0]);
            }
            c.close();
            mDBUtil.closeDB();
            return entity;
        } finally {
            readLock.unlock();
        }
    }
    /**
     * 查找数据库中一天的角色数据信息
     *
     * @return
     */
    public ArrayList<WeightEntity> findDayRoleDataAllByRoleIdAndTime(RoleInfo info, String time) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            ArrayList<WeightEntity> infos = new ArrayList<WeightEntity>();
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            Cursor c = null;
            String sql = "select * from cs_role_data where account_id=" + info.getAccount_id() + " and isdelete=0 and role_id=" + info.getId()
                    + " and weight_time like '" + time
                    + "%' order by weight_time desc";
            c = db.rawQuery(sql, null);
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
     * 获取连续称重天数
     *
     * @param roleInfo
     * @return
     */
    public int[] getSeriesWeighDays(RoleInfo roleInfo) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            Cursor c = db.rawQuery("select weight_date from cs_role_data where isdelete=0 and account_id=? and role_id=? group by weight_date order by weight_date desc",
                    new String[]{String.valueOf(roleInfo.getAccount_id()), String.valueOf(roleInfo.getId())});
            String lastDate = TimeUtil.getCurDate(TimeUtil.TIME_FORMAT7);
            int[] seriesDays = new int[]{0, -1};
            while (c.moveToNext()) {
                String date = c.getString(0);
                int days = (int) Math.abs(TimeUtil.getGapDays(TimeUtil.TIME_FORMAT7, date, lastDate));
                if (seriesDays[1] == -1) {
                    seriesDays[1] = days;
                }
                if (date.equals(TimeUtil.getCurDate(TimeUtil.TIME_FORMAT7))) {
                    seriesDays[0] = 1;
                }
                if (!lastDate.equals(date)) {
                    if (days == 1) {
                        seriesDays[0]++;
                    } else {
                        break;
                    }
                }
                lastDate = date;
            }
            c.close();
            mDBUtil.closeDB();
            return seriesDays;
        } finally {
            readLock.unlock();
        }
    }

    public long getCount(RoleInfo info) {
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            Cursor c = null;
            String sql = "select count(*)from cs_type_data where isdelete=0 and account_id=? and role_id=?";
            c = db.rawQuery(sql, new String[]{info.getAccount_id() + "", info.getId() + ""});
            c.moveToFirst();
            long count = c.getLong(0);
            c.close();
            mDBUtil.closeDB();
            return count;
        } finally {
            readLock.unlock();
        }
    }

    private ContentValues creatContentValue(WeightEntity info) {
        ContentValues values = new ContentValues();
        values.put("id", info.getId());
        values.put("weight", info.getWeight());
        values.put("weight_time", info.getWeight_time());
        values.put("bmi", info.getBmi());
        values.put("axunge", info.getAxunge());
        values.put("bone", info.getBone());
        values.put("muscle", info.getMuscle());
        values.put("water", info.getWater());
        values.put("metabolism", info.getMetabolism());
        values.put("body_age", info.getBody_age());
        values.put("viscera", info.getViscera());
        values.put("account_id", info.getAccount_id());
        values.put("role_id", info.getRole_id());
        values.put("sync_time", info.getSync_time());
        values.put("isdelete", info.getDelete());
        values.put("scaleweight", info.getScaleweight());
        values.put("scaleproperty", info.getScaleproperty());
        values.put("productid", info.getProductid());
        values.put("mtype", info.getMtype());
        values.put("weight_date", TimeUtil.dateFormatChange(info.getWeight_time(), TimeUtil.TIME_FORMAT1, "yyyyMMdd"));
        values.put("r1", info.getR1());
        values.put("score", info.getScore());
        values.put("bw", info.getBw());
        values.put("height", info.getHeight());
        values.put("sex", info.getSex());
        values.put("age", info.getAge());
        values.put("rn8", info.getRn8());
        return values;
    }

    private WeightEntity getContentValue(Cursor c) {
        WeightEntity info = new WeightEntity();
        info.setId(c.getLong(c.getColumnIndex("id")));
        info.setWeight(c.getFloat(c.getColumnIndex("weight")));
        info.setWeight_time(c.getString(c.getColumnIndex("weight_time")));
        info.setBmi(c.getFloat(c.getColumnIndex("bmi")));
        info.setAxunge(c.getFloat(c.getColumnIndex("axunge")));
        info.setBone(c.getFloat(c.getColumnIndex("bone")));
        info.setMuscle(c.getFloat(c.getColumnIndex("muscle")));
        info.setWater(c.getFloat(c.getColumnIndex("water")));
        info.setMetabolism(c.getFloat(c.getColumnIndex("metabolism")));
        info.setBody_age(c.getFloat(c.getColumnIndex("body_age")));
        info.setViscera(c.getFloat(c.getColumnIndex("viscera")));
        info.setAccount_id(c.getLong(c.getColumnIndex("account_id")));
        info.setRole_id(c.getLong(c.getColumnIndex("role_id")));
        info.setSync_time(c.getString(c.getColumnIndex("sync_time")));
        info.setDelete(c.getInt(c.getColumnIndex("isdelete")));
        info.setScaleweight(c.getString(c.getColumnIndex("scaleweight")));
        info.setScaleproperty((byte) c.getInt(c.getColumnIndex("scaleproperty")));
        info.setProductid(c.getInt(c.getColumnIndex("productid")));
        info.setMtype(c.getString(c.getColumnIndex("mtype")));
        info.setR1(c.getFloat(c.getColumnIndex("r1")));
        info.setScore(c.getInt(c.getColumnIndex("score")));
        info.setBw(c.getFloat(c.getColumnIndex("bw")));
        info.setHeight(c.getInt(c.getColumnIndex("height")));
        info.setSex(c.getInt(c.getColumnIndex("sex")));
        info.setAge(c.getInt(c.getColumnIndex("age")));
        info.setRn8(c.getString(c.getColumnIndex("rn8")));
        return info;
    }
}
