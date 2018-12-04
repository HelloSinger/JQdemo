package com.jq.code.code.util;

import android.content.Context;

import com.jq.btlib.util.BytesUtil;
import com.jq.btlib.util.CsBtUtil_v11;
import com.jq.btlib.util.WeightUnitUtil;
import com.jq.code.R;
import com.jq.code.code.business.Config;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 标准范围获取的工具类
 */
public class StandardUtil {


    /**
     * 是否为汉字
     *
     * @param c
     * @return
     */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    /**
     * 名字是否合法
     *
     * @param name
     * @return
     */
    public static boolean isNameOk(String name) {
        Pattern p = Pattern
                .compile("^[\u4E00-\u9FA5A-Za-z0-9_!\"#$%&'() *+,-/:;<>=?@\\^`{}|~！@￥……（）——。，‘’“”；：]+$");
        Matcher m = p.matcher(name);
        return m.matches();

    }

    /**
     * 检查字符串长度
     *
     * @param text
     * @param limitNum
     * @return true 超出
     */
    public static boolean isTextLengthLimitOver(String text, int limitNum) {
        char[] cs = text.toCharArray();
        int leng = 0;
        for (int i = 0; i < cs.length; i++) {
            if (isChinese(cs[i])) {
                leng += 2;
            } else {
                leng += 1;
            }
        }
        return leng > limitNum;

    }

    /**
     * 手机号码是否合法
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^1\\d{10}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * Email是否合法
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        Pattern p = Pattern
                .compile("[A-Z0-9a-z._%+-]+@[A-Z0-9a-z._-]+\\.[A-Za-z]{2,4}");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 获取转换后的单位
     *
     * @return
     */
    public static String getWeightExchangeUnit(Context context) {
        int weightUnit = Config.getInstance(context).getIntWeightUnit();
        int unit = R.string.kilo;
        if (weightUnit == Config.JIN) {
            unit = R.string.jin;
        } else if (weightUnit == Config.INCH) {
            unit = R.string.pounds;
        } else if (weightUnit == Config.ST) {
            unit = R.string.st;
        }
        return context.getString(unit);
    }


    public static boolean isChinese(Context context) {
        String localLan = context.getResources().getConfiguration().locale
                .getDisplayLanguage();
        return localLan.equals(Locale.CHINESE.getDisplayLanguage());
    }


    /**
     * 获取转换后的值
     *
     * @param value
     * @return
     */
    public static float getWeightExchangeValue(Context context, float value) {
        int weightUnit = Config.getInstance(context).getIntWeightUnit();
        if (weightUnit == Config.JIN) {
            value = WeightUnitUtil.KG2JIN(value);  //value * 2;
        } else if (weightUnit == Config.INCH) {
            //value = StandardUtil.getInstance(context).getExchangeWeight(value,
            //		"KG", "LB");
            value = WeightUnitUtil.KG2LB(value);
        }

        //增加多语言问题兼容，法语
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        return Float.valueOf(new DecimalFormat("##0.0", symbols).format(value));
    }

    /**
     * 获取转换后的值(为2.0版本的初始体重和目标体重新增，kg转lb公式不一样)
     *
     * @param value
     * @param scaleWeight
     * @param scaleProp
     * @return
     */
    public static String getWeightExchangeValueforVer2(Context context, float value, String scaleWeight, byte scaleProp) {
        CsBtUtil_v11.Weight_Digit scaleDigit = BytesUtil.getDigit(scaleProp);
        CsBtUtil_v11.Weight_Unit scaleUnit = BytesUtil.getUnit(scaleProp);
        CsBtUtil_v11.Weight_Unit appUnit = CsBtUtil_v11.Weight_Unit.KG;
        String sRet = scaleWeight;
        int weightUnit = Config.getInstance(context).getIntWeightUnit();
        float exchangeValue = value;
        if (weightUnit == Config.JIN) {
            appUnit = CsBtUtil_v11.Weight_Unit.JIN;
            exchangeValue = WeightUnitUtil.KG2JIN(value);
        } else if (weightUnit == Config.INCH) {
            appUnit = CsBtUtil_v11.Weight_Unit.LB;
            exchangeValue = value * 2.2046226f;
        } else if (weightUnit == Config.ST) {
            appUnit = CsBtUtil_v11.Weight_Unit.ST;
        }

        boolean bExchange = true;
        if (appUnit == scaleUnit) {
            if (scaleWeight != null) {
                if (scaleWeight.length() > 0) {
                    bExchange = false;
                }
            }
        }

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        if (bExchange) {
            BigDecimal bigConvert;
            if (appUnit == CsBtUtil_v11.Weight_Unit.ST) {
                sRet = WeightUnitUtil.KG2STVer2(value);
            } else {
                if (scaleDigit == CsBtUtil_v11.Weight_Digit.ZERO) {
                    sRet = "" + (int) exchangeValue;
                } else if (scaleDigit == CsBtUtil_v11.Weight_Digit.ONE) {
                    //bigConvert=new BigDecimal(exchangeValue);
                    //sRet="" + bigConvert.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
                    DecimalFormat df2 = new DecimalFormat("#0.0", symbols);
                    sRet = df2.format(exchangeValue);
                } else {
                    //bigConvert=new BigDecimal(exchangeValue);
                    //sRet="" + bigConvert.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                    DecimalFormat df2 = new DecimalFormat("#0.00", symbols);
                    sRet = df2.format(exchangeValue);
                }
            }
        }
        return sRet;
    }

    /**
     * 获取转换后的值
     *
     * @param value
     * @param scaleWeight
     * @param scaleProp
     * @return
     */
    public static String getWeightExchangeValue(Context context, float value, String scaleWeight, byte scaleProp) {
        CsBtUtil_v11.Weight_Digit scaleDigit = BytesUtil.getDigit(scaleProp);
        CsBtUtil_v11.Weight_Unit scaleUnit = BytesUtil.getUnit(scaleProp);
        CsBtUtil_v11.Weight_Unit appUnit = CsBtUtil_v11.Weight_Unit.KG;
        String sRet = scaleWeight;
        int weightUnit = Config.getInstance(context).getIntWeightUnit();
        float exchangeValue = value;
        if (weightUnit == Config.JIN) {
            appUnit = CsBtUtil_v11.Weight_Unit.JIN;
            exchangeValue = WeightUnitUtil.KG2JIN(value);
        } else if (weightUnit == Config.INCH) {
            appUnit = CsBtUtil_v11.Weight_Unit.LB;
            exchangeValue = WeightUnitUtil.KG2LB(value);
        } else if (weightUnit == Config.ST) {
            appUnit = CsBtUtil_v11.Weight_Unit.ST;
        }

        boolean bExchange = true;
        if (appUnit == scaleUnit) {
            if (scaleWeight != null) {
                if (scaleWeight.length() > 0) {
                    bExchange = false;
                }
            }
        }

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        if (bExchange) {
            BigDecimal bigConvert;
            if (appUnit == CsBtUtil_v11.Weight_Unit.ST) {
                sRet = WeightUnitUtil.KG2ST(value);
            } else {
                if (scaleDigit == CsBtUtil_v11.Weight_Digit.ZERO) {
                    sRet = "" + (int) exchangeValue;
                } else if (scaleDigit == CsBtUtil_v11.Weight_Digit.ONE) {
                    //bigConvert=new BigDecimal(exchangeValue);
                    //sRet="" + bigConvert.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
                    DecimalFormat df2 = new DecimalFormat("#0.0", symbols);
                    sRet = df2.format(exchangeValue);
                } else {
                    //bigConvert=new BigDecimal(exchangeValue);
                    //sRet="" + bigConvert.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                    DecimalFormat df2 = new DecimalFormat("#0.00", symbols);
                    sRet = df2.format(exchangeValue);
                }
            }
        }
        return sRet;
    }


    /**
     * 重量单位转换函数(此函数已过期，请使用libBT中WeightUnitUtil相关重量转换函数，保持统一)
     *
     * @param weight       当前重量
     * @param curUnit      当前重量单位
     * @param exchangeUnit 需要转换为的重量单位
     *                     <p/>
     *                     单位分别为 ："KG" "LB"两种
     */
    public static float getExchangeWeight(float weight, String curUnit,
                                          String exchangeUnit) {
        float exchangeWeight = weight;
        if (curUnit.equals("KG")) {
            if (exchangeUnit.equals("LB")) {
                exchangeWeight = WeightUnitUtil.KG2LB(weight); //(float) (weight / 0.453);
            } else if (exchangeUnit.equals("KG")) {
                exchangeWeight = weight;
            }
        } else if (curUnit.equals("LB")) {
            if (exchangeUnit.equals("KG")) {
                exchangeWeight = WeightUnitUtil.LB2KG(weight);  //(float) ((weight * 0.45));
            } else if (exchangeUnit.equals("LB")) {
                exchangeWeight = weight;
            }
        }
        return exchangeWeight;
    }

    public static float getExchangeWeightforVer2(float weight, String curUnit,
                                                 String exchangeUnit) {
        float exchangeWeight = weight;
        if (curUnit.equals("KG")) {
            if (exchangeUnit.equals("LB")) {
                exchangeWeight = weight * 2.2046226f; //(float) (weight / 0.453);
            } else if (exchangeUnit.equals("KG")) {
                exchangeWeight = weight;
            }
        } else if (curUnit.equals("LB")) {
            if (exchangeUnit.equals("KG")) {
                exchangeWeight = WeightUnitUtil.LB2KG(weight);  //(float) ((weight * 0.45));
            } else if (exchangeUnit.equals("LB")) {
                exchangeWeight = weight;
            }
        }
        return exchangeWeight;
    }

    /**
     * cm转换成英尺、英寸
     *
     * @param cm
     * @return
     */
    public static int[] ExchangeUnitCmToFeetAndInch(float cm) {
        int[] unit = new int[2];
        float feet = getExchangeLength(cm, "cm", "feet");
        float inch = (feet - (int) feet) * 12;
        unit[0] = (int) feet;
        unit[1] = Math.round(inch);
        if (unit[1] == 12) {
            unit[0] += 1;
            unit[1] = 0;
        }
        return unit;
    }

    /**
     * 长度单位转换函数
     *
     * @param length       当前长度
     * @param curUnit      当前单位
     * @param exchangeUnit 需要转换为的单位
     *                     <p/>
     *                     单位分别为 ："cm" "feet" "inch"三种
     */
    public static float getExchangeLength(float length, String curUnit,
                                          String exchangeUnit) {
        float exchangeLength = length;
        if (curUnit.equals("cm")) {
            if (exchangeUnit.equals("feet")) {
                exchangeLength = (float) (length * 0.394 / 12);
            } else if (exchangeUnit.equals("inch")) {
                exchangeLength = (float) (length * 0.394);
            }
        } else if (curUnit.equals("inch")) {
            if (exchangeUnit.equals("cm")) {
                exchangeLength = (float) (length / 0.394);
            } else if (exchangeUnit.equals("feet")) {
                exchangeLength = length / 12;
            }
        } else if (curUnit.equals("feet")) {
            if (exchangeUnit.equals("cm")) {
                exchangeLength = (float) (length * 12 / 0.394);
            } else if (exchangeUnit.equals("inch")) {
                exchangeLength = length * 12;
            }
        }
        return exchangeLength;
    }
}
