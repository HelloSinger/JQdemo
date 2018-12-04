package com.jq.code.model.Type;


/**
 * Created by xulj on 2016/5/10.
 */
public class BPressType extends BaseTypeEntity {
    public int bPressSysValue ;
    public int bPresDiaValue ;

    public BPressType(int bPressSysValue, int bPresDiaValue) {
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
    public int getBPressDiaValue() {
        return bPresDiaValue;
    }

    @Override
    public int getBPressSysValue() {
        return bPressSysValue;
    }
}
