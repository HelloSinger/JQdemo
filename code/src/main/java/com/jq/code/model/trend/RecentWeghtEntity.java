package com.jq.code.model.trend;

import com.jq.code.model.Displayable;
import com.jq.code.model.WeightEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 趋势图：最近有数据的n天
 * 每个对象保存一天的数据
 */

public class RecentWeghtEntity implements Displayable {
    private String date;

    private List<WeightEntity> weightList = new ArrayList<>();
    private float weight;
    // TODO
    private float bmi;
    private float axunge;
    private float bone;
    private float muscle;
    private float water;
    private float metabolism;
    private float viscera;
    private float yAxis;
    private int xPosition;
    //蓝牙秤显示的体重数值
    private String scaleweight;
    private byte scaleproperty;

    public byte getScaleproperty() {
        return scaleproperty;
    }

    public void setScaleproperty(byte scaleproperty) {
        this.scaleproperty = scaleproperty;
    }

    public String getScaleweight() {
        return scaleweight;
    }

    public void setScaleweight(String scaleweight) {
        this.scaleweight = scaleweight;
    }

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public float getyAxis() {
        return yAxis;
    }

    public void setyAxis(float yAxis) {
        this.yAxis = yAxis;
    }

    @Override
    public String getMeasure_time() {
        return date;
    }

    /**
     * 新加数据必须在同一天
     * @param w
     */
    public void addWeight(WeightEntity w){
        addDate(w.getMeasure_time().substring(0, 10));
        weightList.add(w);
        refreshData();
    }

    private void addDate(String ndate) {
        if(date==null){
            this.date = ndate;
        }
        if (!date.equals(ndate)) {
            throw new RuntimeException("Date mismatch!" + ndate + " : " + date);
        }
    }


    private void refreshData() {
        int iWeight=0, iBone=0 ,iBmi=0,iAxunge=0,iMuscle=0,iWater=0,iMetabolism=0,iViscera=0 ;
        float sumWeight=0, sumBone=0,sumBmi=0,sumAxunge=0,sumMuscle=0,sumWater=0,sumMetabolism=0,sumViscera=0 ;
        
        for (WeightEntity weightEntity : weightList) {
            if(weightEntity.getWeight() > 0){
                iWeight++;
                sumWeight+=weightEntity.getWeight();
            }
            if(weightEntity.getBone()>0){
                iBone++;
                sumBone+= weightEntity.getBone();
            }
            if(weightEntity.getBmi()>0){
                iBmi++;
                sumBmi+= weightEntity.getBmi();
            }
            if(weightEntity.getAxunge()>0){
                iAxunge++;
                sumAxunge+= weightEntity.getAxunge();
            }
            if(weightEntity.getMuscle()>0){
                iMuscle++;
                sumMuscle+= weightEntity.getMuscle();
            }
            if(weightEntity.getWater()>0){
                iWater++;
                sumWater+= weightEntity.getWater();
            }
            if(weightEntity.getMetabolism()>0){
                iMetabolism++;
                sumMetabolism+= weightEntity.getMetabolism();
            }
            if(weightEntity.getViscera()>0){
                iViscera++;
                sumViscera+= weightEntity.getViscera();
            }
        }
        weight = iWeight>0 ? sumWeight/iWeight : 0;
        bone = iBone>0 ? sumBone/iBone : 0;
        bmi = iBmi>0 ? sumBmi/iBmi : 0;
        axunge = iAxunge>0 ? sumAxunge/iAxunge : 0;
        muscle = iMuscle>0 ? sumMuscle/iMuscle : 0;
        water = iWater>0 ? sumWater/iWater : 0;
        metabolism = iMetabolism>0 ? sumMetabolism/iMetabolism : 0;
        viscera = iViscera>0 ? sumViscera/iViscera : 0;

        // TODO
        
    }
    public static float getTypeValue(String showType, RecentWeghtEntity tempTrend) {
        float result = 0f;
        if (showType.equals(WeightEntity.WeightType.WEIGHT)) {
            result = tempTrend.getWeight();
        } else if (showType.equals(WeightEntity.WeightType.BMI)) {
            result = tempTrend.getBmi();
        } else if (showType.equals(WeightEntity.WeightType.BONE)) {
            result = tempTrend.getBone();
        } else if (showType.equals(WeightEntity.WeightType.FAT)) {
            result = tempTrend.getAxunge();
        } else if (showType.equals(WeightEntity.WeightType.METABOLISM)) {
            result = tempTrend.getMetabolism();
        } else if (showType.equals(WeightEntity.WeightType.MUSCLE)) {
            result = tempTrend.getMuscle();
        } else if (showType.equals(WeightEntity.WeightType.VISCERA)) {
            result = tempTrend.getViscera();
        } else if (showType.equals(WeightEntity.WeightType.WATER)) {
            result = tempTrend.getWater();
        }
        return result;
    }
    public String getDate() {
        return date;
    }

    public float getAxunge() {
        return axunge;
    }

    public float getBmi() {
        return bmi;
    }


    public float getBone() {
        return bone;
    }

    public float getMetabolism() {
        return metabolism;
    }

    public float getMuscle() {
        return muscle;
    }

    public float getViscera() {
        return viscera;
    }

    public float getWater() {
        return water;
    }

    public float getWeight() {
        return weight;
    }

    public List<WeightEntity> getWeightList() {
        return weightList;
    }
}
