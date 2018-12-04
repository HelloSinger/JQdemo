package com.jq.code.model;

import java.util.List;

/**
 * Created by Administrator on 2016/9/7.
 */
public class FeedbackEntity{
    List<FeedbackReplyEntity> replies;
    String auid;
    String status;
    String ua;
    String appid;
    long nts;
    long ts;
    String content;
    long id;
    long account_id;
    int reply; //0是未回复或者已打开，1是已回复未打开

    @Override
    public String toString() {
        return "FeedbackEntity{" +
                "replies=" + replies +
                ", auid='" + auid + '\'' +
                ", status='" + status + '\'' +
                ", ua='" + ua + '\'' +
                ", appid='" + appid + '\'' +
                ", nts=" + nts +
                ", ts=" + ts +
                ", content='" + content + '\'' +
                ", id=" + id +
                ", account_id=" + account_id +
                ", reply=" + reply +
                '}';
    }

    public int getReply() {
        return reply;
    }

    public void setReply(int reply) {
        this.reply = reply;
    }

    public long getAccount_id() {
        return account_id;
    }
    public void setAccount_id(long account_id) {
        this.account_id = account_id;
    }

    public List<FeedbackReplyEntity> getReplies() {
        return replies;
    }

    public void setReplies(List<FeedbackReplyEntity> replies) {
        this.replies = replies;
    }

    public String getAuid() {
        return auid;
    }

    public void setAuid(String auid) {
        this.auid = auid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public long getNts() {
        return nts;
    }

    public void setNts(long nts) {
        this.nts = nts;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
