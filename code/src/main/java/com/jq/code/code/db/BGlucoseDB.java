package com.jq.code.code.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jq.code.code.util.TimeUtil;
import com.jq.code.model.BGlucoseEntity;
import com.jq.code.model.DataType;
import com.jq.code.model.PutBase;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.trend.BGlucoseTrend;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有账号所有角色的血糖数据的表
 * */
public class BGlucoseDB extends DataTypeSetDB {
    public static final String TABLE_NAME = "cs_bgl_data";
    /**
     * 角色建表
     */
    public static final String CREATE_TABLE_BGLUCOSE = "create table if not exists "
            + TABLE_NAME
            + "(id bigint not null, "
            + "account_id integer not null, "
            + "role_id integer not null,"
            + "mtype varchar(32),"
            + "measure_time date not null,"
            + "description integer,"
            + "bsl float null,"
            + "isdelete integer not null,"
            + "measure_date varchar(20),"
            + "primary key(role_id,measure_time) on conflict ignore)";

    /**
     * 析构函数
     */
    private BGlucoseDB(Context context) {
        super(context.getApplicationContext());
    }

    /**
     * 得到当前实体类
     */
    public static BGlucoseDB getInstance(Context context) {
        BGlucoseDB instance = new BGlucoseDB(context);
        return instance;
    }

    public void create(PutBase info) {
        synchronized (mDBUtil) {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            super.create(db, info);
            mDBUtil.insert(db, "cs_bgl_data", creatContentValue((BGlucoseEntity) info), SQLiteDatabase.CONFLICT_IGNORE);
            
        }
    }

    public void create(List<PutBase> bPressEntities) {
        synchronized (mDBUtil) {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            db.beginTransaction();
            for (PutBase bPressEntity : bPressEntities) {
                mDBUtil.insert(db, "cs_bgl_data", creatContentValue((BGlucoseEntity) bPressEntity), SQLiteDatabase.CONFLICT_IGNORE);
                super.create(db, bPressEntity);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }
    }

    public void remove(PutBase putBase) {
        synchronized (mDBUtil) {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            remove(db, putBase);
            db.close();
        }
    }

    public void remove(List<PutBase> putBases) {
        synchronized (mDBUtil) {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            db.beginTransaction();
            for (PutBase kindInfo : putBases) {
                remove(db, kindInfo);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }
    }

    public void remove(long role_id, long id) {
        synchronized (mDBUtil) {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            super.remove(db, role_id, id, DataType.BSL.getType());
            mDBUtil.delete(db, "cs_bgl_data", "role_id=? and id=?",
                    new String[]{String.valueOf(role_id), String.valueOf(id)});
            db.close();
        }
    }

    @Override
    public int remove(SQLiteDatabase db, PutBase info) {
        super.remove(db, info);
        return mDBUtil.delete(db, "cs_bgl_data", "measure_time=? and role_id=?",
                new String[]{info.getMeasure_time(), String.valueOf(info.getRole_id())});
    }

    public void remove(RoleInfo info) {
        synchronized (mDBUtil) {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            super.remove(db, info.getId(),DataType.BSL.getType());
            mDBUtil.delete(db, "cs_bgl_data", "account_id=? and role_id=?",
                    new String[]{info.getAccount_id() + "", String.valueOf(info.getId())});
            db.close();
        }
    }


    public int clear() {
        return mDBUtil.delete(mDBUtil.getWritableDatabase(), "cs_bgl_data", null, null);
    }

    public int modify(PutBase info) {
        synchronized (mDBUtil) {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            super.modify(db, info);
            mDBUtil.update(db, "cs_bgl_data", creatContentValue((BGlucoseEntity) info), "measure_time=? and role_id=?",
                    new String[]{info.getMeasure_time(), String.valueOf(info.getRole_id())});
            db.close();
            return 0;
        }
    }


    public BGlucoseEntity findBGlucose(long role_id, String measure_time) {
        synchronized (mDBUtil) {
            BGlucoseEntity info = null;
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            Cursor c = null;
            c = db.rawQuery(
                    "select * from " + "cs_bgl_data where isdelete=0 and role_id=? and measure_time=? and isdelete=0",
                    new String[]{role_id + "", measure_time});
            while (c.moveToNext()) {
                info = getContentValue(c);
            }
            c.close();
            db.close();
            return info;
        }
    }

    /**
     * 查找数据库中特定id设备信息
     *
     * @return
     */
    public BGlucoseEntity findBGlucoseLastest(RoleInfo roleInfo) {
        synchronized (mDBUtil) {
            BGlucoseEntity info = null;
            Cursor c = null;
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            String sql = "select * from cs_bgl_data where isdelete=0 and role_id=? order by measure_time desc limit 0,1";
            c = db.rawQuery(sql, new String[]{"" + roleInfo.getId()});

            while (c.moveToNext()) {
                info = getContentValue(c);
            }
            c.close();
            db.close();
            return info;
        }
    }

    /**
     * 获取传入数据的下一条数据
     *
     * @param bPressEntity
     * @return
     */
    public BGlucoseEntity findLastBGlucoseFor(BGlucoseEntity bPressEntity) {
        synchronized (mDBUtil) {
            BGlucoseEntity info = null;
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            Cursor c = null;
            c = db.rawQuery("select * from cs_bgl_data" + " where isdelete=0 and "
                    + "role_id=" + bPressEntity.getRole_id() + " and measure_time<datetime('"
                    + bPressEntity.getMeasure_time() + "')" + " order by measure_time desc limit 0,1", null);
            while (c.moveToNext()) {
                info = getContentValue(c);
            }
            c.close();
            db.close();
            return info;
        }
    }

    /**
     * 获取传入数据的上一条数据
     *
     * @param bPressEntity
     * @return
     */
    public BGlucoseEntity findNextBGlucoseFor(BGlucoseEntity bPressEntity) {
        synchronized (mDBUtil) {
            BGlucoseEntity info = null;
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            Cursor c = null;
            c = db.rawQuery("select * from cs_bgl_data" + " where isdelete=0 and "
                    + "role_id=" + bPressEntity.getRole_id() + " and measure_time>datetime('"
                    + bPressEntity.getMeasure_time() + "')" + " order by measure_time asc limit 0,1", null);
            while (c.moveToNext()) {
                info = getContentValue(c);
            }
            c.close();
            db.close();
            return info;
        }
    }

    public boolean isExist(PutBase info) {
        synchronized (mDBUtil) {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            Cursor c = null;
            String sql = "select * from " + "cs_bgl_data"
                    + " where account_id=? and role_id=? and measure_time=?";
            c = db.rawQuery(sql, new String[]{info.getAccount_id() + "", info.getRole_id() + "", info.getMeasure_time()});
            boolean isExist = c.getCount() != 0;
            c.close();
            return isExist;
        }
    }

    /**
     * 查找当前角色下，所有未同步的角色数据
     *
     * @return
     */
    public ArrayList<PutBase> find(long account_id, long role_id, int count, String endTime) {
        synchronized (mDBUtil) {
            ArrayList<PutBase> infos = new ArrayList<PutBase>();
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            Cursor c = null;
            c = db.rawQuery("select * from cs_bgl_data where account_id=? and role_id=? and isdelete=0 and measure_time<? " +
                    "order by measure_time desc limit " + count, new String[]{account_id + "", role_id + "", endTime});
            while (c.moveToNext()) {
                infos.add(getContentValue(c));
            }
            c.close();
            db.close();
            return infos;
        }
    }

    /**
     * 查询表中一段时间的数据
     *
     * @param role_id
     * @param startTime
     * @param endTime
     * @return int[] max(bsl),min(bsl)
     */
    public List<BGlucoseTrend> finPressLimit(int role_id, int account_id, String startTime,
                                             String endTime) {

        List<BGlucoseTrend> lstRet = new ArrayList<BGlucoseTrend>();
        synchronized (mDBUtil) {
            float[] limit = new float[2];
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            Cursor c = null;
            String sql = "select count(*), max(bsl) ,min(bsl),measure_date from cs_bgl_data where isdelete=0 and measure_time Between '" + startTime + "' and  '" + endTime
                    + "' and role_id=" + role_id + " group by measure_date";
            c = db.rawQuery(sql, null);
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    if (c.getLong(0) > 0) {
                        BGlucoseTrend bGlucoseTrend = new BGlucoseTrend();
                        bGlucoseTrend.setMaxBsl(c.getFloat(1));
                        bGlucoseTrend.setMinBsl(c.getFloat(2));
                        bGlucoseTrend.setAccount_id(account_id);
                        bGlucoseTrend.setRole_id(role_id);
                        bGlucoseTrend.setDate(TimeUtil.getTimestamp(c.getString(3), TimeUtil.TIME_FORMAT2));
                        bGlucoseTrend.setNums(c.getLong(0));
                        lstRet.add(bGlucoseTrend);
                    }
                }
            }
            c.close();
            db.close();
            return lstRet;
        }
    }

    public List<BGlucoseTrend> finPressLimitCount(int role_id, int account_id, String endTime ,int count) {

        List<BGlucoseTrend> lstRet = new ArrayList<BGlucoseTrend>();
        synchronized (mDBUtil) {
            float[] limit = new float[2];
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            Cursor c = null;
            String sql = "select count(*), max(bsl) ,min(bsl),measure_date from cs_bgl_data" +
                    " where account_id=? and role_id=? and isdelete=0 and measure_time<? group by measure_date order by measure_time desc limit ?";
            c = db.rawQuery(sql, new String[]{account_id+ "",role_id + "",endTime,count+""});
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    if (c.getLong(0) > 0) {
                        BGlucoseTrend bGlucoseTrend = new BGlucoseTrend();
                        bGlucoseTrend.setMaxBsl(c.getFloat(1));
                        bGlucoseTrend.setMinBsl(c.getFloat(2));
                        bGlucoseTrend.setAccount_id(account_id);
                        bGlucoseTrend.setRole_id(role_id);
                        bGlucoseTrend.setDate(TimeUtil.getTimestamp(c.getString(3), TimeUtil.TIME_FORMAT2));
                        bGlucoseTrend.setNums(c.getLong(0));
                        lstRet.add(bGlucoseTrend);
                    }
                }
            }
            c.close();
            db.close();
            return lstRet;
        }
    }
    /**
     * 查找当前角色下，所有未同步的角色数据
     *
     * @return
     */
    public ArrayList<BGlucoseEntity> findUnSyncDatas(RoleInfo info) {
        synchronized (mDBUtil) {
            ArrayList<BGlucoseEntity> infos = new ArrayList<BGlucoseEntity>();
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            Cursor c = null;
            c = db.rawQuery("select * from " + "cs_bgl_data where role_id=? and account_id=? and id=0", new String[]{info.getId() + "", info.getAccount_id() + ""});
            while (c.moveToNext()) {
                infos.add(getContentValue(c));
            }
            c.close();
            db.close();
            return infos;
        }
    }

    /**
     * 查询待删除数据
     *
     * @param info
     * @return
     */
    public ArrayList<BGlucoseEntity> findDeleteeDatas(RoleInfo info) {
        synchronized (mDBUtil) {
            ArrayList<BGlucoseEntity> infos = new ArrayList<BGlucoseEntity>();
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            Cursor c = null;
            c = db.rawQuery("select * from " + "cs_bgl_data where account_id=? and role_id=? and isdelete=1", new String[]{info.getAccount_id() + "", info.getId() + ""});
            while (c.moveToNext()) {
                infos.add(getContentValue(c));
            }
            c.close();
            db.close();
            return infos;
        }
    }

    private ContentValues creatContentValue(BGlucoseEntity info) {
        ContentValues values = new ContentValues();
        values.put("id", info.getId());
        values.put("account_id", info.getAccount_id());
        values.put("role_id", info.getRole_id());
        values.put("mtype", info.getMtype());
        values.put("measure_time", info.getMeasure_time());
        values.put("bsl", info.getBsl());
        values.put("description", info.getDescription());
        values.put("isdelete", info.getDelete());
        String[] tempDate = info.getMeasure_time().split(" ");
        values.put("measure_date", tempDate[0]);
        return values;
    }

    private BGlucoseEntity getContentValue(Cursor c) {
        BGlucoseEntity info = new BGlucoseEntity();
        info.setId(c.getLong(0));
        info.setAccount_id(c.getInt(1));
        info.setRole_id(c.getInt(2));
        info.setMtype(c.getString(3));
        info.setMeasure_time(c.getString(4));
        info.setDescription(c.getInt(5));
        info.setBsl(c.getFloat(6));
        info.setDelete(c.getInt(7));
        return info;
    }
}
