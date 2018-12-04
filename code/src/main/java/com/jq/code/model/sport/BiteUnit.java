package com.jq.code.model.sport;

/**
 * Created by Administrator on 2016/10/24.
 */

public class BiteUnit {
    private int unit_id ;
    private float amount ;
    private String unit ;
    private float weight ;
    private float eat_weight ;
    private float calory ;

    public int getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(int unit_id) {
        this.unit_id = unit_id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getEat_weight() {
        return eat_weight;
    }

    public void setEat_weight(float eat_weight) {
        this.eat_weight = eat_weight;
    }

    public float getCalory() {
        return calory;
    }

    public void setCalory(float calory) {
        this.calory = calory;
    }
    public float getUnitKilo(){
        return calory/amount ;
    }

    @Override
    public String toString() {
        return "BiteUnit{" +
                "unit_id=" + unit_id +
                ", amount=" + amount +
                ", unit='" + unit + '\'' +
                ", weight=" + weight +
                ", eat_weight=" + eat_weight +
                ", calory=" + calory +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BiteUnit biteUnit = (BiteUnit) o;

        return unit_id == biteUnit.unit_id;

    }

    @Override
    public int hashCode() {
        return unit_id;
    }
}
