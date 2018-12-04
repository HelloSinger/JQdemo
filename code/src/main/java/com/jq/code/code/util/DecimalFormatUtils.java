package com.jq.code.code.util;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/2/22.
 */

public class DecimalFormatUtils {
    public static String getDecimalNumber(double value,int number){
        String result = "" ;
        switch (number){
            case 0:
             result = getZero(value) ;
             break;
            case 1:
                result = getOne(value) ;
                break;
            case 2:
                result = getTwo(value) ;
                break;
            case 3:
                result = getThree(value) ;
                break;
        }
     return result ;
    }
    public static String getZero(double value){
        return  new DecimalFormat("#0").format(value) ;
    }
    public static String getOne(double value){
        return  new DecimalFormat("#0.0").format(value) ;
    }
    public static String getTwo(double value){
        return  new DecimalFormat("#0.00").format(value) ;
    }
    public static String getThree(double value){
        return  new DecimalFormat("#0.000").format(value) ;
    }
}
