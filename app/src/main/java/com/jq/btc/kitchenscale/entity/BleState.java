package com.jq.btc.kitchenscale.entity;

/**
 * Created by CM on 2018/9/28.
 */

public class BleState {
    int state;

    public BleState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
