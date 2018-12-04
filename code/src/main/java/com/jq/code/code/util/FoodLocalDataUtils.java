package com.jq.code.code.util;

import android.content.Context;

import com.jq.code.code.db.FoodLocalDB;
import com.jq.code.model.Constant;
import com.jq.code.model.sport.BiteEnty;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/10/18.
 */

public class FoodLocalDataUtils {
    private FoodLocalDB localDB  ;
    public FoodLocalDataUtils(Context context){
        localDB = FoodLocalDB.getInstance(context) ;
    }
    public List<String> getTypeName(){
        String[] types = new String[]{"常用","品牌餐饮","主食","肉蛋类","蔬菜","水果","快餐","零食","坚果","饮料饮品","饼干糕点","糖果"};
        return Arrays.asList(types);
//        return localDB.getFoodType() ;
    }
    public List<String> getBrandName(){
        return localDB.getBrandType() ;
    }
    public List<BiteEnty> getBrandBite(String brandName){
        return localDB.getBrandBite(brandName) ;
    }
    public List<BiteEnty> getBiteDataFormType(String type){
        if(type.equals(Constant.FOOD_BASE_TYPE1)){
            return localDB.getOftenLocalBite() ;
        }else {
            return localDB.getLocalBiteFromType(type) ;
        }
    }
    public void insertNewBite(BiteEnty enty){
        localDB.insertNewFoot(enty);
    }
}
