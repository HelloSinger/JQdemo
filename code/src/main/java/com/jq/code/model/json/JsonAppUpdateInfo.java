package com.jq.code.model.json;

import java.io.Serializable;

/**
 * 获取重量数据
 */
public class JsonAppUpdateInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int company_id;
    private String region;
    private String required;
    private String version;
    private String url;
    private String upgrade_time;
    private String content;
    private String system_version;
    private int id;
    public static final String JSON_APP_UPDATE_FLAG = "JsonAppUpdateInfo";

    @Override
    public String toString() {
        return "JsonAppUpdateInfo{" +
                "company_id=" + company_id +
                ", region='" + region + '\'' +
                ", required='" + required + '\'' +
                ", version='" + version + '\'' +
                ", url='" + url + '\'' +
                ", upgrade_time='" + upgrade_time + '\'' +
                ", content='" + content + '\'' +
                ", system_version='" + system_version + '\'' +
                ", id=" + id +
                '}';
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSystem_version() {
        return system_version;
    }

    public void setSystem_version(String system_version) {
        this.system_version = system_version;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUpgrade_time() {
        return upgrade_time;
    }

    public void setUpgrade_time(String upgrade_time) {
        this.upgrade_time = upgrade_time;
    }
}
