package com.jq.code.model.sport;

import android.os.Parcel;

import com.jq.code.model.DataType;
import com.jq.code.model.PutBase;

/**
 * Created by Administrator on 2016/10/23.
 */

public class SubmitSportEntity extends PutBase {
    private String name;
    private int duration;
    private String date;
    private int ex_id;
    private int calory;
    private int metabolism;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        setMeasure_time(date);
        setWeight_time(date);
    }

    public int getEx_id() {
        return ex_id;
    }

    public void setEx_id(int ex_id) {
        this.ex_id = ex_id;
    }

    public int getCalory() {
        return calory;
    }

    public void setCalory(int calory) {
        this.calory = calory;
    }

    public int getMetabolism() {
        return metabolism;
    }

    public void setMetabolism(int metabolism) {
        this.metabolism = metabolism;
    }

    @Override
    public String toString() {
        return "SubmitSportEntity{" +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                ", date='" + date + '\'' +
                ", ex_id=" + ex_id +
                ", calory=" + calory +
                ", metabolism=" + metabolism +
                "} " + super.toString();
    }

    public SubmitSportEntity() {
        setMtype(DataType.EXERCISE.getType());
    }

    public SubmitSportEntity(int role_id, String name, int duration, String date, int ex_id, int calory) {
        setRole_id(role_id);
        setMtype(DataType.EXERCISE.getType());
        this.name = name;
        this.duration = duration;
        this.date = date;
        setMeasure_time(date);
        setWeight_time(date);
        this.ex_id = ex_id;
        this.calory = calory;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.name);
        dest.writeInt(this.duration);
        dest.writeString(this.date);
        dest.writeInt(this.ex_id);
        dest.writeInt(this.calory);
        dest.writeInt(this.metabolism);
    }

    protected SubmitSportEntity(Parcel in) {
        super(in);
        this.name = in.readString();
        this.duration = in.readInt();
        this.date = in.readString();
        this.ex_id = in.readInt();
        this.calory = in.readInt();
        this.metabolism = in.readInt();
    }

    public static final Creator<SubmitSportEntity> CREATOR = new Creator<SubmitSportEntity>() {
        @Override
        public SubmitSportEntity createFromParcel(Parcel source) {
            return new SubmitSportEntity(source);
        }

        @Override
        public SubmitSportEntity[] newArray(int size) {
            return new SubmitSportEntity[size];
        }
    };
}
