package com.jq.code.model;

/**
 * Created by Administrator on 2016/6/8.
 */
public class DeviceEntity {
    String typeName;
    int typeImge;
    String name;
    boolean isBound;

    public DeviceEntity(String typeName, int typeImge, String name, boolean isBound) {
        this.typeName = typeName;
        this.typeImge = typeImge;
        this.name = name;
        this.isBound = isBound;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTypeImge() {
        return typeImge;
    }

    public void setTypeImge(int typeImge) {
        this.typeImge = typeImge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBound() {
        return isBound;
    }

    public void setBound(boolean bound) {
        isBound = bound;
    }
}
