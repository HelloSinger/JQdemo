package com.jq.code.model;

/**
 * Created by hfei on 2015/11/23.
 */
public class SyncDataInfo {

    private long account_id;
    private long role_id;
    private String mtype;
    private long lastsync;
    private long start;
    private long end;

    public SyncDataInfo() {
    }

    public SyncDataInfo(long account_id, long role_id, String mtype, long lastsync, long start, long end) {
        this.account_id = account_id;
        this.role_id = role_id;
        this.mtype = mtype;
        this.lastsync = lastsync;
        this.start = start;
        this.end = end;
    }

    public long getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "SyncDataInfo{" +
                "account_id=" + account_id +
                ", role_id=" + role_id +
                ", mtype='" + mtype + '\'' +
                ", lastsync=" + lastsync +
                ", start=" + start +
                ", end=" + end +
                '}';
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(long account_id) {
        this.account_id = account_id;
    }

    public long getRole_id() {
        return role_id;
    }

    public void setRole_id(long role_id) {
        this.role_id = role_id;
    }

    public long getLastsync() {
        return lastsync;
    }

    public void setLastsync(long lastsync) {
        this.lastsync = lastsync;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public String getMtype() {
        return mtype;
    }

    public void setMtype(String mtype) {
        this.mtype = mtype;
    }
}
