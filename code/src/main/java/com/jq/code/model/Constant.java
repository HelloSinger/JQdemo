package com.jq.code.model;

import android.annotation.SuppressLint;

import com.jq.code.model.sport.SubmitSportEntity;
import com.jq.code.model.trend.SportDetalisViewEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xulj on 2016/6/13.
 */
public class Constant {
    public static final long ONE_DAY_MS = 24 * 3600L * 1000 ;
    public static final float[] BGLUCOSE_RANGE = {1.1F,7f,13f,20f,27.3f,33.3f} ;
    public static final float[] defalutBGlucose = {3.9f,6.7f,11.1f} ;

    public static final int SNAP_VELOCITY = 300;  //手速最小滚动
    public static final int SCROLL_RANGE = 5 ;    //当移动距离差达到此数目时才认定为滚动。
    public static final int SCROLL_TOUCH_MIN_TIME  = 100  ;  //点击事件最大时间间隔
    public static boolean BloodTypeChange = false ;

    public static final float MAX_BLUCOSE_VALUE =33.3f ;
    public static final float MIN_BLUCOSE_VALUE = 1.1f ;
    public static final float LIMOSIS_BGLUCOSE_VALUE1 = 3.9f ;
    public static final float LIMOSIS_BGLUCOSE_VALUE2 = 6.1f ;
    public static final float BGLUCOSE_NORMAL_VALUE1 = 6.7f ;
    public static final float BGLUCOSE_NORMAL_VALUE2 = 9.4f ;

    public static final int MAX_BPRESS_SYS =300 ;
    public static final int MIN_BPRESS_SYS = 30 ;
    public static final int MAX_BPRESS_DIA =300 ;
    public static final int MIN_BPRESS_DIA = 30 ;
    public static final int  SYS_LEVEL0 = 130 ;
    public static final int  SYS_LEVEL1 = 159 ;
    public static final int  SYS_LEVEL2 = 179 ;
    public static final int  SYS_LEVEL3 = 180 ;
    public static final int  DIA_LEVEL0 = 85 ;
    public static final int  DIA_LEVEL1 = 99 ;
    public static final int  DIA_LEVEL2 = 109 ;
    public static final int  DIA_LEVEL3 = 110 ;

    public static final String FOOD_BASE_TYPE1  = "常用";
    public static final String FOOD_BASE_TYPE2  = "品牌餐饮";

    public static final String SPORT_BASE_TYPE1  = "常用";

    public static final String EVENT_RESET = "重置" ;
    public static final String EVENT_CLEAR = "清除" ;

    public static final boolean closeExerciseFood = false ;
    public static  boolean SHARE_PUNCH_CLOCK = false ;
    public static final String NULL_DATA_DEFAULT = "- -" ;

    public static final int MAX_SPORT_TIME  = 999;
    public static final int MAX_FOOT_BASE_NUM  = 999;
    public static final int MAX_FOOT_NUM  = 100;

    public static final String BREAKFAST_TYPE = "breakfast" ;
    public static final String LUNCH_TYPE = "lunch" ;
    public static final String DINNER_TYPE = "dinner" ;
    public static final String SNACKS_TYPE = "snacks" ;
    public static class Brand{
        public static final String BRAND1 = "麦当劳" ;
        public static final String BRAND2 = "肯德基" ;
        public static final String BRAND3 = "711" ;
        public static final String BRAND4 = "星巴克" ;
        public static final String BRAND5 = "哈根达斯" ;
        public static final String BRAND6 = "德克士" ;
        public static final String BRAND7 = "赛百味" ;
        public static final String BRAND8 = "吉野家" ;
    }
    public static class BiteType{
        public static final String COMMON_USER = "常用"  ;
        public static final String BRAND = "品牌餐饮"  ;
        public static final String STAPLE_FOOD = "主食"  ;
        public static final String DRINKS = "饮料饮品"  ;
        public static final String SNACKS = "零食"  ;
        public static final String VEGETABLES = "蔬菜"  ;
        public static final String FRUITS = "水果"  ;
        public static final String NUT = "坚果"  ;
        public static final String EGG = "肉蛋类"  ;
        public static final String FAST_FOOD = "快餐"  ;
        public static final String CAKE = "饼干糕点"  ;
        public static final String CANDY = "糖果"  ;
    }
    public static class SportType{
        public static final String COMMON_USER = "常用"  ;
        public static final String POWER = "力量运动"  ;
        public static final String BALL = "球类运动"  ;
        public static final String DAILY = "日常活动"  ;
        public static final String AQUATIC_SPORTS = "水上运动"  ;
        public static final String DANCE = "舞蹈武术"  ;
        public static final String OUTDOORS = "户外运动"  ;
        public static final String PLAY = "乐器演奏类"  ;
        public static final String LEISURE = "休闲娱乐"  ;
        public static final String FITNESS = "健身器械类"  ;
        public static final String BODYBUILDING = "健身操和舞蹈"  ;
        public static final String ELSE = "其它体育运动"  ;
        public static final String GARDENING = "园艺活动"  ;
        public static final String WORK = "工作"  ;
        public static final String YOGA = "瑜伽和普拉提"  ;
        public static final String BICYCLE= "自行车运动"  ;
        public static final String RUN= "走路跑步"  ;
    }
    @SuppressLint("LongLogTag")
    public static  List<SportDetalisViewEntity> getSportTrendTopData(String sex , ExerciseDietEntity exerciseDietEntity){
        List<SportDetalisViewEntity> entities = new ArrayList<SportDetalisViewEntity>() ;
        SportDetalisViewEntity breakfastEntity = new SportDetalisViewEntity() ;
        breakfastEntity.setTextStr("早餐");
        breakfastEntity.setValue1(exerciseDietEntity.getBfCalory());
        breakfastEntity.setValue2(getValue2(sex,BREAKFAST_TYPE));
        breakfastEntity.setValue3(getValue3(sex,BREAKFAST_TYPE));
        SportDetalisViewEntity lunchEntity = new SportDetalisViewEntity() ;
        lunchEntity.setTextStr("午餐");
        lunchEntity.setValue1(exerciseDietEntity.getLcCalory());
        lunchEntity.setValue2(getValue2(sex,LUNCH_TYPE));
        lunchEntity.setValue3(getValue3(sex,LUNCH_TYPE));
        SportDetalisViewEntity dinnerEntity = new SportDetalisViewEntity() ;
        dinnerEntity.setTextStr("晚餐");
        dinnerEntity.setValue1(exerciseDietEntity.getDnCalory());
        dinnerEntity.setValue2(getValue2(sex,DINNER_TYPE));
        dinnerEntity.setValue3(getValue3(sex,DINNER_TYPE));
        SportDetalisViewEntity snEntity = new SportDetalisViewEntity() ;
        snEntity.setTextStr("加餐");
        snEntity.setValue1(exerciseDietEntity.getSkCalory());
        snEntity.setValue2(getValue2(sex,SNACKS_TYPE));
        snEntity.setValue3(getValue3(sex,SNACKS_TYPE));
        entities.add(breakfastEntity);
        entities.add(lunchEntity);
        entities.add(dinnerEntity);
        entities.add(snEntity);
        return entities ;
    }
    public static int getValue2(String sex , String type){
        int result = 0 ;
        if(sex .equals("男")){
            if(type.equals(BREAKFAST_TYPE)){
                result = 460 ;
            }else if(type.equals(LUNCH_TYPE)){
                result = 730 ;
            }else if(type.equals(DINNER_TYPE)){
                result = 660 ;
            }else if(type.equals(SNACKS_TYPE)){
                result = 0 ;
            }
        }else {
            if(type.equals(BREAKFAST_TYPE)){
                result = 400 ;
            }else if(type.equals(LUNCH_TYPE)){
                result = 640 ;
            }else if(type.equals(DINNER_TYPE)){
                result = 590 ;
            }else if(type.equals(SNACKS_TYPE)){
                result = 0 ;
            }
        }
        return result ;
    }
    public static int getValue3(String sex , String type){
        int result = 0 ;
        if(sex .equals("男")){
            if(type.equals(BREAKFAST_TYPE)){
                result = 340 ;
            }else if(type.equals(LUNCH_TYPE)){
                result = 540 ;
            }else if(type.equals(DINNER_TYPE)){
                result = 490 ;
            }else if(type.equals(SNACKS_TYPE)){
                result = 0 ;
            }
        }else {
            if(type.equals(BREAKFAST_TYPE)){
                result = 290 ;
            }else if(type.equals(LUNCH_TYPE)){
                result = 470 ;
            }else if(type.equals(DINNER_TYPE)){
                result = 440 ;
            }else if(type.equals(SNACKS_TYPE)){
                result = 0 ;
            }
        }
        return result ;
    }
    public static List<SportDetalisViewEntity> getSportTrendBottomData(ExerciseDietEntity exerciseDietEntity){
        List<SubmitSportEntity> sportEntities = exerciseDietEntity.getSports() ;
        List<SportDetalisViewEntity> entities = new ArrayList<SportDetalisViewEntity>() ;
        if(sportEntities == null) return entities ;
        for (SubmitSportEntity entity: sportEntities) {
            SportDetalisViewEntity temp = new SportDetalisViewEntity() ;
            temp.setTextStr(entity.getName());
            temp.setValue1(entity.getCalory());
            if(entities.contains(temp)){
                SportDetalisViewEntity conEntity = entities.get(entities.indexOf(temp)) ;
                conEntity.setValue1(conEntity.getValue1() + temp.getValue1());
            }else {
                entities.add(temp);
            }
        }
        return entities ;
    }
}
