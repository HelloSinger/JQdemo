package com.jq.code.model.blood;

/**
 * Created by xulj on 2016/5/10.
 */
public abstract class BaseTypeEntity {
    protected String typeName ;
    protected int typeRes ;

    public BaseTypeEntity() {
        this.typeName = setTypeName();
        this.typeRes = setTypeRes() ;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getTypeRes() {
        return typeRes;
    }

    public abstract String setTypeName() ;
    public abstract int setTypeRes() ;
    public  float getBGlucoseValue(){
        return 0f ;
    }
    public  float getBPressSysValue(){
        return 0f ;
    }
    public  float getBPressDiaValue(){
        return 0f ;
    }

    public  float getWightValue() {
        return 0f ;
    }
    public  float getFatValue() {
        return 0f ;
    }
}
