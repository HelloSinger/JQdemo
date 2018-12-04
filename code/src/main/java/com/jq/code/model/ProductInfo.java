package com.jq.code.model;

import java.io.Serializable;

/**
 * 设备信息类
 * */
public class ProductInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int count = 3;
	private String address = "深圳市南山区高新南一道009号中国科技开发院中科研发园3号楼塔楼21层B4室";
	private String phone = "4006 999 999";
	private String name = "海尔信息科技（深圳）有限公司";
	private String desc = "";
	private String contact = "";

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public int getCount() {
		return count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "ProductInfo [address=" + address + ", phone=" + phone
				+ ", name=" + name + ", desc=" + desc + ", contact=" + contact
				+ "]";
	}
}
