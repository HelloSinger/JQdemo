package com.jq.code.model;

/**
 * Created by xulj on 2016/5/5.
 */
public class PointHelpEntity {
    private float yValue ;
    private float yYAxis ;
    private int xPosition ;

    public PointHelpEntity(float yValue,  int xPosition) {
        this.yValue = yValue;
        this.xPosition = xPosition;
    }

    public float getyValue() {
        return yValue;
    }

    public void setyValue(float yValue) {
        this.yValue = yValue;
    }

    public float getyYAxis() {
        return yYAxis;
    }

    public void setyYAxis(float yYAxis) {
        this.yYAxis = yYAxis;
    }

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    @Override
    public String toString() {
        return "PointHelpEntity{" +
                "yValue=" + yValue +
                ", yYAxis=" + yYAxis +
                ", xPosition=" + xPosition +
                '}';
    }
}
