package com.jq.code.code.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.jq.code.code.util.TimeUtil;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.WeightEntity;
import com.jq.code.model.WeightTipEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/12/23.
 */

public class WeightDataTipDB {

    /**
     * 角色数据建表
     */
    public static final String CREATE_TABLE = "create table if not exists "
            + "cs_weight_data_tip "
            + "(id integer primary key, "
            + "sex text,"
            + "age_min integer,"
            + "age_max integer,"
            + "bmi_min float,"
            + "bmi_max float,"
            + "fat_min float, "
            + "fat_max float,"
            + "body_type text,"
            + "tip text)";

    private DB mDBUtil;
    private Context context;

    private WeightDataTipDB(Context context) {
        this.context = context.getApplicationContext();
        mDBUtil = DB.getInstance(context.getApplicationContext());
        SQLiteDatabase db = mDBUtil.getWritableDatabase();
        db.execSQL(WeightDataTipDB.CREATE_TABLE);
        initData(db);
        mDBUtil.closeDB();
    }

    public static WeightDataTipDB getInstance(Context context) {
        WeightDataTipDB instance = new WeightDataTipDB(context);
        return instance;
    }

    private void create(SQLiteDatabase db, List<WeightTipEntity> entities) {
        db.beginTransaction();
        for (WeightTipEntity kindInfo : entities) {
            mDBUtil.insert(db, "cs_weight_data_tip", creatContentValue(kindInfo));
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        mDBUtil.closeDB();
    }

    public WeightTipEntity getTipEntity(RoleInfo roleInfo, WeightEntity entity) {
        long gapDays = TimeUtil.getGapDays(roleInfo.getBirthday(), entity.getWeight_time());
        WeightTipEntity tipEntity = find(roleInfo.getSex(), (int) Math.abs(gapDays / 365), entity.getBmi(), entity.getAxunge());
        if (tipEntity == null) {

        } else {
            int compentID = getCompentID(context, tipEntity.getTip());
            if (compentID != 0) {
                String tip = context.getResources().getString(compentID);
                String[] split = tip.split(";");
                Random random = new Random();
                tip = split[random.nextInt(split.length)];
                tipEntity.setTip(tip);
            }
            int compentID1 = getCompentID(context, tipEntity.getBody_type());
            if (compentID1 != 0) {
                tipEntity.setBody_type(context.getResources().getString(compentID1));
            }
        }
        return tipEntity;
    }

    private WeightTipEntity find(String sex, int age, float bmi, float axunge) {
        WeightTipEntity weightTipEntity = null;
        synchronized (mDBUtil) {
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from cs_weight_data_tip where sex=? and age_min<=? and age_max>? and bmi_min<=? and bmi_max>? and fat_min<=? and fat_max>?",
                    new String[]{sex, String.valueOf(age), String.valueOf(age), String.valueOf(bmi), String.valueOf(bmi), String.valueOf(axunge), String.valueOf(axunge)});
            while (cursor.moveToNext()) {
                weightTipEntity = getContentValue(cursor);

            }
            cursor.close();
            mDBUtil.closeDB();
        }
        return weightTipEntity;
    }

    private ContentValues creatContentValue(WeightTipEntity kindInfo) {
        ContentValues values = new ContentValues();
        values.put("sex", kindInfo.getSex());
        values.put("age_min", kindInfo.getAge_min());
        values.put("age_max", kindInfo.getAge_max());
        values.put("bmi_min", kindInfo.getBmi_min());
        values.put("bmi_max", kindInfo.getBmi_max());
        values.put("fat_min", kindInfo.getFat_min());
        values.put("fat_max", kindInfo.getFat_max());
        values.put("body_type", kindInfo.getBody_type());
        values.put("tip", kindInfo.getTip());
        return values;
    }

    private WeightTipEntity getContentValue(Cursor c) {
        WeightTipEntity values = new WeightTipEntity();
        values.setId(c.getInt(c.getColumnIndex("id")));
        values.setSex(c.getString(c.getColumnIndex("sex")));
        values.setAge_min(c.getInt(c.getColumnIndex("age_min")));
        values.setAge_max(c.getInt(c.getColumnIndex("age_max")));
        values.setBmi_min(c.getFloat(c.getColumnIndex("bmi_min")));
        values.setBmi_max(c.getFloat(c.getColumnIndex("bmi_max")));
        values.setFat_min(c.getFloat(c.getColumnIndex("fat_min")));
        values.setFat_max(c.getFloat(c.getColumnIndex("fat_max")));
        values.setTip(c.getString(c.getColumnIndex("tip")));
        String body_type = c.getString(c.getColumnIndex("body_type"));
        values.setBody_type(body_type);
        values.setStandard(parseType(body_type));
        return values;
    }

    String[] standard1 = new String[]{"bodyType2", "bodyType3"};
    String[] standard2 = new String[]{"bodyType7", "bodyType8", "bodyType10", "bodyType11"};
    String[] standard3 = new String[]{"bodyType12", "bodyType13", "bodyType14", "bodyType16", "bodyType17"};

    private int parseType(String body_type) {
        List<String> standardList1 = Arrays.asList(standard1);
        if (standardList1.contains(body_type)) return 1;
        List<String> standardList2 = Arrays.asList(standard2);
        if (standardList2.contains(body_type)) return 2;
        List<String> standardList3 = Arrays.asList(standard3);
        if (standardList3.contains(body_type)) return 3;
        return 0;
    }

    private boolean isEmpty(SQLiteDatabase db) {
        Cursor c = db.rawQuery("select count(*) from cs_weight_data_tip", null);
        boolean isEmpty = false;
        while (c.moveToNext()) {
            isEmpty = c.getInt(0) == 0;
        }
        c.close();
        return isEmpty;
    }

    private void initData(SQLiteDatabase db) {
        if (!isEmpty(db)) return;
        List<WeightTipEntity> entities = new ArrayList<>();
        entities.add(new WeightTipEntity("男", 0, 18, 0, 75, 0, 76, "bodyType1", "codeTip1"));
        entities.add(new WeightTipEntity("男", 18, 30, 0, 15, 0, 20, "bodyType2", "codeTip2"));
        entities.add(new WeightTipEntity("男", 18, 30, 15, 17, 0, 20, "bodyType3", "codeTip3"));
        entities.add(new WeightTipEntity("男", 18, 30, 17, 18.5f, 0, 20, "bodyType6", "codeTip4"));
        entities.add(new WeightTipEntity("男", 18, 30, 18.5f, 20, 0, 20, "bodyType5", "codeTip5"));
        entities.add(new WeightTipEntity("男", 18, 30, 20, 22, 0, 20, "bodyType4", "codeTip6"));
        entities.add(new WeightTipEntity("男", 18, 30, 0, 22, 20, 76, "bodyType10", "codeTip7"));
        entities.add(new WeightTipEntity("男", 18, 30, 22, 23, 20, 76, "bodyType7", "codeTip8"));
        entities.add(new WeightTipEntity("男", 18, 30, 23, 24, 20, 76, "bodyType8", "codeTip9"));
        entities.add(new WeightTipEntity("男", 18, 30, 22, 22.5f, 0, 17, "bodyType9", "codeTip10"));
        entities.add(new WeightTipEntity("男", 18, 30, 22, 22.5f, 17, 20, "bodyType10", "codeTip11"));
        entities.add(new WeightTipEntity("男", 18, 30, 22.5f, 24, 0, 18, "bodyType9", "codeTip12"));
        entities.add(new WeightTipEntity("男", 18, 30, 22.5f, 24, 18, 20, "bodyType10", "codeTip13"));
        entities.add(new WeightTipEntity("男", 18, 30, 24, 100, 0, 18.5f, "bodyType9", "codeTip14"));
        entities.add(new WeightTipEntity("男", 18, 30, 24, 100, 18.5f, 20, "bodyType10", "codeTip15"));
        entities.add(new WeightTipEntity("男", 18, 30, 24, 28, 20, 76, "bodyType11", "codeTip16"));
        entities.add(new WeightTipEntity("男", 18, 30, 28, 40, 20, 76, "bodyType12", "codeTip17"));
        entities.add(new WeightTipEntity("男", 18, 30, 40, 60, 20, 76, "bodyType13", "codeTip18"));
        entities.add(new WeightTipEntity("男", 18, 30, 60, 100, 20, 76, "bodyType14", "codeTip19"));
        entities.add(new WeightTipEntity("男", 30, 40, 0, 15, 0, 21.5f, "bodyType2", "codeTip20"));
        entities.add(new WeightTipEntity("男", 30, 40, 15, 17, 0, 21.5f, "bodyType3", "codeTip21"));
        entities.add(new WeightTipEntity("男", 30, 40, 17, 18.5f, 0, 21.5f, "bodyType6", "codeTip22"));
        entities.add(new WeightTipEntity("男", 30, 40, 0, 22.5f, 21.5f, 76, "bodyType10", "codeTip23"));
        entities.add(new WeightTipEntity("男", 30, 40, 22.5f, 23, 0, 18.5f, "bodyType9", "codeTip24"));
        entities.add(new WeightTipEntity("男", 30, 40, 22.5f, 23, 18.5f, 21.5f, "bodyType10", "codeTip25"));
        entities.add(new WeightTipEntity("男", 30, 40, 18.5f, 20, 0, 21.5f, "bodyType5", "codeTip26"));
        entities.add(new WeightTipEntity("男", 30, 40, 20, 22.5f, 0, 21.5f, "bodyType4", "codeTip27"));
        entities.add(new WeightTipEntity("男", 30, 40, 22.5f, 24, 21.5f, 76, "bodyType4", "codeTip27"));
        entities.add(new WeightTipEntity("男", 30, 40, 24, 26, 21.5f, 76, "bodyType8", "codeTip29"));
        entities.add(new WeightTipEntity("男", 30, 40, 23, 23.5f, 0, 19.5f, "bodyType9", "codeTip30"));
        entities.add(new WeightTipEntity("男", 30, 40, 23, 23.5f, 19.5f, 21.5f, "bodyType10", "codeTip31"));
        entities.add(new WeightTipEntity("男", 30, 40, 23.5f, 24, 0, 20.5f, "bodyType9", "codeTip32"));
        entities.add(new WeightTipEntity("男", 30, 40, 23.5f, 24, 20, 21.5f, "bodyType10", "codeTip33"));
        entities.add(new WeightTipEntity("男", 30, 40, 24, 100, 0, 20.5f, "bodyType9", "codeTip34"));
        entities.add(new WeightTipEntity("男", 30, 40, 24, 100, 20.5f, 21.5f, "bodyType10", "codeTip35"));
        entities.add(new WeightTipEntity("男", 30, 40, 26, 28, 21.5f, 76, "bodyType11", "codeTip36"));
        entities.add(new WeightTipEntity("男", 30, 40, 28, 35, 21.5f, 76, "bodyType12", "codeTip37"));
        entities.add(new WeightTipEntity("男", 30, 40, 35, 75, 21.5f, 76, "bodyType13", "codeTip38"));
        entities.add(new WeightTipEntity("男", 30, 40, 75, 100, 21.5f, 76, "bodyType14", "codeTip39"));
        entities.add(new WeightTipEntity("男", 40, 80, 0, 15, 0, 23, "bodyType2", "codeTip40"));
        entities.add(new WeightTipEntity("男", 40, 80, 15, 17, 0, 23, "bodyType3", "codeTip41"));
        entities.add(new WeightTipEntity("男", 40, 80, 17, 18.5f, 0, 23, "bodyType6", "codeTip42"));
        entities.add(new WeightTipEntity("男", 40, 80, 18.5f, 20, 0, 23, "bodyType5", "codeTip43"));
        entities.add(new WeightTipEntity("男", 40, 80, 20, 24, 0, 23, "bodyType4", "codeTip44"));
        entities.add(new WeightTipEntity("男", 40, 80, 0, 24, 23, 76, "bodyType10", "codeTip45"));
        entities.add(new WeightTipEntity("男", 40, 80, 24, 25.5f, 23, 76, "bodyType8", "codeTip46"));
        entities.add(new WeightTipEntity("男", 40, 80, 25.5f, 27, 23, 76, "bodyType8", "codeTip47"));
        entities.add(new WeightTipEntity("男", 40, 80, 24, 100, 20, 23, "bodyType10", "codeTip48"));
        entities.add(new WeightTipEntity("男", 40, 80, 24, 100, 0, 20, "bodyType9", "codeTip49"));
        entities.add(new WeightTipEntity("男", 40, 80, 27, 29, 23, 76, "bodyType11", "codeTip50"));
        entities.add(new WeightTipEntity("男", 40, 80, 29, 35, 23, 76, "bodyType12", "codeTip51"));
        entities.add(new WeightTipEntity("男", 40, 80, 35, 75, 23, 76, "bodyType13", "codeTip52"));
        entities.add(new WeightTipEntity("男", 40, 80, 75, 100, 23, 76, "bodyType14", "codeTip53"));
        entities.add(new WeightTipEntity("男", 80, 120, 0, 75, 0, 76, "bodyType15", "codeTip54"));

        entities.add(new WeightTipEntity("女", 0, 18, 0, 100, 0, 76, "bodyType1", "codeTip55"));
        entities.add(new WeightTipEntity("女", 18, 30, 0, 15, 0, 24, "bodyType2", "codeTip56"));
        entities.add(new WeightTipEntity("女", 18, 30, 15, 17, 0, 24, "bodyType3", "codeTip57"));
        entities.add(new WeightTipEntity("女", 18, 30, 17, 18.5f, 0, 24, "bodyType6", "codeTip58"));
        entities.add(new WeightTipEntity("女", 18, 30, 18.f, 20, 0, 24, "bodyType4", "codeTip59"));
        entities.add(new WeightTipEntity("女", 18, 30, 0, 20, 24, 76, "bodyType10", "codeTip60"));
        entities.add(new WeightTipEntity("女", 18, 30, 20, 28, 24, 76, "bodyType8", "codeTip61"));
        entities.add(new WeightTipEntity("女", 18, 30, 20, 100, 0, 24, "bodyType9", "codeTip62"));
        entities.add(new WeightTipEntity("女", 18, 30, 28, 35, 24, 76, "bodyType12", "codeTip63"));
        entities.add(new WeightTipEntity("女", 18, 30, 35, 75, 24, 76, "bodyType16", "codeTip64"));
        entities.add(new WeightTipEntity("女", 18, 30, 75, 100, 24, 76, "bodyType17", "codeTip65"));
        entities.add(new WeightTipEntity("女", 30, 80, 0, 15, 0, 28, "bodyType2", "codeTip66"));
        entities.add(new WeightTipEntity("女", 30, 80, 15, 17, 0, 28, "bodyType3", "codeTip67"));
        entities.add(new WeightTipEntity("女", 30, 80, 17, 18.5f, 0, 28, "bodyType6", "codeTip68"));
        entities.add(new WeightTipEntity("女", 30, 80, 18.5f, 21, 0, 28, "bodyType4", "codeTip69"));
        entities.add(new WeightTipEntity("女", 30, 80, 0, 21, 28, 76, "bodyType10", "codeTip70"));
        entities.add(new WeightTipEntity("女", 30, 80, 21, 28, 28, 76, "bodyType8", "codeTip71"));
        entities.add(new WeightTipEntity("女", 30, 80, 21, 100, 0, 28, "bodyType9", "codeTip72"));
        entities.add(new WeightTipEntity("女", 30, 80, 28, 35, 28, 76, "bodyType12", "codeTip73"));
        entities.add(new WeightTipEntity("女", 30, 80, 35, 75, 28, 76, "bodyType16", "codeTip74"));
        entities.add(new WeightTipEntity("女", 30, 80, 75, 100, 28, 76, "bodyType17", "codeTip75"));
        entities.add(new WeightTipEntity("女", 80, 120, 0, 100, 0, 76, "bodyType15", "codeTip76"));

        create(db, entities);
    }

    /**
     * 反射得到组件的id号
     *
     * @param context
     * @param idName  唯一文件名
     * @return 资源id
     */
    public static int getCompentID(Context context, String idName) {
        return getCompentID(context, "string", idName);
    }

    /**
     * 反射得到组件的id号
     *
     * @param context
     * @param className layout,string,drawable,style,id,color,array
     * @param idName    唯一文件名
     * @return 资源id
     */
    public static int getCompentID(Context context, String className, String idName) {
        int id = 0;
        try {
            Class<?> cls = Class.forName(context.getPackageName() + ".R$" + className);
            id = cls.getField(idName).getInt(cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}
