package com.jq.code.model;

/**
 * Created by Administrator on 2016/12/2.
 */

public class TypeProperty {
    int name;
    int color;

    public TypeProperty(int name, int color) {
        this.name = name;
        this.color = color;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
