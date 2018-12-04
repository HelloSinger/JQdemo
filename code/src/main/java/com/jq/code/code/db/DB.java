package com.jq.code.code.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jq.code.code.business.Config;
import com.jq.code.code.util.LogUtil;
import com.jq.code.model.DataType;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * chipsea数据库操作类
 *
 * @author kenneth
 */
public class DB extends SQLiteOpenHelper {

    // =====================================定义常量=======================================
    public static final String TAG = "DB";

    private static DB instance; // 当前实体类
    // 版本号，字段名
    public static final int DB_VERSION = 12;
    public static int DB_VERSION_OLD = DB_VERSION;
    public static final String DB_NAME = "jq.db"; // 必须要求db后缀~~！
    private Context context;
    private ReentrantReadWriteLock mReentrantReadWriteLock = new ReentrantReadWriteLock();
    /** 读锁 */
    private Lock mReadLock = mReentrantReadWriteLock.readLock();
    /** 写锁 */
    private Lock mWriteLock = mReentrantReadWriteLock.writeLock();

    /**
     * 析构函数
     */
    protected DB(Context context) {
        super(context.getApplicationContext(), DB_NAME, null, DB_VERSION);
        this.context = context.getApplicationContext();
    }

    /** 获取读锁 */
    public Lock getReadLock() {
        return mReadLock;
    }

    /** 获取写锁 */
    public Lock getWriteLock() {
        return mWriteLock;
    }

    /**
     * 得到当前实体类
     */
    public static DB getInstance(Context context) {
        if (instance == null) {
            synchronized (DB.class) {
                if(null == instance) {
                    instance = new DB(context.getApplicationContext());
                }
            }
        }
        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // 建表
        db.execSQL(RoleDB.CREATE_TABLE_ROLE);
        db.execSQL(WeightDataDB.CREATE_TABLE_ROLE_DATA);
        db.execSQL(RemindWeightDB.CREATE_TABLE_WEIGHT_REMIND);
        db.execSQL(PushDataDB.CREATE_TABLE_PUSH);
        db.execSQL(SyncDataDB.CREATE_TABLE_SYNC);
        db.execSQL(BPressDB.CREATE_TABLE_BPRESS);
        db.execSQL(BGlucoseDB.CREATE_TABLE_BGLUCOSE);
        db.execSQL(DataTypeSetDB.CREATE_TABLE_TYPE);
        db.execSQL(FeedbackDB.CREATE_TABLE);
        db.execSQL(FeedbackReplyDB.CREATE_TABLE);
        db.execSQL(FoodDB.CREATE_TABLE);
        db.execSQL(SportDB.CREATE_TABLE);
        db.execSQL(WeightTmpDB.CREATE_TABLE);
        db.execSQL(SyncDataDB2.CREATE_TABLE_SYNC);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtil.i("onUpgrade", "onUpgrade from " + oldVersion + " to " + newVersion);
        DB_VERSION_OLD = oldVersion;

//        update3(db, oldVersion);
//        update4(db, oldVersion);
//        update6(db, oldVersion);
//        update7(db, oldVersion);
//        update8(db, oldVersion);
//        update9(db, oldVersion);
//        update10(db, oldVersion);
//        update11(db, oldVersion);

        update11(db, oldVersion);

        update12(db, oldVersion);

    }

    private void update12(SQLiteDatabase db, int oldVersion) {

        if (oldVersion < 12) {
            db.execSQL("ALTER TABLE cs_role_data ADD COLUMN rn8 text");
        }

    }
    private void update11(SQLiteDatabase db, int oldVersion) {
        if (oldVersion < 11) {
            db.execSQL("DROP TABLE IF EXISTS " + RoleDB.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + WeightDataDB.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + RemindWeightDB.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + PushDataDB.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + SyncDataDB.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + BPressDB.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + BGlucoseDB.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DataTypeSetDB.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + FeedbackDB.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + FeedbackReplyDB.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + FoodDB.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + SportDB.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + WeightTmpDB.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + SyncDataDB2.TABLE_NAME);

            onCreate(db);
        }
    }

    private void update9(SQLiteDatabase db, int oldVersion) {
        if (oldVersion < 9) {
            db.execSQL("ALTER TABLE cs_push_data ADD COLUMN ncomments INT DEFAULT 0");
            db.execSQL("ALTER TABLE cs_push_data ADD COLUMN nlikes INT DEFAULT 0");
            db.execSQL("ALTER TABLE cs_role_data ADD COLUMN mtype text");
            db.execSQL("ALTER TABLE cs_role ADD COLUMN weight_init float DEFAULT 0");
            db.execSQL("ALTER TABLE cs_sync_log ADD COLUMN mtype text");
            db.execSQL("UPDATE cs_sync_log SET mtype = 'weight'");
            db.execSQL("create unique index role_uk on cs_role(id)");
            db.execSQL("create unique index push_uk on cs_push_data(id)");
            updateRoleData(db);
            db.execSQL("DROP TABLE cs_role_data_day");
            db.execSQL("DROP TABLE cs_role_data_week");
            db.execSQL("DROP TABLE cs_role_data_month");
            db.execSQL(BPressDB.CREATE_TABLE_BPRESS);
            db.execSQL(BGlucoseDB.CREATE_TABLE_BGLUCOSE);
            db.execSQL(DataTypeSetDB.CREATE_TABLE_TYPE);
            db.execSQL("INSERT INTO cs_type_data (id,account_id, role_id, mtype,measure_time,isdelete) " +
                    "SELECT id,account_id, role_id,('weight'),weight_time,isdelete FROM cs_role_data");
            //2.0版本update10
            db.execSQL(FoodDB.CREATE_TABLE);
            db.execSQL(SportDB.CREATE_TABLE);
            Config.getInstance(context).setDynamicType(DataType.EXERCISE.getType());
            Config.getInstance(context).setDynamicType(DataType.FOOD.getType());
            //2.0版本update11
            db.execSQL(WeightTmpDB.CREATE_TABLE);
        }
    }
    private void update10(SQLiteDatabase db, int oldVersion) {
        if (oldVersion < 10) {
            db.execSQL("ALTER TABLE cs_role ADD COLUMN role_type int DEFAULT 0");
        }
    }
    private void update8(SQLiteDatabase db, int oldVersion) {
        if (oldVersion < 8) {
            db.execSQL(FeedbackDB.CREATE_TABLE);
            db.execSQL(FeedbackReplyDB.CREATE_TABLE);

        }
    }

    private void update7(SQLiteDatabase db, int oldVersion) {
        //6对应v1.4.4
        //7对应v1.4.5
        if (oldVersion < 7) {
            db.execSQL("ALTER TABLE cs_role_data ADD COLUMN scaleweight VARCHAR(20) DEFAULT ''");
            db.execSQL("ALTER TABLE cs_role_data ADD COLUMN scaleproperty INT DEFAULT 1");
            db.execSQL("ALTER TABLE cs_role_data ADD COLUMN productid INT DEFAULT 0");
        }
    }

    private void update6(SQLiteDatabase db, int oldVersion) {
        if (oldVersion < 6) {
            db.execSQL("ALTER TABLE cs_push_data ADD COLUMN company_id INT DEFAULT 0");
            db.execSQL("ALTER TABLE cs_push_data ADD COLUMN sex text DEFAULT ''");
        }
    }

    private void update4(SQLiteDatabase db, int oldVersion) {
        if (oldVersion <= 4) {
            db.execSQL("ALTER TABLE cs_role_data ADD COLUMN isdelete INT DEFAULT 0");
            db.execSQL(SyncDataDB.CREATE_TABLE_SYNC);
        }
    }

    private void update3(SQLiteDatabase db, int oldVersion) {
        if (oldVersion <= 3) {
            db.execSQL(PushDataDB.CREATE_TABLE_PUSH);
        }
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    public void closeDB() {

    }

    /**
     * 更新体重数据表
     *
     * @param db
     */
    private void updateRoleData(SQLiteDatabase db) {
        db.execSQL(" ALTER TABLE cs_role_data RENAME TO cs_role_data_tmp");
        db.execSQL(WeightDataDB.CREATE_TABLE_ROLE_DATA);
        db.execSQL("INSERT INTO cs_role_data (id, weight, weight_time, bmi, axunge, bone, muscle, water, metabolism, body_age, viscera, " +
                "account_id, role_id, sync_time, isdelete, scaleweight, scaleproperty, productid, mtype,weight_date) " +
                "SELECT id, weight, weight_time, bmi, axunge, bone, muscle, water, metabolism, body_age, viscera, account_id, " +
                "role_id, sync_time, isdelete, scaleweight, scaleproperty, productid,('weight'),strftime('%Y%m%d',weight_time) FROM cs_role_data_tmp");
        db.execSQL("DROP TABLE cs_role_data_tmp");
    }

    /**
     * 插入数据
     *
     * @param table
     * @param values
     * @return
     **/
    public long insert(SQLiteDatabase db, String table, ContentValues values) {
        return db.insert(table, null, values);
    }

    /**
     * 插入数据
     *
     * @param table
     * @param values
     * @return
     **/
    public long insert(SQLiteDatabase db, String table, ContentValues values, int conflictAlgorithm) {
        return db.insertWithOnConflict(table, null, values, conflictAlgorithm);
    }

    /**
     * 更新数据
     */
    public int update(SQLiteDatabase db, String table, ContentValues values, String where, String[] whereArgs) {
        return db.update(table, values, where, whereArgs);
    }

    /**
     * 删除数据
     *
     * @param table
     * @param id
     * @return
     */
    public int delete(SQLiteDatabase db, String table, int id) {
        return delete(db, table, "id=?", new String[]{String.valueOf(id)});
    }

    /**
     * 删除数据
     */
    public int delete(SQLiteDatabase db, String table, String where, String[] whereArgs) {
        return db.delete(table, where, whereArgs);
    }
}
