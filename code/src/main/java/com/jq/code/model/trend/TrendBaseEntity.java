package com.jq.code.model.trend;

/**
 * Created by xulj on 2016/6/14.
 */
public class TrendBaseEntity {
    private long role_id ;
    private long account_id ;
    private int xPosition ;
    private long date ;
    public long getRole_id() {
        return role_id;
    }

    public void setRole_id(long role_id) {
        this.role_id = role_id;
    }

    public long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(long account_id) {
        this.account_id = account_id;
    }

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }


}
