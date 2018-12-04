package com.jq.code.model.Type;


/**
 * Created by xulj on 2016/5/10.
 */
public class BGlucoseType extends BaseTypeEntity {
    public float bGlucoseValue ;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BGlucoseType(float bGlucoseValue) {
        super();
        this.bGlucoseValue = bGlucoseValue;
    }

    @Override
    public String setTypeName() {
        return "血糖";
    }

    @Override
    public int setTypeRes() {
        return 1;
    }


    @Override
    public float getBGlucoseValue() {
        return bGlucoseValue;
    }
}
