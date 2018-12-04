package com.jq.code.model;

/**
 * Created by Administrator on 2016/12/23.
 */

public class WeightTipEntity {
    int id;
    String sex;
    int age_min;
    int age_max;
    float bmi_min;
    float bmi_max;
    float fat_min;
    float fat_max;
    String body_type;
    String tip;
    int standard; //0标准型，1偏瘦型，2偏胖型，3超重型

    public WeightTipEntity() {
    }

    public WeightTipEntity(String sex, int age_min, int age_max, float bmi_min, float bmi_max, float fat_min, float fat_max, String body_type, String tip) {
        this.sex = sex;
        this.age_min = age_min;
        this.age_max = age_max;
        this.bmi_min = bmi_min;
        this.bmi_max = bmi_max;
        this.fat_min = fat_min;
        this.fat_max = fat_max;
        this.body_type = body_type;
        this.tip = tip;
    }

    public int getId() {
        return id;
    }

    public String getSex() {
        return sex;
    }

    public int getAge_min() {
        return age_min;
    }

    public int getAge_max() {
        return age_max;
    }

    public float getBmi_min() {
        return bmi_min;
    }

    public float getBmi_max() {
        return bmi_max;
    }

    public float getFat_min() {
        return fat_min;
    }

    public float getFat_max() {
        return fat_max;
    }

    public String getBody_type() {
        return body_type;
    }

    public String getTip() {
        return tip;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStandard() {
        return standard;
    }

    public void setStandard(int standard) {
        this.standard = standard;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setAge_min(int age_min) {
        this.age_min = age_min;
    }

    public void setAge_max(int age_max) {
        this.age_max = age_max;
    }

    public void setBmi_min(float bmi_min) {
        this.bmi_min = bmi_min;
    }

    public void setBmi_max(float bmi_max) {
        this.bmi_max = bmi_max;
    }

    public void setFat_min(float fat_min) {
        this.fat_min = fat_min;
    }

    public void setFat_max(float fat_max) {
        this.fat_max = fat_max;
    }

    public void setBody_type(String body_type) {
        this.body_type = body_type;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }
}
