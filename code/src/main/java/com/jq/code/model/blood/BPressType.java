package com.jq.code.model.blood;


/**
 * Created by xulj on 2016/5/10.
 */
public class BPressType extends BaseTypeEntity {
    public float bPressSysValue ;
    public float bPresDiaValue ;

    public BPressType(float bPressSysValue, float bPresDiaValue) {
        super();
        this.bPressSysValue = bPressSysValue;
        this.bPresDiaValue = bPresDiaValue;
    }

    @Override
    public String setTypeName() {
        return "血压";
    }

    @Override
    public int setTypeRes() {
        return 2;
    }

    @Override
    public float getBPressDiaValue() {
        return bPresDiaValue;
    }

    @Override
    public float getBPressSysValue() {
        return bPressSysValue;
    }
}
