package com.jq.code.model.trend;

/**
 * Created by xulj on 2016/6/1.
 */
public class BGlucoseTrend extends TrendBaseEntity {
    private double minBsl ;
    private double maxBsl ;
    private double nums ;
    private float minAxis ;
    private float maxAxis ;

    public float getMinAxis() {
        return minAxis;
    }

    public void setMinAxis(float minAxis) {
        this.minAxis = minAxis;
    }

    public float getMaxAxis() {
        return maxAxis;
    }

    public void setMaxAxis(float maxAxis) {
        this.maxAxis = maxAxis;
    }

    public double getNums() {
        return nums;
    }

    public void setNums(double nums) {
        this.nums = nums;
    }

    public double getMaxBsl() {
        return maxBsl;
    }

    public void setMaxBsl(double maxBsl) {
        this.maxBsl = maxBsl;
    }

    public double getMinBsl() {
        return minBsl;
    }

    public void setMinBsl(double minBsl) {
        this.minBsl = minBsl;
    }

}
