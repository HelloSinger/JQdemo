package com.jq.code.model;


public class SwipeDataItem {
    String ImgeUrl;
    String nickName;
    String date;
    String time;
    float weight;
    float axunge;
    String scaleWeight;
    byte scaleProperty;

    public String getScaleWeight() {
        return scaleWeight;
    }

    public void setScaleWeight(String scaleWeight) {
        this.scaleWeight = scaleWeight;
    }

    public byte getScaleProperty() {
        return scaleProperty;
    }

    public void setScaleProperty(byte scaleProperty) {
        this.scaleProperty = scaleProperty;
    }

    boolean hasHeadable;

    public String getImgeUrl() {
        return ImgeUrl;
    }

    public void setImgeUrl(String imgeUrl) {
        ImgeUrl = imgeUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getAxunge() {
        return axunge;
    }

    public void setAxunge(float axunge) {
        this.axunge = axunge;
    }

    public boolean isHasHeadable() {
        return hasHeadable;
    }

    public void setHasHeadable(boolean hasHeadable) {
        this.hasHeadable = hasHeadable;
    }

    @Override
    public String toString() {
        return "SwipeDataItem{" +
                "ImgeUrl='" + ImgeUrl + '\'' +
                ", nickName='" + nickName + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", weight=" + weight +
                ", axunge=" + axunge +
                ", hasHeadable=" + hasHeadable +
                '}';
    }
}