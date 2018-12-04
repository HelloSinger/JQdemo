package com.jq.code.code.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.jq.code.code.util.FoodAndSportUtilis;
import com.jq.code.code.util.LogUtil;
import com.jq.code.model.sport.BiteEnty;
import com.jq.code.model.sport.FoodMicroelementEntity;
import com.jq.code.model.sport.SubmitFoodEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 从本地数据库文件foodandsports.db拷贝得到的 食物表
 * Created by xulj on 2016/7/29.
 */
public class FoodLocalDB {


    private LocalDB mDBUtil;

    /**
     * 析构函数
     */
    private FoodLocalDB() {
        mDBUtil = new LocalDB();
    }

    /**
     * 得到当前实体类
     */
    public static FoodLocalDB getInstance(Context context) {
        FoodLocalDB instance = new FoodLocalDB();
        return instance;
    }

    public FoodMicroelementEntity findBiteEnty(SubmitFoodEntity entity) {
        SQLiteDatabase db = mDBUtil.getSQLiteDatabase();
        String sql = "select * from food where name=? and id=? order by update_time desc";
        Cursor c = db.rawQuery(sql, new String[]{String.valueOf(entity.getName()), String.valueOf(entity.getFood_id())});
        FoodMicroelementEntity biteEnty = null;
        while (c.moveToNext()) {
            biteEnty = getContentValue(c);
        }
        c.close();
        db.close();
        return biteEnty;
    }

    public List<String> getBrandType() {
        List<String> types = new ArrayList<String>();
        Cursor c = null;
        SQLiteDatabase db = mDBUtil.getSQLiteDatabase();
        String sql = "select distinct brand from food";
        c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            String type = c.getString(c.getColumnIndex("brand"));
            if (!TextUtils.isEmpty(type)) {
                types.add(type);
            }
        }
        c.close();
        db.close();
        return types;
    }

    public List<BiteEnty> getLocalBiteFromType(String type) {
        List<BiteEnty> result = new ArrayList<BiteEnty>();
        Cursor c = null;
        SQLiteDatabase db = mDBUtil.getSQLiteDatabase();
        String sql = "select * from food where type=? order by update_time desc";
        c = db.rawQuery(sql, new String[]{type});
        while (c.moveToNext()) {
            result.add(parserBit(c));
        }
        c.close();
        db.close();
        return result;
    }

    public List<BiteEnty> getBrandBite(String brandName) {
        List<BiteEnty> result = new ArrayList<BiteEnty>();
        Cursor c = null;
        SQLiteDatabase db = mDBUtil.getSQLiteDatabase();
        String sql = "select * from food where brand=? order by update_time desc";
        c = db.rawQuery(sql, new String[]{brandName});
        while (c.moveToNext()) {
            result.add(parserBit(c));
        }
        c.close();
        db.close();
        return result;
    }

    public List<BiteEnty> getOftenLocalBite() {
        List<BiteEnty> result = new ArrayList<BiteEnty>();
        Cursor c = null;
        SQLiteDatabase db = mDBUtil.getSQLiteDatabase();
        String sql = "select * from food where isfrequentused=1 order by update_time desc limit 100";
        c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            result.add(parserBit(c));
        }
        c.close();
        db.close();
        return result;
    }

    public BiteEnty parserBit(Cursor c) {
        BiteEnty enty = new BiteEnty();
        enty.setId(c.getLong(c.getColumnIndex("id")));
        enty.setName(c.getString(c.getColumnIndex("name")));
        enty.setIs_liquid(c.getInt(c.getColumnIndex("is_liquid")) == 1);
        enty.setCalory(c.getFloat(c.getColumnIndex("calory")));
        enty.setUnits(c.getString(c.getColumnIndex("units")));
        enty.setBrand(c.getString(c.getColumnIndex("brand")));
        enty.setUnitList(FoodAndSportUtilis.getUnitEnity(enty));
        return enty;
    }

    public void create(BiteEnty biteEnty) {
        biteEnty.setUpdate_time(System.currentTimeMillis());
        synchronized (mDBUtil) {
            SQLiteDatabase db = mDBUtil.getSQLiteDatabase();
            db.insertWithOnConflict("food", null, creatContentValue(biteEnty), SQLiteDatabase.CONFLICT_IGNORE);
            db.close();
        }
    }


    public void modity(BiteEnty biteEnty) {
        biteEnty.setUpdate_time(System.currentTimeMillis());
        synchronized (mDBUtil) {
            SQLiteDatabase db = mDBUtil.getSQLiteDatabase();
            db.update("food", creatContentValue(biteEnty), "id=?", new String[]{String.valueOf(biteEnty.getId())});
            db.close();
        }
    }

    public void insertNewFoot(BiteEnty biteEnty) {
        Cursor c = null;
        SQLiteDatabase db = mDBUtil.getSQLiteDatabase();
        String sql = "select * from food where id=?";
        c = db.rawQuery(sql, new String[]{biteEnty.getId() + ""});
        if (c.getCount() == 0) {
            db.insertWithOnConflict("food", null, creatContentValue(biteEnty), 0);
            LogUtil.e("footLocalDb", "插入数据成功");
        }
        c.close();
        db.close();
    }

    private FoodMicroelementEntity getContentValue(Cursor c) {
        FoodMicroelementEntity enty = new FoodMicroelementEntity();
        enty.setId(c.getInt(c.getColumnIndex("id")));
        enty.setWeight(c.getFloat(c.getColumnIndex("weight")));
        enty.setName(c.getString(c.getColumnIndex("name")));
        enty.setIs_liquid(c.getInt(c.getColumnIndex("is_liquid")) == 1);
        enty.setCalory(c.getFloat(c.getColumnIndex("calory")));
        enty.setUnits(c.getString(c.getColumnIndex("units")));
        enty.setBrand(c.getString(c.getColumnIndex("brand")));
        enty.setNiacin(c.getFloat(c.getColumnIndex("niacin")));
        enty.setNatrium(c.getFloat(c.getColumnIndex("natrium")));
        enty.setThiamine(c.getFloat(c.getColumnIndex("thiamine")));
        enty.setCarbohydrate(c.getFloat(c.getColumnIndex("carbohydrate")));
        enty.setManganese(c.getFloat(c.getColumnIndex("manganese")));
        enty.setVitamin_c(c.getFloat(c.getColumnIndex("vitamin_c")));
        enty.setIodine(c.getFloat(c.getColumnIndex("iodine")));
        enty.setProtein(c.getFloat(c.getColumnIndex("protein")));
        enty.setVitamin_e(c.getFloat(c.getColumnIndex("vitamin_e")));
        enty.setUsedegree(c.getInt(c.getColumnIndex("usedegree")));
        enty.setVitamin_a(c.getFloat(c.getColumnIndex("vitamin_a")));
        enty.setLactoflavin(c.getFloat(c.getColumnIndex("lactoflavin")));
        enty.setFiber_dietary(c.getFloat(c.getColumnIndex("fiber_dietary")));
        enty.setThumb_image_url(c.getString(c.getColumnIndex("thumb_image_url")));
        enty.setCholesterol(c.getFloat(c.getColumnIndex("cholesterol")));
        enty.setIron(c.getFloat(c.getColumnIndex("iron")));
        enty.setKalium(c.getFloat(c.getColumnIndex("kalium")));
        enty.setCalcium(c.getFloat(c.getColumnIndex("calcium")));
        enty.setMagnesium(c.getFloat(c.getColumnIndex("magnesium")));
        enty.setFat(c.getFloat(c.getColumnIndex("fat")));
        enty.setType(c.getString(c.getColumnIndex("type")));
        enty.setSelenium(c.getFloat(c.getColumnIndex("selenium")));
        enty.setZinc(c.getFloat(c.getColumnIndex("zinc")));
        enty.setCopper(c.getFloat(c.getColumnIndex("copper")));
        enty.setUpdate_time(c.getLong(c.getColumnIndex("update_time")));
        return enty;
    }

    public ContentValues creatContentValue(BiteEnty biteEnty) {
        ContentValues values = new ContentValues();
        values.put("id", biteEnty.getId());
        values.put("weight", biteEnty.getWeight());
        values.put("name", biteEnty.getName());
        values.put("is_liquid", biteEnty.is_liquid() ? "1" : "0");
        values.put("calory", biteEnty.getCalory());
        values.put("units", biteEnty.getUnits());
        values.put("brand", biteEnty.getBrand());
        values.put("isfrequentused", "1");
        values.put("niacin", biteEnty.getNiacin());
        values.put("natrium", biteEnty.getNatrium());
        values.put("thiamine", biteEnty.getThiamine());
        values.put("carbohydrate", biteEnty.getCarbohydrate());
        values.put("manganese", biteEnty.getManganese());
        values.put("vitamin_c", biteEnty.getVitamin_c());
        values.put("iodine", biteEnty.getIodine());
        values.put("protein", biteEnty.getProtein());
        values.put("vitamin_e", biteEnty.getVitamin_e());
        values.put("usedegree", biteEnty.getUsedegree());
        values.put("vitamin_a", biteEnty.getVitamin_a());
        values.put("lactoflavin", biteEnty.getLactoflavin());
        values.put("fiber_dietary", biteEnty.getFiber_dietary());
        values.put("thumb_image_url", biteEnty.getThumb_image_url());
        values.put("cholesterol", biteEnty.getCholesterol());
        values.put("iron", biteEnty.getIron());
        values.put("kalium", biteEnty.getKalium());
        values.put("calcium", biteEnty.getCalcium());
        values.put("magnesium", biteEnty.getMagnesium());
        values.put("fat", biteEnty.getFat());
        values.put("type", biteEnty.getType());
        values.put("selenium", biteEnty.getSelenium());
        values.put("zinc", biteEnty.getZinc());
        values.put("copper", biteEnty.getCopper());
        values.put("update_time", biteEnty.getUpdate_time());
        return values;
    }
}
