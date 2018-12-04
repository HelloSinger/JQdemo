package com.jq.code.model.trend;

import com.jq.code.model.BGlucoseEntity;

import java.util.List;

/**
 * Created by xulj on 2016/6/3.
 */
public class BGlucosePackEntity {
    private List<BGlucoseTrend> bGlucoseTrends;
    private int xSesion ;
    private List<BGlucoseEntity> dayBGlucose ;
    private long startTs ;

    public long getStartTs() {
        return startTs;
    }

    public void setStartTs(long startTs) {
        this.startTs = startTs;
    }

    public List<BGlucoseEntity> getDayBGlucose() {
        return dayBGlucose;
    }

    public void setDayBGlucose(List<BGlucoseEntity> dayBGlucose) {
        this.dayBGlucose = dayBGlucose;
    }

    public int getxSesion() {
        return xSesion;
    }

    public void setxSesion(int xSesion) {
        this.xSesion = xSesion;
    }

    public List<BGlucoseTrend> getbGlucoseTrends() {
        return bGlucoseTrends;
    }

    public void setbGlucoseTrends(List<BGlucoseTrend> bGlucoseTrends) {
        this.bGlucoseTrends = bGlucoseTrends;
    }
}
