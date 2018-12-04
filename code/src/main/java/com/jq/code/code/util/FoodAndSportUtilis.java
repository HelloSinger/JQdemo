package com.jq.code.code.util;

import com.jq.code.R;
import com.jq.code.code.business.JsonMapper;
import com.jq.code.model.Constant;
import com.jq.code.model.ExerciseDietEntity;
import com.jq.code.model.XHelpEntity;
import com.jq.code.model.sport.BiteEnty;
import com.jq.code.model.sport.BiteUnit;
import com.jq.code.model.trend.SportEntityRightXText;
import com.fasterxml.jackson.core.type.TypeReference;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/20.
 */

public class FoodAndSportUtilis {
    public static int getKiloByUser(float min, float weight, float met) {
        return (int) (met * (weight * min) / 60.0f);
    }

    public static String numberRoundOne(float number) {
        if (number == 0) {
            return "0";
        } else {
//            DecimalFormat df = new DecimalFormat("#.0");
            return String.format("%.1f", number);
        }
    }
    public static String numberRoundTwo(float number) {
        if (number == 0) {
            return "0";
        } else {
            DecimalFormat df = new DecimalFormat("#.00");
            return df.format(number);
        }
    }
    public static List<BiteUnit> getUnitEnity(BiteEnty enty) {
        List<BiteUnit> result = new ArrayList<BiteUnit>();
        if (enty.is_liquid()) {
            if (enty.getUnits() != null) {
                List<BiteUnit> restUnit = JsonMapper.fromJson(enty.getUnits(), new TypeReference<ArrayList<BiteUnit>>() {
                });
                result.addAll(restUnit);
            }
            BiteUnit unit = new BiteUnit();
            unit.setAmount(100);
            unit.setUnit("毫升");
            unit.setUnit_id(-2);
            unit.setEat_weight(100);
            unit.setWeight(100);
            unit.setCalory(enty.getCalory());
            result.add(unit);
        } else {
            if (enty.getUnits() != null) {
                List<BiteUnit> restUnit = JsonMapper.fromJson(enty.getUnits(), new TypeReference<ArrayList<BiteUnit>>() {
                });
                for (int i = 0; i < restUnit.size(); i++) {
                    BiteUnit biteUnit = restUnit.get(i) ;
                    if(biteUnit.getUnit().equals("2个")){
                        biteUnit.setUnit("两支");
                        break;
                    }
                }
                result.addAll(restUnit);
            }
            BiteUnit unit = new BiteUnit();
            unit.setAmount(100);
            unit.setUnit("克");
            unit.setUnit_id(-1);
            unit.setEat_weight(100);
            unit.setWeight(100);
            unit.setCalory(enty.getCalory());
            result.add(unit);
        }
        return result;
    }

    public static BiteUnit getSubUnit(String unitStr, float quantity, float calory) {
        BiteUnit unit = new BiteUnit();
        if (unitStr.equals("g")) {
            unit.setAmount(100);
            unit.setUnit("克");
            unit.setUnit_id(-1);
            unit.setEat_weight(100);
            unit.setWeight(100);
            unit.setCalory(calory * 100.0f / quantity);
        } else if (unitStr.equals("ml")) {
            unit.setAmount(100);
            unit.setUnit("毫升");
            unit.setUnit_id(-2);
            unit.setEat_weight(100);
            unit.setWeight(100);
            unit.setCalory(calory * 100.0f / quantity);
        } else {
            unit = JsonMapper.fromJson(unitStr, BiteUnit.class);
        }
        return unit;
    }

    public static BiteUnit parseSubUnit(String unitStr) {
        BiteUnit unit = new BiteUnit();
        if (unitStr.equals("g")) {
            unit.setUnit("克");
        } else if (unitStr.equals("ml")) {
            unit.setUnit("毫升");
        } else {
            unit = JsonMapper.fromJson(unitStr, BiteUnit.class);
        }
        return unit;
    }

    public static List<String> parserDate(Object hashObj) {
        List<String> result = new ArrayList<String>();
        HashMap<String, HashMap<String, Object>> objHm = JsonMapper.fromJson(hashObj, new TypeReference<HashMap<String, HashMap<String, Object>>>() {
        });
        if (objHm != null) {
            Iterator iterable = objHm.entrySet().iterator();
            while (iterable.hasNext()) {
                Map.Entry entry = (Map.Entry) iterable.next();
                String type = (String) entry.getKey();
                HashMap<String, Object> outHashValue = objHm.get(type);
                Iterator iterable2 = outHashValue.entrySet().iterator();
                while (iterable.hasNext()) {
                    Map.Entry entry2 = (Map.Entry) iterable2.next();
                    result.add((String) entry2.getKey());
                }
            }
        }
        return result;
    }

    public static List<String> parserDate(String typeStr, Object hashObj) {
        List<String> result = new ArrayList<String>();
        HashMap dataHash = JsonMapper.fromJson(hashObj, HashMap.class);
        if (dataHash == null) return result;
        Object typeObj = dataHash.get(typeStr);
        if (typeObj != null) {
            HashMap<String, Object> hp = JsonMapper.fromJson(typeObj, new TypeReference<HashMap<String, Object>>() {
            });
            if (hp != null) {
                Iterator iterable = hp.entrySet().iterator();
                while (iterable.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterable.next();
                    String date = (String) entry.getKey();
                    result.add(date);
                }
            }
        }

        return result;
    }

    public static List<XHelpEntity> getTrendViewText(boolean isWeek, long startTs) {
        List<XHelpEntity> result = new ArrayList<>();
        if (isWeek) {
            for (int i = 0; i < 7; i++) {
                XHelpEntity xHelpEntity = new XHelpEntity(i, TimeUtil.parseTimes(startTs + i * Constant.ONE_DAY_MS, TimeUtil.TIME_FORMAT_EN_3));
                result.add(xHelpEntity);
            }
        } else {
            XHelpEntity xHelpEntity1 = new XHelpEntity(0, TimeUtil.parseTimes(startTs + 0 * Constant.ONE_DAY_MS, TimeUtil.TIME_FORMAT_EN_3));
            XHelpEntity xHelpEntity2 = new XHelpEntity(13, TimeUtil.parseTimes(startTs + 14 * Constant.ONE_DAY_MS, TimeUtil.TIME_FORMAT_EN_3));
            XHelpEntity xHelpEntity3 = new XHelpEntity(26, TimeUtil.parseTimes(startTs + 27 * Constant.ONE_DAY_MS, TimeUtil.TIME_FORMAT_EN_3));
            result.add(xHelpEntity1);
            result.add(xHelpEntity2);
            result.add(xHelpEntity3);
        }
        return result;
    }

    public static List<SportEntityRightXText> getSportRightXText(long startTs, List<ExerciseDietEntity> tempEntities) {
        List<SportEntityRightXText> result = new ArrayList<>();
        for (int i = 0; i < tempEntities.size(); i++) {
            ExerciseDietEntity dietEntity  = tempEntities.get(i) ;
            int value1 = dietEntity.getTotalIntake();
            int value2 = dietEntity.getMetabolism() + dietEntity.getExCalory();
            int value3 = dietEntity.getMetabolism();
            int position = TimeUtil.getTwoDay(startTs, dietEntity.getMeasure_time()); //两天距离多久
            SportEntityRightXText xText = new SportEntityRightXText(value1, value2, value3, position);
            result.add(xText);
        }
        return result;
    }
    public static List<SportEntityRightXText> getSynthTrendSportRightXText(List<ExerciseDietEntity> tempEntities) {
        List<SportEntityRightXText> result = new ArrayList<>();
        for (int i = 0; i < tempEntities.size(); i++) {
            ExerciseDietEntity dietEntity  = tempEntities.get(i) ;
            int value1 = dietEntity.getTotalIntake();
            int value2 = dietEntity.getMetabolism() + dietEntity.getExCalory();
            int value3 = dietEntity.getMetabolism();
            int position = tempEntities.size()-1 - i ;
            SportEntityRightXText xText = new SportEntityRightXText(value1, value2, value3, position);
            result.add(xText);
        }
        return result;
    }
    public static int getMaxYTextValue(List<SportEntityRightXText> rightXTexts) {
        int maxValue = 0;
        for (SportEntityRightXText rightXText : rightXTexts) {
            int tempMaxValue = (int) (rightXText.getValue1() > rightXText.getValue2() ? rightXText.getValue1() : rightXText.getValue2());
            maxValue = maxValue > tempMaxValue ? maxValue : tempMaxValue;
        }
        return maxValue;
    }
    public static int getDetalisPrompt(ExerciseDietEntity entity, String sex){
        int result = R.string.unknow_tip;
        int intake = entity.getTotalIntake() ;
        int consume = entity.getExCalory() ;
        int metabolism = entity.getMetabolism() ;
        int sugIntake = getSugIntake(sex) ;
        int sugConsume = 150 ;
        if(intake > (consume + metabolism) && consume >= sugConsume){
            result = R.string.sport_food_consum_tip1 ;
        }else if(intake > (metabolism + consume) && consume < sugConsume){
            result = R.string.sport_food_consum_tip2;
        }else if(sugIntake < intake && intake < metabolism + consume && consume > sugConsume){
            result = R.string.sport_food_consum_tip3 ;
        }else  if(sugIntake < intake && intake < metabolism + consume && consume<sugConsume){
            result = R.string.sport_food_consum_tip4;
        }else if(intake < sugIntake){
            result = R.string.sport_food_consum_tip5;
        }
        return result ;
    }
    public static int getSugIntake(String sex){
        int intake = 0 ;
        if(sex.equals("男")){
            intake = 340 + 540 + 490 ;
        }else {
            intake = 290 + 470 + 440 ;
        }
        return intake ;
     }
}
