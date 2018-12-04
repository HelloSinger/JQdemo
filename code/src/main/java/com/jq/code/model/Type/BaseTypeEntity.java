package com.jq.code.model.Type;

/**
 * Created by xulj on 2016/5/10.
 */
public abstract class BaseTypeEntity {
    protected String typeName;
    protected int typeRes;
    protected String time;
    private boolean hasHead;
    private boolean canDelete;

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public boolean isHasHead() {
        return hasHead;
    }

    public void setHasHead(boolean hasHead) {
        this.hasHead = hasHead;
    }

    public BaseTypeEntity() {
        this.typeName = setTypeName();
        this.typeRes = setTypeRes();
    }

    public String getTypeName() {
        return typeName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTypeRes() {
        return typeRes;
    }

    public abstract String setTypeName();

    public abstract int setTypeRes();

    public float getBGlucoseValue() {
        return 0f;
    }

    public int getBPressSysValue() {
        return 0;
    }

    public int getBPressDiaValue() {
        return 0;
    }

    public String getWightValue() {
        return "0";
    }

    public float getFatValue() {
        return 0f;
    }

    @Override
    public String toString() {
        return "BaseTypeEntity{" +
                "typeName='" + typeName + '\'' +
                ", typeRes=" + typeRes +
                ", time='" + time + '\'' +
                ", hasHead=" + hasHead +
                ", canDelete=" + canDelete +
                '}';
    }
}
