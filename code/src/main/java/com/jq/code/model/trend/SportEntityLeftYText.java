package com.jq.code.model.trend;

/**
 * Created by xulj on 2016/7/28.
 */
public class SportEntityLeftYText {
    private int value ;
    private float yAxis ;

    public SportEntityLeftYText(int value) {
        this.value = value;
    }


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public float getyAxis() {
        return yAxis;
    }

    public void setyAxis(float yAxis) {
        this.yAxis = yAxis;
    }
}
