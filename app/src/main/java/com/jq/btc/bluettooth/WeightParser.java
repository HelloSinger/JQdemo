package com.jq.btc.bluettooth;

import android.content.Context;

import com.jq.btc.app.R;
import com.jq.btlib.util.WeightUnitUtil;
import com.jq.code.code.business.Account;
import com.jq.code.code.business.Config;
import com.jq.code.code.db.WeightDataDB;
import com.jq.code.code.util.StandardUtil;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.WeightEntity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/30.
 */
public class WeightParser {

    public static Map<String, Object> calculate(Context context) {
        RoleInfo roleInfo = Account.getInstance(context).getMainRoleInfo();
        WeightEntity entity = WeightDataDB.getInstance(context).findLastRoleDataByRoleId(roleInfo);
        return calculate(context, roleInfo, entity);
    }

    public static Map<String, Object> calculate(Context context, RoleInfo roleInfo) {
        WeightEntity entity = WeightDataDB.getInstance(context).findLastRoleDataByRoleId(roleInfo);
        return calculate(context, roleInfo, entity);
    }

    public static Map<String, Object> calculate(Context context, RoleInfo roleInfo, WeightEntity entity) {
        Map<String, Object> map = new HashMap<String, Object>();
        BigDecimal bigDecimal;
        int intWeightUnit = Config.getInstance(context).getIntWeightUnit();
//        String unit = StandardUtil.getWeightExchangeUnit(context);
        String goalVaule = onWeight(context, roleInfo.getWeight_goal(), "", (byte) 1);
        String initValue = onWeight(context, roleInfo.getWeight_init(), "", (byte) 1);
        if (roleInfo.getWeight_goal() == 0) {
            map.put("goal", "- -");
        } else {
            map.put("goal", goalVaule);
        }
        if (roleInfo.getWeight_init() == 0) {
            map.put("init", "- -");
        } else {
            map.put("init", initValue);
        }
        if (entity == null) {
            map.put("cur", "- -");
            map.put("compare", "- -");
            map.put("tip", context.getString(R.string.weightTip9));
        } else {
            String curValue = onWeight(context, entity.getWeight(), entity.getScaleweight(), entity.getScaleproperty());
            map.put("cur", curValue);
            if (roleInfo.getWeight_goal() == 0 && roleInfo.getWeight_init() == 0) {
                map.put("compare", "- -");
                map.put("tip", context.getString(R.string.weightTip6));
            } else if (roleInfo.getWeight_goal() == 0 && roleInfo.getWeight_init() != 0) {
                map.put("compare", "- -");
                map.put("tip", context.getString(R.string.weightTip8));
            } else if (roleInfo.getWeight_goal() != 0 && roleInfo.getWeight_init() == 0) {
                map.put("compare", "- -");
                map.put("tip", context.getString(R.string.weightTip7));
            } else {
                String compareValue = "0.0";
                String initCompareValue = "0.0";

                if (intWeightUnit == Config.ST) {
                    String[] goalSplit = goalVaule.split(":");
                    String[] initSplit = initValue.split(":");
                    String[] curSplit = curValue.split(":");

                    float goalLb = WeightUnitUtil.ST2LB(goalVaule);
                    float initLb = WeightUnitUtil.ST2LB(initValue);
                    float curLb = WeightUnitUtil.ST2LB(curValue);

                    float diffGoal = curLb - goalLb;
                    compareValue = WeightUnitUtil.LB2ST(Math.abs(diffGoal));
                    float diffInit = curLb - initLb;
                    initCompareValue = WeightUnitUtil.LB2ST(Math.abs(diffInit));

                    float diffInitGoal = initLb - goalLb;
                    boolean bTargetIsOk = false;
                    if (diffInitGoal > 0) {
                        //需要减肥
                        if (diffGoal <= 0) bTargetIsOk = true;
                    } else if (diffInitGoal < 0) {
                        //需要增肥
                        if (diffGoal >= 0) bTargetIsOk = true;
                    } else {
                        if (diffGoal == 0.0f) bTargetIsOk = true;
                    }
                    coverSTTip(context, diffInit, compareValue, initCompareValue, map, bTargetIsOk, diffGoal);
                } else {
                    float sum = Float.parseFloat(curValue) - Float.parseFloat(goalVaule);
                    float sum1 = Float.parseFloat(curValue) - Float.parseFloat(initValue);
                    bigDecimal = new BigDecimal(sum);
                    float diffGoal = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();

                    bigDecimal = new BigDecimal(sum1);
                    float diffInit = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();

                    boolean bTargetIsOk = false;
                    float diffInitGoal = Float.parseFloat(initValue) - Float.parseFloat(goalVaule);
                    if (diffInitGoal > 0) {
                        //需要减肥
                        if (diffGoal <= 0) bTargetIsOk = true;
                    } else if (diffInitGoal < 0) {
                        //需要增肥
                        if (diffGoal >= 0) bTargetIsOk = true;
                    } else {
                        if (diffGoal == 0.0f) bTargetIsOk = true;
                    }
                    compareValue = Math.abs(diffGoal) + "";
                    coverTip(context, diffGoal, diffInit, map, bTargetIsOk);
                }
                map.put("compare", compareValue);
            }
        }
        return map;
    }

    /**
     * 获取目标比数据
     *
     * @return
     */
    public static Map<String, String> parseGoalCompare(Context context, WeightEntity weightEntity) {
        Map<String, String> map = new HashMap<>();
        map.put("tip", "");
        map.put("value", "- -");
        if (weightEntity != null) {
            RoleInfo info = Account.getInstance(context).findRole(weightEntity.getAccount_id(), weightEntity.getRole_id());
            if (!Account.getInstance(context).isAccountLogined() || info == null) return map;
            if (info.getWeight_goal() == 0) return map;
            if (weightEntity.getWeight() == 0) return map;
            int intWeightUnit = Config.getInstance(context).getIntWeightUnit();
            String goalVaule = onWeight(context, info.getWeight_goal(), "", (byte) 1);
            String curValue = onWeight(context, weightEntity.getWeight(), weightEntity.getScaleweight(), weightEntity.getScaleproperty());

            if (intWeightUnit == Config.ST) {
                float goalLb = WeightUnitUtil.ST2LB(goalVaule);
                float curLb = WeightUnitUtil.ST2LB(curValue);
                float diffGoal = curLb - goalLb;
                if (diffGoal < 0) {
                    map.put("tip", context.getString(R.string.main_goalCompareTip2));
                } else if (diffGoal == 0) {
                    map.put("tip", context.getString(R.string.main_goalCompareTip1));
                } else {
                    map.put("tip", context.getString(R.string.main_goalCompareTip3));
                }
                map.put("value", WeightUnitUtil.LB2ST(Math.abs(diffGoal)));
                return map;
            } else {
                float sum = Float.parseFloat(curValue) - Float.parseFloat(goalVaule);
                if (sum < 0) {
                    map.put("tip", context.getString(R.string.main_goalCompareTip2));
                } else if (sum == 0) {
                    map.put("tip", context.getString(R.string.main_goalCompareTip1));
                } else {
                    map.put("tip", context.getString(R.string.main_goalCompareTip3));
                }
                BigDecimal bigDecimal = new BigDecimal(sum);
                float diffGoal = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
                map.put("value", Math.abs(diffGoal) + "");
                return map;
            }
        }
        return map;
    }


    private static void coverTip(Context context, float goalValue, float initValue, Map<String, Object> map, boolean isTargetOK) {
        String tip = "";
        String unit = StandardUtil.getWeightExchangeUnit(context);
        if (initValue == 0) {
            if (goalValue == 0) {
                tip = context.getString(R.string.weightTip13);
            } else {
                tip = String.format(context.getString(R.string.weightTip1), Math.abs(goalValue) + unit);
            }

        } else if (initValue < 0) {
            if (isTargetOK) {
                tip = String.format(context.getString(R.string.weightTip3), Math.abs(initValue) + unit);
            } else {
                tip = String.format(context.getString(R.string.weightTip2), Math.abs(initValue) + unit, Math.abs(goalValue) + unit);
            }
        } else {
            if (isTargetOK) {
                tip = String.format(context.getString(R.string.weightTip5), Math.abs(initValue) + unit);
            } else {
                tip = String.format(context.getString(R.string.weightTip4), Math.abs(initValue) + unit, Math.abs(goalValue) + unit);
            }
        }
        map.put("tip", tip);
    }

    private static void coverSTTip(Context context, float diffInit, String compareValue, String initCompareValue, Map<String, Object> map, boolean isTargeOK, float diffGoal) {
        String tip = "";
        String unit = StandardUtil.getWeightExchangeUnit(context);
        if (diffInit == 0) {
            if (diffGoal == 0) {
                tip = context.getString(R.string.weightTip13);
            } else {
                tip = String.format(context.getString(R.string.weightTip1), compareValue + unit);
            }

        } else if (diffInit < 0) {
            if (isTargeOK) {
                tip = String.format(context.getString(R.string.weightTip3), initCompareValue + unit);
            } else {
                tip = String.format(context.getString(R.string.weightTip2), initCompareValue + unit, compareValue + unit);
            }
        } else {
            if (isTargeOK) {
                tip = String.format(context.getString(R.string.weightTip5), initCompareValue + unit);
            } else {
                tip = String.format(context.getString(R.string.weightTip4), initCompareValue + unit, compareValue + unit);
            }
        }
        map.put("tip", tip);
    }

    public static String onWeight(Context context, float value, String scaleWeight, byte scaleProp) {
        if (Float.compare(value, 0.0f) == 0) return "0.0";
        //value = ((int) (value * 10)) / 10f;
        String exchangeValue = StandardUtil.getWeightExchangeValueforVer2(context,
                value,
                scaleWeight,
                scaleProp);
        return exchangeValue;
    }
}
