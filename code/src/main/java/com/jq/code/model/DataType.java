package com.jq.code.model;

/**
 * Created by Administrator on 2016/11/16.
 */

public enum DataType {
    EXERCISE("exercise"), FOOD("food"), WEIGHT("weight"), BP("bp"), BSL("bsl"), ;

    String type;

    DataType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public boolean isThis(String type) {
        return type.equals(this.type);
    }

    public static DataType curAddType = EXERCISE;
}
