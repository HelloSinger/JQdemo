package com.jq.code.model.sport;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/10/20.
 */

public class SportEntity implements Parcelable {
    private int id ;
    private String name ;
    private float met ;
    private String type ;
    private int usedegree ;
    private boolean isCheck ;
    private int min ;
    private int kilo ;
    public int getKilo() {
        return kilo;
    }

    public void setKilo(int kilo) {
        this.kilo = kilo;
    }



    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getMet() {
        return met;
    }

    public void setMet(float met) {
        this.met = met;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUsedegree() {
        return usedegree;
    }

    public void setUsedegree(int usedegree) {
        this.usedegree = usedegree;
    }



    public SportEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SportEntity that = (SportEntity) o;

        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeFloat(this.met);
        dest.writeString(this.type);
        dest.writeInt(this.usedegree);
        dest.writeByte(this.isCheck ? (byte) 1 : (byte) 0);
        dest.writeInt(this.min);
        dest.writeInt(this.kilo);
    }

    protected SportEntity(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.met = in.readFloat();
        this.type = in.readString();
        this.usedegree = in.readInt();
        this.isCheck = in.readByte() != 0;
        this.min = in.readInt();
        this.kilo = in.readInt();
    }

    public static final Creator<SportEntity> CREATOR = new Creator<SportEntity>() {
        @Override
        public SportEntity createFromParcel(Parcel source) {
            return new SportEntity(source);
        }

        @Override
        public SportEntity[] newArray(int size) {
            return new SportEntity[size];
        }
    };
}
