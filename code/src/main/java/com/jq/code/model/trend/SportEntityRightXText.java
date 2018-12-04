package com.jq.code.model.trend;

/**
 * Created by xulj on 2016/7/28.
 */
public class SportEntityRightXText {
    private float leftAxis ;
    private float value1 ,value2 , value3;
    private float valueAxis1,valueAxis2,valueAxis3 ;
    private int position ;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public SportEntityRightXText(float value1, float value2, float value3 , int position) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.position = position ;

    }
    public float getLeftAxis() {
        return leftAxis;
    }

    public void setLeftAxis(float leftAxis) {
        this.leftAxis = leftAxis;
    }

    public float getValue1() {
        return value1;
    }

    public void setValue1(float value1) {
        this.value1 = value1;
    }

    public float getValue2() {
        return value2;
    }

    public void setValue2(float value2) {
        this.value2 = value2;
    }

    public float getValue3() {
        return value3;
    }

    public void setValue3(float value3) {
        this.value3 = value3;
    }

    public float getValueAxis1() {
        return valueAxis1;
    }

    public void setValueAxis1(float valueAxis1) {
        this.valueAxis1 = valueAxis1;
    }

    public float getValueAxis2() {
        return valueAxis2;
    }

    public void setValueAxis2(float valueAxis2) {
        this.valueAxis2 = valueAxis2;
    }

    public float getValueAxis3() {
        return valueAxis3;
    }

    public void setValueAxis3(float valueAxis3) {
        this.valueAxis3 = valueAxis3;
    }
}
