package com.jq.code.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 秤数据信息实体类
 */
public class WeightTmpEntity implements Parcelable {

    private int id;
    private long account_id;
    private float weight;
    private String weight_time;
    /**电阻值**/
    private float impedance;
    //蓝牙秤显示的体重数值
    private String scaleweight;
    /**
     * 蓝牙秤显示的体重属性
     * 如果当前单位为英石:磅, ScaleProperty固定设置为10进制的25
     * 如果单位为jin，ScaleProperty固定设置为10进制的9
     * 如果单位为lb,ScaleProperty固定设置为10进制的17
     * 如果单位为kg,ScaleProperty固定设置为10进制的1
     */
    private byte scaleproperty;

    @Override
    public String toString() {
        return "WeightTmpEntity{" +
                "id=" + id +
                ", account_id=" + account_id +
                ", weight=" + weight +
                ", weight_time='" + weight_time + '\'' +
                ", impedance=" + impedance +
                ", scaleweight='" + scaleweight + '\'' +
                ", scaleproperty=" + scaleproperty +
                '}';
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(long account_id) {
        this.account_id = account_id;
    }


    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getWeight_time() {
        return weight_time;
    }

    public void setWeight_time(String weight_time) {
        this.weight_time = weight_time;
    }

    public float getImpedance() {
        return impedance;
    }

    public void setImpedance(float impedance) {
        this.impedance = impedance;
    }

    public WeightTmpEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeLong(this.account_id);
        dest.writeFloat(this.weight);
        dest.writeString(this.weight_time);
        dest.writeFloat(this.impedance);
        dest.writeString(this.scaleweight);
        dest.writeByte(this.scaleproperty);
    }

    protected WeightTmpEntity(Parcel in) {
        this.id = in.readInt();
        this.account_id = in.readLong();
        this.weight = in.readFloat();
        this.weight_time = in.readString();
        this.impedance = in.readFloat();
        this.scaleweight = in.readString();
        this.scaleproperty = in.readByte();
    }

    public static final Creator<WeightTmpEntity> CREATOR = new Creator<WeightTmpEntity>() {
        @Override
        public WeightTmpEntity createFromParcel(Parcel source) {
            return new WeightTmpEntity(source);
        }

        @Override
        public WeightTmpEntity[] newArray(int size) {
            return new WeightTmpEntity[size];
        }
    };

    public WeightEntity getWeightEntity() {
        WeightEntity entity = new WeightEntity();
        entity.setAccount_id(account_id);
        entity.setWeight(weight);
        entity.setWeight_time(weight_time);
        entity.setR1(impedance);
        entity.setScaleproperty(scaleproperty);
        entity.setScaleweight(scaleweight);
        return entity;
    }
}
