package com.jq.btlib.model.device;

import java.util.ArrayList;

/**
 * Created by lixun on 2016/1/21.
 */
public class CsFatConfirm {
    private double weight;
    private byte scaleProperty;
    private String scaleWeight;
    private ArrayList<Integer> matchedRoleList;

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight /10;
    }

    public byte getScaleProperty() {
        return scaleProperty;
    }

    public void setScaleProperty(byte property) {
        this.scaleProperty = property;
    }

    public String getScaleWeight() {
        return scaleWeight;
    }

    public void setScaleWeight(String scaleWeight) {
        this.scaleWeight = scaleWeight;
    }

    public ArrayList<Integer> getMatchedRoleList() {
        return matchedRoleList;
    }

    public void setMatchedRoleList(ArrayList<Integer> matchedRoleList) {
        this.matchedRoleList = matchedRoleList;
    }
}
