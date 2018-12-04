package com.jq.code.model.sport;

import java.util.List;

/**
 * Created by Administrator on 2016/11/8.
 */

public class FoodDetailEntity {
    String name;
    float value;
    float goal;
    float limit;
    String unit;
    List<FoodMicroelementEntity> entities;

    public List<FoodMicroelementEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<FoodMicroelementEntity> entities) {
        this.entities = entities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getGoal() {
        return goal;
    }

    public void setGoal(float goal) {
        this.goal = goal;
    }

    public float getLimit() {
        return limit;
    }

    public void setLimit(float limit) {
        this.limit = limit;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
