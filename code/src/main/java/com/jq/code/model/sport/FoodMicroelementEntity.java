package com.jq.code.model.sport;

import android.os.Parcel;

/**
 * Created by Administrator on 2016/11/4.
 */

public class FoodMicroelementEntity extends BiteEnty {
    float quantity;
    String date;
    String unit;

    @Override
    public String toString() {
        return "FoodMicroelementEntity{" +
                "quantity=" + quantity +
                ", date='" + date + '\'' +
                ", unit='" + unit + '\'' +
                "} " + super.toString();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public FoodMicroelementEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeFloat(this.quantity);
        dest.writeString(this.date);
        dest.writeString(this.unit);
    }

    protected FoodMicroelementEntity(Parcel in) {
        super(in);
        this.quantity = in.readFloat();
        this.date = in.readString();
        this.unit = in.readString();
    }

    public static final Creator<FoodMicroelementEntity> CREATOR = new Creator<FoodMicroelementEntity>() {
        @Override
        public FoodMicroelementEntity createFromParcel(Parcel source) {
            return new FoodMicroelementEntity(source);
        }

        @Override
        public FoodMicroelementEntity[] newArray(int size) {
            return new FoodMicroelementEntity[size];
        }
    };
}
