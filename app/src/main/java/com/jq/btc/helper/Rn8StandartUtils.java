package com.jq.btc.helper;


import com.jq.btc.bluettooth.report.haier.item.Rn8Item;

/**
 * Created by Administrator on 2017/9/11.
 */

public class Rn8StandartUtils {
    public static Rn8Item getRn8Item(boolean isAxunge, float value, boolean isTrunk, int sex, int age){
        Rn8Item rn8Item  = new Rn8Item() ;
        rn8Item.setAxunge(isAxunge);
        rn8Item.setStandard(getStandard(isAxunge,isTrunk,sex,age,value));
        rn8Item.setValue(value);
        return rn8Item ;
    }
    public static int getStandard(boolean isAxunge, boolean isTrunk,int sex, int age,float value){
        int result = -1 ;
        if(isAxunge){
            if(isTrunk){
                float[] standards = axungeTrunkStandard(sex,age) ;
                result = parseStandards(standards,value) ;
            }else {
                float[] standards = axungeLimbsStandard(sex,age) ;
                result = parseStandards(standards,value) ;
            }

        }
        return result ;
    }
    public static int parseStandards(float[] standards,float value){
        if(value <= standards[1]){
            return 0 ;
        }else if(value <= standards[2]){
            return 1 ;
        }else {
            return 2 ;
        }
    }
    public static float[] axungeTrunkStandard(int sex,int age){
        if (age < 40) {
            if (sex==1) {
                return new float[]{0.0f,10.0f,28.0f,80.0f} ;
            } else {
                return new float[]{0.0f,20.0f,39.0f,80.0f} ;
            }
        } else if (age < 60) {
            if (sex==1) {
                return new float[]{0.0f,11.0f,29.0f,80.0f} ;
            } else {
                return new float[]{0.0f,21.0f,40.0f,80.0f} ;
            }
        } else {
            if (sex==1) {
                return new float[]{0.0f,12.0f,30.0f,80.0f} ;
            } else {
                return new float[]{0.0f,22.0f,41.0f,80.0f} ;
            }
        }
    }
    public static float[] axungeLimbsStandard(int sex,int age){
        if (age < 40) {
            if (sex==1) {
                return new float[]{0.0f,10.0f,24.0f,80.0f} ;
            } else {
                return new float[]{0.0f,20.0f,37.0f,80.0f} ;
            }
        } else if (age < 60) {
            if (sex==1) {
                return new float[]{0.0f,11.0f,25.0f,80.0f} ;
            } else {
                return new float[]{0.0f,21.0f,38.0f,80.0f} ;
            }
        } else {
            if (sex==1) {
                return new float[]{0.0f,12.0f,26.0f,80.0f} ;
            } else {
                return new float[]{0.0f,22.0f,39.0f,80.0f} ;
            }
        }
    }
}
