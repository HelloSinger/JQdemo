package com.jq.btlib.model.device;

/**
 * 蓝牙必须信息实体类
 * */
public class CsDevice {

	private int product_id;
	private String btName;
	private String btMac;

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public void setBtName(String name) {
		btName = name;
	}

	public void setBtMac(String mac) {
		btMac = mac;
	}

	public String getBtName() {
		return btName;
	}

	public String getBtMac() {
		return btMac;
	}

	@Override
	public String toString() {
		return "CsDevice [product_id=" + product_id + ", btName=" + btName
				+ ", btMac=" + btMac + "]";
	}
}
