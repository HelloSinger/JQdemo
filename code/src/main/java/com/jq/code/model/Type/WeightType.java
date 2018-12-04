package com.jq.code.model.Type;


/**
 * Created by xulj on 2016/5/10.
 */
public class WeightType extends BaseTypeEntity {
    public String weightValue ;
    public float fatValue;

    public WeightType(String weightValue,float fatValue) {
        super();
        this.weightValue = weightValue;
        this.fatValue = fatValue;
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
    public String getWightValue() {
        return weightValue;
    }

    @Override
    public float getFatValue() {
        return fatValue;
    }
}
