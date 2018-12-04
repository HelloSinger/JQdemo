package com.jq.code.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xulj on 2016/5/25.
 */
public class PutBase implements Parcelable, Displayable {
    public final static String INTENT_FLAG = "PutBase";
    public static final String TYPE_ALL = "all";
    public static final String TYPE_EXERCISE_FOOD = "exercise_food";
    protected String measure_time;
    protected String weight_time;
    protected String upload_time;
    protected String mtype;
    protected long role_id;
    protected long account_id;
    protected long id;
    protected long _id;
    protected int delete; // =1待删除
    protected int xPosition;

    @Override
    public String toString() {
        return "PutBase{" +
                "measure_time='" + measure_time + '\'' +
                ", weight_time='" + weight_time + '\'' +
                ", upload_time='" + upload_time + '\'' +
                ", mtype='" + mtype + '\'' +
                ", role_id=" + role_id +
                ", account_id=" + account_id +
                ", id=" + id +
                ", _id=" + _id +
                ", delete=" + delete +
                ", xPosition=" + xPosition +
                '}';
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(String upload_time) {
        this.upload_time = upload_time;
    }

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getDelete() {
        return delete;
    }

    public void setDelete(int delete) {
        this.delete = delete;
    }

    public String getMeasure_time() {
        return measure_time;
    }

    public void setMeasure_time(String measure_time) {
        this.weight_time = measure_time;
        this.measure_time = measure_time;
    }

    public String getWeight_time() {
        return weight_time;
    }

    public void setWeight_time(String weight_time) {
        this.weight_time = weight_time;
        this.measure_time = weight_time;
    }

    public String getMtype() {
        return mtype;
    }

    public void setMtype(String mtype) {
        this.mtype = mtype;
    }

    public long getRole_id() {
        return role_id;
    }

    public void setRole_id(long role_id) {
        this.role_id = role_id;
    }

    public long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(long account_id) {
        this.account_id = account_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PutBase() {
    }

    public boolean isFe() {
        return getMtype().equals(DataType.FOOD.getType()) || getMtype().equals(DataType.EXERCISE.getType());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.measure_time);
        dest.writeString(this.weight_time);
        dest.writeString(this.upload_time);
        dest.writeString(this.mtype);
        dest.writeLong(this.role_id);
        dest.writeLong(this.account_id);
        dest.writeLong(this.id);
        dest.writeLong(this._id);
        dest.writeInt(this.delete);
        dest.writeInt(this.xPosition);
    }

    protected PutBase(Parcel in) {
        this.measure_time = in.readString();
        this.weight_time = in.readString();
        this.upload_time = in.readString();
        this.mtype = in.readString();
        this.role_id = in.readLong();
        this.account_id = in.readLong();
        this.id = in.readLong();
        this._id = in.readLong();
        this.delete = in.readInt();
        this.xPosition = in.readInt();
    }

}
