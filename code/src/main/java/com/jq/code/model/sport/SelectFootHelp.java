package com.jq.code.model.sport;

/**
 * Created by Administrator on 2016/10/24.
 */

public class SelectFootHelp {
    private String type;
    private BiteEnty biteEnty ;
    private float selectNumber ;
    private BiteUnit biteUnit ;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SelectFootHelp() {
    }
    public SelectFootHelp(BiteEnty biteEnty, float selectNumber, BiteUnit biteUnit) {
        this.biteEnty = biteEnty;
        this.selectNumber = selectNumber;
        this.biteUnit = biteUnit;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SelectFootHelp that = (SelectFootHelp) o;

        if (!biteEnty.equals(that.biteEnty)) return false;
        return biteUnit.equals(that.biteUnit);

    }

    @Override
    public int hashCode() {
        int result = biteEnty.hashCode();
        result = 31 * result + biteUnit.hashCode();
        return result;
    }

    public BiteEnty getBiteEnty() {
        return biteEnty;
    }

    public void setBiteEnty(BiteEnty biteEnty) {
        this.biteEnty = biteEnty;
    }

    public float getSelectNumber() {
        return selectNumber;
    }

    public void setSelectNumber(float selectNumber) {
        this.selectNumber = selectNumber;
    }

    public BiteUnit getBiteUnit() {
        return biteUnit;
    }

    public void setBiteUnit(BiteUnit biteUnit) {
        this.biteUnit = biteUnit;
    }
}
