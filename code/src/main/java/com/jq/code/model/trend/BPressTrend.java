package com.jq.code.model.trend;

/**
 * Created by xulj on 2016/6/1.
 */
public class BPressTrend extends TrendBaseEntity {
    private double minSys ;
    private double maxSys ;
    private double minDia ;
    private double maxDia ;
    private double minHb ;
    private double maxHb ;
    private double nums ;
    private float maxSysAxis ;
    private float minSysAxis ;
    private float maxDiaAxis ;
    private float minDiaAxis ;
    public float getMaxSysAxis() {
        return maxSysAxis;
    }

    public void setMaxSysAxis(float maxSysAxis) {
        this.maxSysAxis = maxSysAxis;
    }

    public float getMinSysAxis() {
        return minSysAxis;
    }

    public void setMinSysAxis(float minSysAxis) {
        this.minSysAxis = minSysAxis;
    }

    public float getMaxDiaAxis() {
        return maxDiaAxis;
    }

    public void setMaxDiaAxis(float maxDiaAxis) {
        this.maxDiaAxis = maxDiaAxis;
    }

    public float getMinDiaAxis() {
        return minDiaAxis;
    }

    public void setMinDiaAxis(float minDiaAxis) {
        this.minDiaAxis = minDiaAxis;
    }


    public double getMinSys() {
        return minSys;
    }

    public void setMinSys(double minSys) {
        this.minSys = minSys;
    }

    public double getMaxSys() {
        return maxSys;
    }

    public void setMaxSys(double maxSys) {
        this.maxSys = maxSys;
    }

    public double getMinDia() {
        return minDia;
    }

    public void setMinDia(double minDia) {
        this.minDia = minDia;
    }

    public double getMaxDia() {
        return maxDia;
    }

    public void setMaxDia(double maxDia) {
        this.maxDia = maxDia;
    }

    public double getMinHb() {
        return minHb;
    }

    public void setMinHb(double minHb) {
        this.minHb = minHb;
    }

    public double getMaxHb() {
        return maxHb;
    }

    public void setMaxHb(double maxHb) {
        this.maxHb = maxHb;
    }

    public double getNums() {
        return nums;
    }

    public void setNums(double nums) {
        this.nums = nums;
    }


}
