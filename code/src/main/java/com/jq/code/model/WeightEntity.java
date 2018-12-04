package com.jq.code.model;

import android.content.Context;
import android.os.Parcel;

import com.jq.code.code.util.StandardUtil;

/**
 * 秤数据信息实体类
 */
public class WeightEntity extends PutBase {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private float weight;
    private float bmi;
    private float axunge;
    private float bone;
    private float muscle;
    private float water;
    private float metabolism;
    private float body_age;
    private float viscera;
    private String sync_time;

    private long productid;
    //蓝牙秤显示的体重数值
    private String scaleweight;
    /**
     * 电阻值
     **/
    private float r1;
    private int score ;
    private float bw ;
    private int height;
    private int sex;
    private int age ;
    protected String rn8;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    /** 用于显示的体重值 */
    private String displayWeight;
    /** 当前 显示的体重值 对应的体重单位 */
    private String mDisplayingWeightUnit;

    public String getDisplayWeight(Context context, String currentWeightUnit) {
        // 获得当前体重单位
        if(null == displayWeight || !currentWeightUnit.equals(mDisplayingWeightUnit)) {
            displayWeight = StandardUtil.getWeightExchangeValue(context, getWeight(),
                    getScaleweight(), getScaleproperty());
            mDisplayingWeightUnit = currentWeightUnit;
        }
        return displayWeight;
    }

    /**
     * 蓝牙秤显示的体重属性
     * 如果当前单位为英石:磅, ScaleProperty固定设置为10进制的25
     * 如果单位为jin，ScaleProperty固定设置为10进制的9
     * 如果单位为lb,ScaleProperty固定设置为10进制的17
     * 如果单位为kg,ScaleProperty固定设置为10进制的1
     */
    private byte scaleproperty;
    private float yAxis;

    public float getyAxis() {
        return yAxis;
    }

    public void setyAxis(float yAxis) {
        this.yAxis = yAxis;
    }

    public PutBase copy() {
        WeightEntity info = new WeightEntity();
        info.setId(getId());
        info.setWeight(weight);
        info.setWeight_time(weight_time);
        info.setBmi(bmi);
        info.setAxunge(axunge);
        info.setBone(bone);
        info.setMuscle(muscle);
        info.setWater(water);
        info.setMetabolism(metabolism);
        info.setBody_age(body_age);
        info.setViscera(viscera);
        info.setAccount_id(account_id);
        info.setRole_id(role_id);
        info.setSync_time(sync_time);
        info.setDelete(delete);
        info.setScaleweight(scaleweight);
        info.setScaleproperty(scaleproperty);
        info.setProductid(productid);
        info.setMtype(mtype);
        info.setR1(r1);
        info.setBw(bw);
        info.setScore(score);
        info.setHeight(height);
        info.setSex(sex);
        info.setAge(age);
        info.setRn8(rn8);
        return info;
    }

    public static class WeightType {
        public static final String WEIGHT = "weight";
        public static final String BMI = "bmi";
        public static final String FAT = "fat";
        public static final String MUSCLE = "muscle";
        public static final String VISCERA = "viscera";
        public static final String BONE = "bone";
        public static final String WATER = "water";
        public static final String METABOLISM = "metabolism";


    }

    public static float getTypeValue(String showType, WeightEntity tempTrend) {
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
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public float getBw() {
        return bw;
    }

    public void setBw(float bw) {
        this.bw = bw;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public float getR1() {
        return r1;
    }

    public void setR1(float r1) {
        this.r1 = r1;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getBmi() {
        return bmi;
    }

    public void setBmi(float bmi) {
        this.bmi = bmi;
    }

    public float getAxunge() {
        return axunge;
    }

    public void setAxunge(float axunge) {
        this.axunge = axunge;
    }

    public float getBone() {
        return bone;
    }

    public void setBone(float bone) {
        this.bone = bone;
    }

    public float getMuscle() {
        return muscle;
    }

    public void setMuscle(float muscle) {
        this.muscle = muscle;
    }

    public float getWater() {
        return water;
    }

    public void setWater(float water) {
        this.water = water;
    }

    public float getMetabolism() {
        return metabolism;
    }

    public void setMetabolism(float metabolism) {
        this.metabolism = metabolism;
    }

    public float getBody_age() {
        return body_age;
    }

    public void setBody_age(float body_age) {
        this.body_age = body_age;
    }

    public float getViscera() {
        return viscera;
    }

    public void setViscera(float viscera) {
        this.viscera = viscera;
    }

    public String getSync_time() {
        return sync_time;
    }

    public void setSync_time(String sync_time) {
        this.sync_time = sync_time;
    }

    public long getProductid() {
        return productid;
    }

    public void setProductid(long productid) {
        this.productid = productid;
    }

    public String getScaleweight() {
        return scaleweight;
    }

    public void setScaleweight(String scaleweight) {
        this.scaleweight = scaleweight;
    }

    public byte getScaleproperty() {
        return scaleproperty;
    }

    public void setScaleproperty(byte scaleproperty) {
        this.scaleproperty = scaleproperty;
    }

    public String getRn8(){
        return rn8;
    }

    public void setRn8(String rn8){
        this.rn8=rn8;
    }

    @Override
    public String toString() {
        return "WeightEntity{" +
                "weight=" + weight +
                ", bmi=" + bmi +
                ", axunge=" + axunge +
                ", bone=" + bone +
                ", muscle=" + muscle +
                ", water=" + water +
                ", metabolism=" + metabolism +
                ", body_age=" + body_age +
                ", viscera=" + viscera +
                ", sync_time='" + sync_time + '\'' +
                ", productid=" + productid +
                ", scaleweight='" + scaleweight + '\'' +
                ", r1=" + r1 +
                ", scaleproperty=" + scaleproperty +
                ", yAxis=" + yAxis +
                "} " + super.toString();
    }

    public WeightEntity() {
        setMtype(DataType.WEIGHT.getType());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeFloat(this.weight);
        dest.writeFloat(this.bmi);
        dest.writeFloat(this.axunge);
        dest.writeFloat(this.bone);
        dest.writeFloat(this.muscle);
        dest.writeFloat(this.water);
        dest.writeFloat(this.metabolism);
        dest.writeFloat(this.body_age);
        dest.writeFloat(this.viscera);
        dest.writeString(this.sync_time);
        dest.writeLong(this.productid);
        dest.writeString(this.scaleweight);
        dest.writeFloat(this.r1);
        dest.writeByte(this.scaleproperty);
        dest.writeFloat(this.yAxis);
        dest.writeInt(this.score);
        dest.writeFloat(this.bw);
        dest.writeInt(this.height);
        dest.writeInt(this.sex);
        dest.writeInt(this.age);
        dest.writeString(this.rn8);
    }

    protected WeightEntity(Parcel in) {
        super(in);
        this.weight = in.readFloat();
        this.bmi = in.readFloat();
        this.axunge = in.readFloat();
        this.bone = in.readFloat();
        this.muscle = in.readFloat();
        this.water = in.readFloat();
        this.metabolism = in.readFloat();
        this.body_age = in.readFloat();
        this.viscera = in.readFloat();
        this.sync_time = in.readString();
        this.productid = in.readLong();
        this.scaleweight = in.readString();
        this.r1 = in.readFloat();
        this.scaleproperty = in.readByte();
        this.yAxis = in.readFloat();
        this.score = in.readInt() ;
        this.bw = in.readFloat() ;
        this.height = in.readInt() ;
        this.sex = in.readInt() ;
        this.age = in.readInt() ;
        this.rn8=in.readString();
    }

    public static final Creator<WeightEntity> CREATOR = new Creator<WeightEntity>() {
        @Override
        public WeightEntity createFromParcel(Parcel source) {
            return new WeightEntity(source);
        }

        @Override
        public WeightEntity[] newArray(int size) {
            return new WeightEntity[size];
        }
    };
}
