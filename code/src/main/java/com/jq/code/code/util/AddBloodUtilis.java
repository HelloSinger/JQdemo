package com.jq.code.code.util;

import android.content.Context;

import com.jq.code.R;
import com.jq.code.model.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xulj on 2016/7/8.
 */
public class AddBloodUtilis {

    public static List<String>  getBGlucoseCommentFromLevel(int level){
        List<String> result  = new ArrayList<String>();
        switch (level){
            case 1:
                result.add("空腹");
                break;
            case 2:
                result.add("空腹");
                result.add("早餐后");
                break;
            case 3:
                result.add("空腹");
                result.add("早餐后");
                result.add("午餐前");
                break;
            case 4:
                result.add("早餐后");
                result.add("午餐前");
                result.add("午餐后");
                break;
            case 5:
                result.add("午餐前");
                result.add("午餐后");
                result.add("晚餐前");
                break;
            case 6:
                result.add("午餐后");
                result.add("晚餐前");
                result.add("晚餐后");
                break;
            case 7:
                result.add("晚餐前");
                result.add("晚餐后");
                break;
        }
        return result ;
    }
    public static int getBGlucoseCommentLevel(String time){
       int level = 1 ;
       long timestamp =TimeUtil.getTimestamp(time,"HH:mm") ;
       if(isLevel1(timestamp)) {
           level = 1 ;
       }else if(isLevel2(timestamp)){
           level = 2 ;
       }else if(isLevel3(timestamp)){
           level = 3 ;
       }else if(isLevel4(timestamp)){
           level = 4 ;
       }else if(isLevel5(timestamp)){
           level = 5 ;
       }else if(isLevel6(timestamp)){
           level = 6 ;
       }else if(isLevel7(timestamp)){
           level = 7 ;
       }
       return  level ;
    }
    public static List<String> getBGlucoseComment(String time){
        return  getBGlucoseCommentFromLevel(getBGlucoseCommentLevel(time)) ;
    }
    public static boolean isLevel1(long timestamp){
        long minStatmp = TimeUtil.getTimestamp("00:00","HH:mm")  ;
        long maxStatmp = TimeUtil.getTimestamp("04:44","HH:mm") ;
        return timestamp >=minStatmp && timestamp <= maxStatmp  ;
    }
    public static boolean isLevel2(long timestamp){
        long minStatmp = TimeUtil.getTimestamp("04:45","HH:mm")  ;
        long maxStatmp = TimeUtil.getTimestamp("07:45","HH:mm") ;
        return timestamp >=minStatmp && timestamp <= maxStatmp  ;
    }
    public static boolean isLevel3(long timestamp){
        long minStatmp = TimeUtil.getTimestamp("07:46","HH:mm")  ;
        long maxStatmp = TimeUtil.getTimestamp("10:15","HH:mm") ;
        return timestamp >=minStatmp && timestamp <= maxStatmp  ;
    }
    public static boolean isLevel4(long timestamp){
        long minStatmp = TimeUtil.getTimestamp("10:16","HH:mm")  ;
        long maxStatmp = TimeUtil.getTimestamp("12:45","HH:mm") ;
        return timestamp >=minStatmp && timestamp <= maxStatmp  ;
    }
    public static boolean isLevel5(long timestamp){
        long minStatmp = TimeUtil.getTimestamp("12:46","HH:mm")  ;
        long maxStatmp = TimeUtil.getTimestamp("15:45","HH:mm") ;
        return timestamp >=minStatmp && timestamp <= maxStatmp  ;
    }
    public static boolean isLevel6(long timestamp){
        long minStatmp = TimeUtil.getTimestamp("15:46","HH:mm")  ;
        long maxStatmp = TimeUtil.getTimestamp("18:45","HH:mm") ;
        return timestamp >=minStatmp && timestamp <= maxStatmp  ;
    }
    public static boolean isLevel7(long timestamp){
        long minStatmp = TimeUtil.getTimestamp("18:46","HH:mm")  ;
        long maxStatmp = TimeUtil.getTimestamp("23:59","HH:mm") ;
        return timestamp >=minStatmp && timestamp <= maxStatmp  ;
    }
    public static int  getLimosisLevel(float value){
        int result = 0 ;
        if(value < Constant.LIMOSIS_BGLUCOSE_VALUE1){
            result = -1 ;
        }else if(value > Constant.LIMOSIS_BGLUCOSE_VALUE2){
            result = 1 ;
        }
        return result ;
    }
    public static String getGlucoseHintForLevel(Context context,int level){
        String result = context.getString(R.string.bGucoseLevelHint2) ;
        if(level == -1){
            result = context.getString(R.string.bGucoseLevelHint1) ;
        }else if(level == 1){
            result = context.getString(R.string.bGucoseLevelHint3) ;
        }
        return  result ;
    }
    public static int  getGlucoseLevel(float value){
        int result = 0 ;
        if(value < Constant.BGLUCOSE_NORMAL_VALUE1){
            result = -1 ;
        }else if(value > Constant.BGLUCOSE_NORMAL_VALUE2){
            result = 1 ;
        }
        return result ;
    }
    public static String getBPressHintForLevel(Context context,int level){
        String result = context.getString(R.string.bPressLevelHint0) ;
        if(level == 1){
            result = context.getString(R.string.bPressLevelHint1) ;
        }else if(level == 2){
            result = context.getString(R.string.bPressLevelHint2) ;
        }else if(level == 3){
            result = context.getString(R.string.bPressLevelHint3) ;
        }
        return  result ;
    }
    public static int  getBPressLevel(int sys,int dia){
        int result = 0 ;
        int sysLeve1 = 0 ;
        int diaLevel = 0 ;
        if(sys < Constant.SYS_LEVEL0){
            sysLeve1 = 0 ;
        }else if(sys >= Constant.SYS_LEVEL0 && sys <= Constant.SYS_LEVEL1){
            sysLeve1 = 1 ;
        }else if(sys >Constant.SYS_LEVEL1 && sys<= Constant.SYS_LEVEL2){
            sysLeve1 = 2 ;
        }else{
            sysLeve1 = 3 ;
        }
        if(dia < Constant.DIA_LEVEL0){
            diaLevel = 0 ;
        }else if(dia >= Constant.DIA_LEVEL0 && dia <= Constant.DIA_LEVEL1){
            diaLevel = 1 ;
        }else if(dia >Constant.DIA_LEVEL1 && dia<= Constant.DIA_LEVEL2){
            diaLevel = 2 ;
        }else{
            diaLevel = 3 ;
        }
        if(sysLeve1 == diaLevel){
            result = sysLeve1 ;
        }else {
            result = sysLeve1 > diaLevel ? sysLeve1 : diaLevel ;
        }
        return result ;
    }
    public static int getGlucoseStatForLevel(int layoutWidth ,float currValue1,float currValue2, float bsl,int currLevel){
        int result = 0 ;
        if(currLevel == -1){
            float yifen =  (layoutWidth/3) /(currValue1 - Constant.MIN_BLUCOSE_VALUE);
             result = (int) (yifen * (bsl - Constant.MIN_BLUCOSE_VALUE));
        }else if(currLevel == 0){
            float yifen =  (layoutWidth/3) /(currValue2 - currValue1);
            result = (int) (yifen * (bsl - currValue1));
            result = layoutWidth/3 + result ;
        }else if(currLevel == 1){
            float yifen =  (layoutWidth/3) /(Constant.MAX_BLUCOSE_VALUE - currValue2);
            result = (int) (yifen * (bsl - currValue2));
            result = 2 * layoutWidth/3 + result ;
        }
        return result  ;
    }
}
