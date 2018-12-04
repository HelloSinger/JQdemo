package com.jq.code.model;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xulj on 2016/5/25.
 */
public class BGlucoseEntity extends PutBase {

    private float bsl;
    private int description;
    private float axis;

    public static class BGlucoseType {
        public static List<String> create() {
            List<String> comentData  = new ArrayList<String>();
            comentData.add("空腹");
            comentData.add("早餐后");
            comentData.add("午餐前");
            comentData.add("午餐后");
            comentData.add("晚餐前");
            comentData.add("晚餐后");
            return comentData;
        }
    }

    public float getBsl() {
        return bsl;
    }

    public void setBsl(float bsl) {
        this.bsl = bsl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getDescription() {
        return description;
    }

    public void setDescription(int description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "BGlucoseEntity{" +
                "bsl=" + bsl +
                ", description='" + description + '\'' +
                "} " + super.toString();
    }

    public float getAxis() {
        return axis;
    }

    public void setAxis(float axis) {
        this.axis = axis;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeFloat(this.bsl);
        dest.writeInt(this.description);
        dest.writeFloat(axis);

    }

    public BGlucoseEntity() {
        setMtype(DataType.BSL.getType());
    }

    protected BGlucoseEntity(Parcel in) {
        super(in);
        this.bsl = in.readFloat();
        this.description = in.readInt();
        this.axis = in.readFloat();
    }

    public static final Creator<BGlucoseEntity> CREATOR = new Creator<BGlucoseEntity>() {
        @Override
        public BGlucoseEntity createFromParcel(Parcel source) {
            return new BGlucoseEntity(source);
        }

        @Override
        public BGlucoseEntity[] newArray(int size) {
            return new BGlucoseEntity[size];
        }
    };
}
