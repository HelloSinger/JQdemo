package com.jq.code.model;

import java.io.Serializable;

/**
 * 设备信息类
 * */
public class ScaleInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String MAC = "MAC_ADDRESS";

	private int id;
	private int account_id;
	private String name;
	private String mac;
	private int type_id = -1;
	private String last_time;
	private int product_id;
	private int version;
	private String procotalType;
	private String fushu;
	private String statue;

	public String getFushu() {
		return fushu;
	}

	public void setFushu(String fushu) {
		this.fushu = fushu;
	}

	public String getStatue() {
		return statue;
	}

	public void setStatue(String statue) {
		this.statue = statue;
	}

	public String getKitchens() {
		return kitchens;
	}

	public void setKitchens(String kitchens) {
		this.kitchens = kitchens;
	}

	private String kitchens;

	public String getProcotalType() {
		return procotalType;
	}

	public void setProcotalType(String procotalType) {
		this.procotalType = procotalType;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAccount_id() {
		return account_id;
	}

	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public int getType_id() {
		return type_id;
	}

	public void setType_id(int type_id) {
		this.type_id = type_id;
	}

	public String getLast_time() {
		return last_time;
	}

	public void setLast_time(String last_time) {
		this.last_time = last_time;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	@Override
	public String toString() {
		return "ScaleInfo [id=" + id + ", account_id=" + account_id + ", name="
				+ name + ", mac=" + mac + ", type_id=" + type_id
				+ ", last_time=" + last_time + ", product_id=" + product_id
				+ ", version=" + version + ", procotalType=" + procotalType
				+ "]";
	}
}
