package com.jq.code.code.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jq.code.code.util.TimeUtil;
import com.jq.code.model.DataType;
import com.jq.code.model.ExerciseDietEntity;
import com.jq.code.model.PutBase;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.WeightEntity;
import com.jq.code.model.sport.SubmitFoodEntity;
import com.jq.code.model.sport.SubmitSportEntity;
import com.jq.code.model.trend.RecentWeghtEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 各数据类型聚合表，为了用于显示，满足2.0版本的业务混杂显示要求，即将不用
 */
public class DataTypeSetDB {
    public static final String TABLE_NAME = "cs_type_data";
    /**
     * 角色建表
     */
    public static final String CREATE_TABLE_TYPE = "create table if not exists "
            + TABLE_NAME
            + "(id bigint not null, "
            + "account_id integer not null, "
            + "role_id integer not null,"
            + "mtype varchar(32),"
            + "measure_time date null,"
            + "isdelete integer)";

    protected DB mDBUtil;
    private Context context;

    /**
     * 析构函数
     */
    protected DataTypeSetDB(Context context) {
        this.context = context.getApplicationContext();
        mDBUtil = DB.getInstance(context.getApplicationContext());
    }

    /**
     * 得到当前实体类
     */
    public static DataTypeSetDB getInstance(Context context) {
        DataTypeSetDB instance = new DataTypeSetDB(context);
        return instance;
    }

    /**
     * 添加角色信息
     *
     * @param info
     * @return
     */
    public void create(SQLiteDatabase db, PutBase info) {
        if (!isExist(db, info)) {
            mDBUtil.insert(db, "cs_type_data", creatContentValue(info));
        } else if (info instanceof SubmitFoodEntity || info instanceof SubmitSportEntity) {
        } else {
            modify(db, info);
        }
    }

    /**
     * 删除角色
     *
     * @param info
     * @return
     */
    public int remove(SQLiteDatabase db, PutBase info) {
        return mDBUtil.delete(db, "cs_type_data", "measure_time=? and role_id=? and mtype=?",
                new String[]{info.getMeasure_time(), String.valueOf(info.getRole_id()), info.getMtype()});
    }

    /**
     * 删除角色
     *
     * @param db
     * @param role_id
     * @param id
     * @param mtype
     * @return
     */
    public int remove(SQLiteDatabase db, long role_id, long id, String mtype) {
        return mDBUtil.delete(db, "cs_type_data", "role_id=? and id=? and mtype=?",
                new String[]{String.valueOf(role_id), String.valueOf(id), mtype});
    }


    public void remove(SQLiteDatabase db, long role_id, String mtype) {
        mDBUtil.delete(db, "cs_type_data", "role_id=? and mtype=?", new String[]{role_id + "", mtype});
    }

    public void removeSportOrFood(SQLiteDatabase db, long role_id, String measure_time) {
        if (isSportAndFoodEmpty(db, role_id, measure_time)) {
            mDBUtil.delete(db, "cs_type_data", "role_id=? and mtype=? and measure_time=?", new String[]{role_id + "", PutBase.TYPE_EXERCISE_FOOD, measure_time + " 23:59:59"});
        }
    }

    /**
     * 是否能删除指定
     *
     * @param role_id
     * @param measure_time
     * @return
     */
    public boolean isSportAndFoodEmpty(SQLiteDatabase db, long role_id, String measure_time) {
        boolean isEmpty = true;
        boolean isSportEmpty = SportDB.getInstance(context).isEmpty(db, role_id, measure_time.split(" ")[0]);
        boolean isFoodEmpty = FoodDB.getInstance(context).isEmpty(db, role_id, measure_time.split(" ")[0]);
        if (isSportEmpty && isFoodEmpty) {

        } else {
            isEmpty = false;
        }
        return isEmpty;
    }

    public int remove() {
        return mDBUtil.delete(mDBUtil.getWritableDatabase(), "cs_type_data", null, null);
    }

    public void modifySet(PutBase info) {
        synchronized (mDBUtil) {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            modify(db, info);
            
        }
    }

    public int modify(SQLiteDatabase db, PutBase info) {
        return mDBUtil.update(db, "cs_type_data", creatContentValue(info), "measure_time=? and role_id=? and mtype=?",
                new String[]{info.getMeasure_time(), String.valueOf(info.getRole_id()), info.getMtype()});
    }

    private boolean isExist(SQLiteDatabase db, PutBase info) {
        Cursor c = null;
        String sql = "select count(*) from " + "cs_type_data" + " where account_id=? and role_id=? and mtype=? and measure_time=?";
        c = db.rawQuery(sql, new String[]{info.getAccount_id() + "", info.getRole_id() + "",
                info.isFe() ? PutBase.TYPE_EXERCISE_FOOD : info.getMtype(), info.isFe() ? info.getMeasure_time() + " 23:59:59" : info.getMeasure_time()});
        boolean isExist = false;
        while (c.moveToNext()) {
            isExist = c.getInt(0) > 0;
        }
        c.close();
        return isExist;
    }

    public long getCount(RoleInfo info) {
        SQLiteDatabase db = mDBUtil.getReadableDatabase();
        Cursor c = null;
        String sql = "select count(*)from cs_type_data where isdelete=0 and account_id=? and role_id=?";
        c = db.rawQuery(sql, new String[]{info.getAccount_id() + "", info.getId() + ""});
        c.moveToFirst();
        long count = c.getLong(0);
        c.close();
        
        return count;
    }

    /**
     * 根据时间段查询数据
     *
     * @param mtypes
     * @param account_id
     * @param role_id
     * @param startTime  格式yyyy-MM-dd HH:mm:ss
     * @param endTime    格式yyyy-MM-dd HH:mm:ss
     * @return
     */
    public ArrayList<PutBase> findByTimeSlot(String[] mtypes, long account_id, long role_id, String startTime, String endTime) {
        synchronized (mDBUtil) {
            ArrayList<PutBase> infos = new ArrayList<PutBase>();
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            StringBuilder builder = new StringBuilder();
            builder.append("select * from cs_type_data where");
            builder.append(" isdelete=0 and account_id=? and role_id=?");
            builder.append(" and measure_time>=? and  measure_time<?");
            if (mtypes != null) {
                builder.append(" and mtype in (");
                builder.append(formatTypeStr(mtypes));
                builder.append(")");
            }
            builder.append(" order by measure_time desc");
            Cursor c = null;
            c = db.rawQuery(builder.toString(), new String[]{account_id + "", role_id + "", startTime, endTime});
            while (c.moveToNext()) {
                infos.add(getContentValue(c));
            }
            c.close();
            
            return getTypeData(infos);
        }
    }

    /**
     * 获取ends时间cnt天的数据
     *
     * @param mtypes
     * @param account_id
     * @param roleId
     * @param cnt
     * @param end        格式yyyy-MM-dd
     * @return
     */
    private String getStartDate(String[] mtypes, long account_id, long roleId, int cnt, String end) {
        synchronized (mDBUtil) {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            StringBuilder builder = new StringBuilder();
            builder.append("select distinct substr(measure_time,1,11) date from cs_type_data where isdelete=0 and account_id=? and role_id=? and measure_time<?");
            if (mtypes != null) {
                builder.append(" and mtype in (");
                builder.append(formatTypeStr(mtypes));
                builder.append(")");
            }
            builder.append(" order by measure_time desc limit ?");
            Cursor c = null;
            c = db.rawQuery(builder.toString(), new String[]{account_id + "", roleId + "", end, cnt + ""});
            String satrtTime = null;
            while (c.moveToNext()) {
                satrtTime = c.getString(c.getColumnIndex("date"));
            }
            c.close();
            
            return satrtTime;
        }
    }

    /**
     * 根据endDate查询有数据的count天的数据
     *
     * @param mtypes
     * @param account_id
     * @param role_id
     * @param count
     * @param endDate    格式yyyy-MM-dd
     * @return
     */
    public ArrayList<PutBase> findByEndTimeWithCount(String[] mtypes, long account_id, long role_id, int count, String endDate) {
        String startDate = getStartDate(mtypes, account_id, role_id, count, endDate);
        if (startDate == null) return new ArrayList<>();
        return findByTimeSlot(mtypes, account_id, role_id, startDate, endDate);
    }

    /**
     * 按类型查找数据
     *
     * @param putBases
     * @return
     */
    public ArrayList<PutBase> getTypeData(ArrayList<PutBase> putBases) {
        ArrayList<PutBase> tmpBases = new ArrayList<PutBase>();
        if (!putBases.isEmpty()) {
            for (PutBase base : putBases) {
                PutBase typeData = getTypeData(base);
                if (typeData != null) {
                    tmpBases.add(typeData);
                }
            }
        }
        return tmpBases;
    }

    public PutBase getTypeData(PutBase base) {
        String time = base.getMeasure_time();
        long role_id = base.getRole_id();
        if (base.getMtype().equals(DataType.WEIGHT.getType())) {
            base = WeightDataDB.getInstance(context).findRoleDataByRoleIdAndTime(role_id, time);
        } else if (base.getMtype().equals(DataType.BP.getType())) {
            base = BPressDB.getInstance(context).findBPress(role_id, time);
        } else if (base.getMtype().equals(DataType.BSL.getType())) {
            base = BGlucoseDB.getInstance(context).findBGlucose(role_id, time);
        } else if (base.getMtype().equals(PutBase.TYPE_EXERCISE_FOOD)) {
            base = parseSportFoood(role_id, time);
        }
        return base;
    }

    /**
     * 查找指定类型的数据的最新时间
     *
     * @param mtype
     * @param account_id
     * @param roleId
     * @return
     */
    public PutBase findNewestDataBytype(String mtype, long account_id, long roleId) {
        synchronized (mDBUtil) {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            String sql = "select * from cs_type_data where isdelete=0 and account_id=? and role_id=? and mtype=? order by measure_time desc limit 1";
            Cursor c = null;
            c = db.rawQuery(sql, new String[]{account_id + "", roleId + "", mtype});
            PutBase base = null;
            while (c.moveToNext()) {
                base = getContentValue(c);
            }
            c.close();
            
            PutBase typeData = null;
            if (base != null) {
                typeData = getTypeData(base);
            }
            return typeData;
        }
    }

    /**
     * 查找指定类型的数据的最新时间
     *
     * @param mtype
     * @param account_id
     * @param roleId
     * @return
     */
    public PutBase findDataByTime(String mtype, long account_id, long roleId, String time) {
        synchronized (mDBUtil) {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            String sql = "select * from cs_type_data where isdelete=0 and account_id=? and role_id=? and mtype=? and measure_time=?";
            Cursor c = null;
            c = db.rawQuery(sql, new String[]{account_id + "", roleId + "", mtype, time});
            PutBase base = null;
            while (c.moveToNext()) {
                base = getContentValue(c);
            }
            c.close();
            
            PutBase typeData = null;
            if (base != null) {
                typeData = getTypeData(base);
            }
            return typeData;
        }
    }

    /**
     * 解析于东饮食数据
     *
     * @param role_id
     * @param time
     * @return
     */
    private PutBase parseSportFoood(long role_id, String time) {
        ExerciseDietEntity dietEntity = new ExerciseDietEntity();
        dietEntity.setMeasure_time(time);
        dietEntity.setRole_id(role_id);
        dietEntity.setSports(SportDB.getInstance(context).findBydate(role_id, time.split(" ")[0]));
        dietEntity.setFoods(FoodDB.getInstance(context).findByDate(role_id, time.split(" ")[0]));
        return dietEntity;
    }

    /**
     * mtypes sqlite 格式化
     *
     * @param mtypes
     * @return
     */
    private String formatTypeStr(String[] mtypes) {
        StringBuilder builder = new StringBuilder();
        String tmp = null;
        for (int i = 0; i < mtypes.length; i++) {
            if (mtypes[i].equals(DataType.FOOD.getType()) || mtypes[i].equals(DataType.EXERCISE.getType())) {
                tmp = PutBase.TYPE_EXERCISE_FOOD;
            } else {
                builder.append("'");
                builder.append(mtypes[i]);
                builder.append("'");
                if (i == mtypes.length - 1 && tmp == null) {
                } else {
                    builder.append(",");
                }
            }
            if (i == mtypes.length - 1 && tmp != null) {
                builder.append("'");
                builder.append(tmp);
                builder.append("'");
            }
        }
        return builder.toString();
    }


    private ContentValues creatContentValue(PutBase info) {
        ContentValues values = new ContentValues();
        values.put("id", info.getId());
        values.put("account_id", info.getAccount_id());
        values.put("role_id", info.getRole_id());
        if (info.isFe()) {
            values.put("mtype", PutBase.TYPE_EXERCISE_FOOD);
            values.put("measure_time", info.getMeasure_time() + " 23:59:59");
        } else {
            values.put("mtype", info.getMtype());
            values.put("measure_time", info.getMeasure_time() == null ? info.getWeight_time() : info.getMeasure_time());
        }
        values.put("isdelete", info.getDelete());
        return values;
    }

    private PutBase getContentValue(Cursor c) {
        PutBase info = new PutBase();
        info.setId(c.getLong(0));
        info.setAccount_id(c.getInt(1));
        info.setRole_id(c.getInt(2));
        info.setMtype(c.getString(3));
        info.setMeasure_time(c.getString(4));
        info.setDelete(c.getInt(5));
        return info;
    }


    // TODO 移动到其它地方

    /**
     * 获取从今天开始，最近7个数据天的趋势数据
     *
     * @param account_id
     * @param role_id
     * @return
     */
    public List<RecentWeghtEntity> getTrendStatsByDataDays(long account_id, long role_id) {
        return getTrendStatsByDataDays(account_id, role_id, 7, getTomorrowAsString());
    }


    public List<RecentWeghtEntity> getTrendStatsByDataDays(long account_id, long role_id, int nDays, String endDate) {
        String[] mtypes = {"weight"};
        ArrayList<PutBase> weightList = findByEndTimeWithCount(mtypes, account_id, role_id, nDays, endDate);
        List<RecentWeghtEntity> rList = new ArrayList<>();
        RecentWeghtEntity prev = null;
        for (PutBase item : weightList) {
            WeightEntity w = (WeightEntity) item;
            if (mergeable(prev, w)) {
                prev.addWeight(w);
            } else {
                prev = new RecentWeghtEntity();
                prev.addWeight(w);
                rList.add(prev);
            }
        }
        return rList;

    }

    /**
     * 称重在同一天则可合并
     *
     * @param recentWeghtEntity
     * @param weightEntity
     * @return
     */
    private boolean mergeable(RecentWeghtEntity recentWeghtEntity, WeightEntity weightEntity) {
        return recentWeghtEntity != null &&
                recentWeghtEntity.getDate().equals(weightEntity.getMeasure_time().substring(0, 10));
    }

    public String getTomorrowAsString() {
        return TimeUtil.addDay(TimeUtil.getCurDate(), 1);
    }
}
