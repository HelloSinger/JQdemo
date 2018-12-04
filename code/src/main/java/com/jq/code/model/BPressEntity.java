package com.jq.code.model;

import android.os.Parcel;

/**
 * Created by xulj on 2016/5/25.
 */
public class BPressEntity extends PutBase {
    private int sys  ;
    private int dia  ;
    private  int hb  ;
    private float sysAxis ;
    private float diaAxis ;

    public float getSysAxis() {
        return sysAxis;
    }

    public void setSysAxis(float sysAxis) {
        this.sysAxis = sysAxis;
    }

    public float getDiaAxis() {
        return diaAxis;
    }

    public void setDiaAxis(float diaAxis) {
        this.diaAxis = diaAxis;
    }

    public int getSys() {
        return sys;
    }

    public void setSys(int sys) {
        this.sys = sys;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getHb() {
        return hb;
    }

    public void setHb(int hb) {
        this.hb = hb;
    }

    @Override
    public String toString() {
        return "BPressEntity{" +
                "sys=" + sys +
                ", dia=" + dia +
                ", hb=" + hb +
                "} " + super.toString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest,flags);
        dest.writeInt(this.sys);
        dest.writeInt(this.dia);
        dest.writeInt(this.hb);
        dest.writeFloat(this.sysAxis);
        dest.writeFloat(this.diaAxis);
    }

    public BPressEntity() {
        setMtype(DataType.BP.getType());
    }

    protected BPressEntity(Parcel in) {
        super(in);
        this.sys = in.readInt();
        this.dia = in.readInt();
        this.hb = in.readInt();
        this.sysAxis = in.readFloat() ;
        this.sysAxis = in.readFloat() ;
    }

    public static final Creator<BPressEntity> CREATOR = new Creator<BPressEntity>() {
        @Override
        public BPressEntity createFromParcel(Parcel source) {
            return new BPressEntity(source);
        }

        @Override
        public BPressEntity[] newArray(int size) {
            return new BPressEntity[size];
        }
    };
}
