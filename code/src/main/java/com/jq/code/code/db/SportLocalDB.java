package com.jq.code.code.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jq.code.code.util.LogUtil;
import com.jq.code.model.sport.SportEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 从本地数据库文件foodandsports.db拷贝得到的 运动表
 * Created by xulj on 2016/7/29.
 */
public class SportLocalDB {


    private LocalDB mDBUtil;
    /**
     * 析构函数
     */
    private SportLocalDB() {
        mDBUtil = new LocalDB();
    }
    /**
     * 得到当前实体类
     */
    public static SportLocalDB getInstance(Context context) {
        SportLocalDB instance = new SportLocalDB();
        return instance;
    }

    public List<SportEntity> getAllSprot(){
        List<SportEntity> result = new ArrayList<SportEntity>();
        Cursor c = null;
        SQLiteDatabase db = mDBUtil.getSQLiteDatabase();
        String sql = "select * from exercise";
        c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            SportEntity entity = parserSport(c) ;
            if(!result.contains(entity)){
                result.add(entity) ;
            }
        }
        c.close();
        db.close();
        return result ;
    }
    public List<SportEntity> getSprotByType(String type){
        List<SportEntity> result = new ArrayList<SportEntity>();
        Cursor c = null;
        SQLiteDatabase db = mDBUtil.getSQLiteDatabase();
        String sql = "select * from exercise where type=?";
        c = db.rawQuery(sql, new String[]{type});
        while (c.moveToNext()) {
            result.add(parserSport(c)) ;
        }
        c.close();
        db.close();
        return result ;
    }

    public SportEntity parserSport(Cursor c){
        SportEntity sportEntity = new SportEntity();
        sportEntity.setId(c.getInt(c.getColumnIndex("id")));
        sportEntity.setMet(c.getFloat(c.getColumnIndex("met")));
        sportEntity.setName(c.getString(c.getColumnIndex("name")));
        sportEntity.setType(c.getString(c.getColumnIndex("type")));
        sportEntity.setUsedegree(c.getInt(c.getColumnIndex("usedegree")));
        return sportEntity ;
    }
    public void insertNewFoot(SportEntity sportEnty) {
        Cursor c = null;
        SQLiteDatabase db = mDBUtil.getSQLiteDatabase();
        String sql = "select * from exercise where id=? and type=?";
        c = db.rawQuery(sql, new String[]{sportEnty.getId() + "",sportEnty.getType()});
        if (c.getCount() == 0) {
            db.insertWithOnConflict("exercise", null, creatContentValue(sportEnty), 0);
            LogUtil.e("footLocalDb", "插入数据成功");
        }
        c.close();
        db.close();
    }
    public ContentValues creatContentValue(SportEntity sportEnty) {
        ContentValues values = new ContentValues();
        values.put("id",sportEnty.getId());
        values.put("met",sportEnty.getMet()) ;
        values.put("name",sportEnty.getName());
        values.put("type",sportEnty.getType());
        values.put("usedegree",sportEnty.getUsedegree());
        return values;
    }
}
