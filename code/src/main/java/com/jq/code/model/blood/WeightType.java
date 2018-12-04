package com.jq.code.model.blood;


/**
 * Created by xulj on 2016/5/10.
 */
public class WeightType extends BaseTypeEntity {
    public float weightValue ;

    public WeightType(float weightValue) {
        super();
        this.weightValue = weightValue;
    }

    @Override
    public String setTypeName() {
        return "体重";
    }

    @Override
    public int setTypeRes() {
        return 0;
    }

    @Override
    public float getWightValue() {
        return weightValue;
    }
}
