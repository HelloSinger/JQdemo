package com.jq.btc.utils;

import android.content.Context;

import com.jq.btc.app.R;
import com.jq.btc.helper.WeighDataParser;
import com.jq.code.code.business.Account;
import com.jq.code.code.util.StandardUtil;
import com.jq.code.code.util.TimeUtil;
import com.jq.code.model.Constant;
import com.jq.code.model.PieChartEntity;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.WeightEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 */

public class ShareUtils {
    private Context context ;
    private WeightEntity upWeight ;
    private WeightEntity lastWeight ;
    public ShareUtils(Context context,WeightEntity lastWeight,WeightEntity upWeight){
        this.context = context ;
        this.lastWeight = lastWeight ;
        this.upWeight = upWeight ;

    }
    public String getTiXingString(){
        int bmiLevel = WeighDataParser.getBmiLevel(lastWeight) - 1;
//        int type = WeighDataParser.StandardSet.BMI.getStandards()[bmiLevel];
        String type = WeighDataParser.StandardSet.BMI.getStandards()[bmiLevel];
        String typeStr = type ;
        return context.getString(R.string.share_tixing_tip,typeStr) ;
    }
    public String getChangeText(){
        if(upWeight == null){
            return context.getString(R.string.share_change_not_up_weight) ;
        }else {
            String dateStr = TimeUtil.dateFormatChange(upWeight.getWeight_time(),TimeUtil.TIME_FORMAT1,TimeUtil.TIME_FORMAT_EN_5) ;
            float currWeight = lastWeight.getWeight() ;
            float lastWeight = upWeight.getWeight();
            if (currWeight > lastWeight) {
                 return context.getString(R.string.share_change_up_tip,dateStr) ;
            } else {
                return context.getString(R.string.share_change_down_tip,dateStr) ;
            }
        }
    }
    public String getValueText(){
        if(upWeight == null){
            return Constant.NULL_DATA_DEFAULT;
        }else {
            float currWeight = lastWeight.getWeight() ;
            float lastWeight = upWeight.getWeight();
            float data = currWeight- lastWeight;
            return StandardUtil.getWeightExchangeValue(context, Math.abs(data), "", (byte) 1) ;
        }
    }
    public String getCurrWeight(){
        return StandardUtil.getWeightExchangeValueforVer2(context, lastWeight.getWeight(), lastWeight.getScaleweight(), lastWeight.getScaleproperty()) ;
    }
    public String getGoldText(){
        RoleInfo roleInfo = Account.getInstance(context).getRoleInfo();
        float weightGoal = roleInfo.getWeight_goal();
        if(weightGoal > 0){
            float data = lastWeight.getWeight() - weightGoal;
            if(data > 0){
                String weightStr = StandardUtil.getWeightExchangeValue(context, Math.abs(data), "", (byte) 5)  + StandardUtil.getWeightExchangeUnit(context);
                return context.getString(R.string.share_goal_jian_tip,weightStr) ;
            }else {
                String weightStr = StandardUtil.getWeightExchangeValue(context, Math.abs(data), "", (byte) 5)  + StandardUtil.getWeightExchangeUnit(context);
                return context.getString(R.string.share_goal_zeng_tip,weightStr) ;
            }
        }else {
            return context.getString(R.string.share_goal_jian_tip,Constant.NULL_DATA_DEFAULT);
        }
    }
    public String getBodyText(){
        return context.getString(R.string.share_body_tip,StandardUtil.getWeightExchangeUnit(context)) ;
    }
    public List<PieChartEntity> getPieEntity(){
        List<PieChartEntity> result = new ArrayList<>() ;
        if(lastWeight.getAxunge() <= 0){
            result.add(getSingleEntity());
        }else {
            if(lastWeight.getR1() > 0){
                    if(lastWeight.getAge() < 18){
                        result.add(getMuscleEntity());
                        result.add(getAxungeEntity());
                        result.add(getBoneEntity());
                    }else {
                        result.add(getProteinEntity());
                        result.add(getWaterEntity());
                        result.add(getAxungeEntity());
                        result.add(getBoneEntity());
                    }
            }else {
                result.add(getMuscleEntity());
                result.add(getAxungeEntity());
                result.add(getBoneEntity());
                result.add(getOntherEntity());
            }
        }
        return result ;
    }


    public PieChartEntity getSingleEntity(){
        PieChartEntity single = new PieChartEntity() ;
        single.setColor(R.color.share_pie_color_other) ;
        single.setContent("");
        single.setValue(lastWeight.getWeight());
        return single ;
    }
    public PieChartEntity getMuscleEntity(){
        float muscleWeight = lastWeight.getWeight() *  lastWeight.getMuscle() / 100.f ;
        String muscleWeightStr = StandardUtil.getWeightExchangeValueforVer2(context, muscleWeight, "", (byte) 1) ;
        PieChartEntity muscle = new PieChartEntity() ;
        muscle.setValue(muscleWeight) ;
        muscle.setContent(context.getString(R.string.share_pie_muscle,muscleWeightStr));
        muscle.setColor(R.color.share_pie_color_muscle);
        return muscle ;
    }
    public PieChartEntity getAxungeEntity(){
        float axungeWeight = lastWeight.getWeight() *  lastWeight.getAxunge() / 100.f ;
        String axugeWeightStr = StandardUtil.getWeightExchangeValueforVer2(context, axungeWeight, "", (byte) 1)  ;
        PieChartEntity axunge = new PieChartEntity() ;
        axunge.setValue(axungeWeight) ;
        axunge.setContent(context.getString(R.string.share_pie_axunge,axugeWeightStr));
        axunge.setColor(R.color.share_pie_color_axunge);
        return axunge ;
    }
    public PieChartEntity getBoneEntity(){
        float boneWeight = lastWeight.getBone() ;
        String boneWeightStr = StandardUtil.getWeightExchangeValueforVer2(context, boneWeight, "", (byte) 1)  ;
        PieChartEntity bone = new PieChartEntity() ;
        bone.setValue(boneWeight) ;
        bone.setContent(context.getString(R.string.share_pie_bone,boneWeightStr));
        bone.setColor(R.color.share_pie_color_bone);
        return bone ;
    }
    public PieChartEntity getProteinEntity(){
        float proteinWeight  = lastWeight.getWeight() *  WeighDataParser.getProtein(lastWeight) / 100.f  ;
        String proteinWeightStr = StandardUtil.getWeightExchangeValueforVer2(context, proteinWeight, "", (byte) 1)  ;
        PieChartEntity protein = new PieChartEntity() ;
        protein.setValue(proteinWeight) ;
        protein.setContent(context.getString(R.string.share_pie_protein,proteinWeightStr));
        protein.setColor(R.color.share_pie_color_protein);
        return protein ;
    }
    public PieChartEntity getWaterEntity(){
        float waterWeight  = lastWeight.getWeight() *  lastWeight.getWater() / 100.f  ;
        String waterWeightStr = StandardUtil.getWeightExchangeValueforVer2(context, waterWeight, "", (byte) 1)  ;
        PieChartEntity water = new PieChartEntity() ;
        water.setValue(waterWeight) ;
        water.setContent(context.getString(R.string.share_pie_water,waterWeightStr));
        water.setColor(R.color.share_pie_color_water);
        return water ;
    }
    public PieChartEntity getOntherEntity(){
        float muscleWeight = lastWeight.getWeight() *  lastWeight.getMuscle() / 100.f ;
        float axungeWeight = lastWeight.getWeight() *  lastWeight.getAxunge() / 100.f ;
        float boneWeight = lastWeight.getBone() ;
        float otherWeight = lastWeight.getWeight() - muscleWeight - axungeWeight - boneWeight ;
        String otherWeightStr = StandardUtil.getWeightExchangeValueforVer2(context, otherWeight, "", (byte) 1)  ;
        PieChartEntity other = new PieChartEntity() ;
        other.setValue(otherWeight) ;
        other.setContent(context.getString(R.string.share_pie_other,otherWeightStr));
        other.setColor(R.color.share_pie_color_other);
        return other ;
    }

}
