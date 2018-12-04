package com.jq.code.model.find;

/**
 * Created by xulj on 2016/5/23.
 */
public class FindEntity {
    private int ncomments ;
    private int nlikes ;
    private String categories ;
    private long ts ;
    private String cover ;
    private String uri ;
    private String title ;
    private String id ;

    public int getNcomments() {
        return ncomments;
    }

    public void setNcomments(int ncomments) {
        this.ncomments = ncomments;
    }

    public int getNlikes() {
        return nlikes;
    }

    public void setNlikes(int nlikes) {
        this.nlikes = nlikes;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
