package com.jq.code.model.trend;

import com.jq.code.model.WeightEntity;
import com.jq.code.model.XHelpEntity;

import java.util.List;

/**
 * Created by xulj on 2016/6/3.
 */
public class WeightPackEntity {
    private List<XHelpEntity> xHelpEntities;
    private List<WeightEntity> weightEntities ;
    private int xSesion ;
    private String showType ;

    public List<XHelpEntity> getxHelpEntities() {
        return xHelpEntities;
    }

    public void setxHelpEntities(List<XHelpEntity> xHelpEntities) {
        this.xHelpEntities = xHelpEntities;
    }

    public List<WeightEntity> getWeightEntities() {
        return weightEntities;
    }

    public void setWeightEntities(List<WeightEntity> weightEntities) {
        this.weightEntities = weightEntities;
    }

    public int getxSesion() {
        return xSesion;
    }

    public void setxSesion(int xSesion) {
        this.xSesion = xSesion;
    }

    public String getShowType() {
        return showType;

    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

}
