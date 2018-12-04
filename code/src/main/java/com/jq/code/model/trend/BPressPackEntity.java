package com.jq.code.model.trend;

import com.jq.code.model.BPressEntity;

import java.util.List;

/**
 * Created by xulj on 2016/6/3.
 */
public class BPressPackEntity {
    public List<BPressTrend> getbPressTrends() {
        return bPressTrends;
    }

    public void setbPressTrends(List<BPressTrend> bPressTrends) {
        this.bPressTrends = bPressTrends;
    }

    private List<BPressTrend> bPressTrends;
    private int xSesion ;
    private List<BPressEntity> dayBPress ;
    private long startTs ;

    public long getStartTs() {
        return startTs;
    }

    public void setStartTs(long startTs) {
        this.startTs = startTs;
    }

    public List<BPressEntity> getDayBPress() {
        return dayBPress;
    }

    public void setDayBPress(List<BPressEntity> dayBPress) {
        this.dayBPress = dayBPress;
    }



    public int getxSesion() {
        return xSesion;
    }

    public void setxSesion(int xSesion) {
        this.xSesion = xSesion;
    }

    @Override
    public String toString() {
        return "BPressPackEntity{" +
                "bPressTrends=" + bPressTrends +
                ", xSesion=" + xSesion +
                ", dayBPress=" + dayBPress +
                ", startTs=" + startTs +
                '}';
    }
}
