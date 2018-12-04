package com.jq.code.model.trend;

/**
 * Created by Administrator on 2016/10/10.
 */

public class SportDetalisViewEntity {
    private String textStr ;
    private int value1;
    private int value2;
    private int value3;
    private float leftAxis ;
    private float value1Axis;
    private float value2Axis;
    private float value3Axis;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SportDetalisViewEntity entity = (SportDetalisViewEntity) o;

        return textStr != null ? textStr.equals(entity.textStr) : entity.textStr == null;

    }

    @Override
    public int hashCode() {
        return textStr != null ? textStr.hashCode() : 0;
    }

    public float getValue3Axis() {
        return value3Axis;

    }

    public void setValue3Axis(float value3Axis) {
        this.value3Axis = value3Axis;
    }

    public int getValue3() {
        return value3;
    }

    public void setValue3(int value3) {
        this.value3 = value3;
    }

    public float getLeftAxis() {
        return leftAxis;
    }

    public void setLeftAxis(float leftAxis) {
        this.leftAxis = leftAxis;
    }

    public float getValue2Axis() {
        return value2Axis;
    }

    public void setValue2Axis(float value2Axis) {
        this.value2Axis = value2Axis;
    }

    public float getValue1Axis() {
        return value1Axis;
    }

    public void setValue1Axis(float value1Axis) {
        this.value1Axis = value1Axis;
    }
    public SportDetalisViewEntity() {
    }
    public SportDetalisViewEntity(String textStr, int value1, int value2,int value3) {
        this.textStr = textStr;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3 ;
    }

    public String getTextStr() {

        return textStr;
    }

    public void setTextStr(String textStr) {
        this.textStr = textStr;
    }

    public int getValue2() {
        return value2;
    }

    public void setValue2(int value2) {
        this.value2 = value2;
    }

    public int getValue1() {
        return value1;
    }

    public void setValue1(int value1) {
        this.value1 = value1;
    }
}
