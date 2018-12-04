package com.jq.btc.kitchenscale.entity;

import android.annotation.SuppressLint;

import com.jq.code.model.PutBase;

/**
 * Created by CM on 2018/9/29.
 */

@SuppressLint("ParcelCreator")
public class kitchenEntity extends PutBase {

    private String name;
    private String quantity;
    private String food_id;
    private String date;
    private String unit;
    private String calory;
    private String ftype;
    private String metabolism;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getFood_id() {
        return food_id;
    }

    public void setFood_id(String food_id) {
        this.food_id = food_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCalory() {
        return calory;
    }

    public void setCalory(String calory) {
        this.calory = calory;
    }

    public String getFtype() {
        return ftype;
    }

    public void setFtype(String ftype) {
        this.ftype = ftype;
    }

    public String getMetabolism() {
        return metabolism;
    }

    public void setMetabolism(String metabolism) {
        this.metabolism = metabolism;
    }
}
