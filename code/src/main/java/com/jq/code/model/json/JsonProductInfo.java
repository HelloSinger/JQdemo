package com.jq.code.model.json;


import com.jq.code.model.AccountEntity;
import com.jq.code.model.ProductInfo;

public class JsonProductInfo {

    private ProductInfo zh;
    private ProductInfo en;
    private String product_desc;
    private String product_model;
    private String logo_path;
    private int company_id = 1; // 默认ID为1


    public String getLogo_path() {
        return logo_path;
    }

    public void setLogo_path(String logo_path) {
        this.logo_path = logo_path;
    }

    public ProductInfo getZh() {
        return zh;
    }

    public void setZh(ProductInfo zh) {
        this.zh = zh;
    }

    public ProductInfo getEn() {
        return en;
    }

    public void setEn(ProductInfo en) {
        this.en = en;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public String getProduct_model() {
        return product_model;
    }

    public void setProduct_model(String product_model) {
        this.product_model = product_model;
    }

    public int getCompany_id() {
        return company_id == 0 ? AccountEntity.company_id : company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    @Override
    public String toString() {
        return "JsonFProductInfo [zh=" + zh + ", en=" + en + ", product_desc="
                + product_desc + ", product_model=" + product_model
                + ", logo_path=" + logo_path + ", company_id=" + company_id
                + "]";
    }
}
