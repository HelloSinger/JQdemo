package com.jq.btlib.util;

import com.jq.btlib.model.WeightParserReturn;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by lixun on 2015/12/29.
 */

public class WeightUnitUtil {


    public static float KG2JIN(float kgWeight) {
        return kgWeight * 2;
    }

    public static String KG2ST(float kgWeight) {
        float f1 = KG2LB(kgWeight);
        int b1 = (int) (f1 / 14);
        float f2 = f1 % 14;
        BigDecimal bigConvert = new BigDecimal(f2);
        f2 = bigConvert.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        return b1 + ":" + f2;
    }

    public static String KG2STVer2(float kgWeight) {
        float f1 = kgWeight * 2.2046226f;
        int b1 = (int) (f1 / 14);
        float f2 = f1 % 14;
        BigDecimal bigConvert = new BigDecimal(f2);
        f2 = bigConvert.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        return b1 + ":" + f2;
    }

    public static String LB2ST(float lbWeight) {
        float f1 = lbWeight;
        int b1 = (int) (f1 / 14);
        float f2 = f1 % 14;
        BigDecimal bigConvert = new BigDecimal(f2);
        f2 = bigConvert.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        return b1 + ":" + f2;
    }

    public static float JIN2KG(float jinWeight) {
        return jinWeight * 0.5f;
    }

    //威盛康kg转lb公式,适用于MCU的处理方式，所有除法后都要将小数位去掉
    public static float KG2LB_WSK(float kgWeight) {
        float weight1 = kgWeight * 10;
        int iWeight = (int) ((weight1 * 11023) / 5000);
        int iWeight1 = (iWeight + 1) / 2;
        float weight2 = ((float) (iWeight1 * 2) / 10f);
        return weight2;
    }

    public static float KG2LB(float kgWeight) {
        //return kgWeight * 2.2046226f;
        float f1 = KG2LB_WSK(kgWeight);
        return f1;

    }

    public static float LB2KG(float lbWeight) {
        return lbWeight * 0.4535924f;
    }

    public static float ST2LB(String stWeight) {
        float fRet = 0;
        String[] s1 = stWeight.split(":");
        if (s1.length == 2) {
            fRet = (Integer.parseInt(s1[0]) * 14 + Float.parseFloat(s1[1]));
        }
        return fRet;
    }

    public static float ST2KG(String stWeight) {
        float fRet = 0;
        String[] s1 = stWeight.split(":");
        if (s1.length == 2) {
            float lbValue = (Integer.parseInt(s1[0]) * 14 + Float.parseFloat(s1[1]));
            fRet = LB2KG(lbValue);
        }
//        DecimalFormat df1 =new DecimalFormat("#0.0");
//        fRet = Float.parseFloat(df1.format(fRet));
        BigDecimal b = new BigDecimal(fRet);
        fRet = b.setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
        return fRet;
    }

    public static WeightParserReturn Parser(byte highWeight, byte lowWeight, byte scaleProperty,boolean isCloudProtocal) {
        CsBtUtil_v11.Weight_Unit scaleUnit = BytesUtil.getUnit(scaleProperty);
        CsBtUtil_v11.Weight_Digit scaleDigit;
        if(isCloudProtocal){
            scaleDigit = BytesUtil.getCloudDigit(scaleProperty);
        }else{
            scaleDigit = BytesUtil.getDigit(scaleProperty);
        }

        WeightParserReturn parserReturn = new WeightParserReturn();

        byte[] bWeight1 = new byte[2];
        bWeight1[0] = highWeight;
        bWeight1[1] = lowWeight;

        int iDigit = 0;
        float scaleTmpWeight = 0;
        if (scaleDigit == CsBtUtil_v11.Weight_Digit.ONE) {
            scaleTmpWeight = BytesUtil.bytesToInt(bWeight1) / 10f;
            iDigit = 1;
        } else if (scaleDigit == CsBtUtil_v11.Weight_Digit.TWO) {
            scaleTmpWeight = BytesUtil.bytesToInt(bWeight1) / 100f;
            iDigit = 2;
        } else {
            scaleTmpWeight = BytesUtil.bytesToInt(bWeight1);
        }
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');

        BigDecimal bigConvert = new BigDecimal(scaleTmpWeight);
        scaleTmpWeight = bigConvert.setScale(iDigit, BigDecimal.ROUND_HALF_UP).floatValue();
        if (scaleUnit == CsBtUtil_v11.Weight_Unit.ST) {
            parserReturn.scaleWeight = (highWeight & 0xff) + ":" + (lowWeight & 0xff) / 10f;
        } else {
            if (iDigit == 0) {
                parserReturn.scaleWeight = "" + (int) scaleTmpWeight;
            } else if (iDigit == 1) {

                DecimalFormat df1 = new DecimalFormat("#0.0", symbols);
                parserReturn.scaleWeight = df1.format(scaleTmpWeight);
            } else if (iDigit == 2) {
                //parserReturn.scaleWeight="" + scaleTmpWeight;
                DecimalFormat df2 = new DecimalFormat("#0.00", symbols);
                parserReturn.scaleWeight = df2.format(scaleTmpWeight);

            }
        }

        int iKgDigit=2; //数据库默认为两位小数，故默认保留两位小数
        if (scaleUnit == CsBtUtil_v11.Weight_Unit.JIN) {
            parserReturn.kgWeight = JIN2KG(scaleTmpWeight);
        } else if (scaleUnit == CsBtUtil_v11.Weight_Unit.LB) {
            parserReturn.kgWeight = LB2KG(scaleTmpWeight);
        } else if (scaleUnit == CsBtUtil_v11.Weight_Unit.ST) {
            parserReturn.kgWeight = ST2KG(parserReturn.scaleWeight);
        } else {
            iKgDigit=iDigit;
            parserReturn.kgWeight = scaleTmpWeight;
        }

        bigConvert = new BigDecimal(parserReturn.kgWeight);
        parserReturn.kgWeight = bigConvert.setScale(iDigit, BigDecimal.ROUND_HALF_UP).doubleValue();

        return parserReturn;
    }

}
