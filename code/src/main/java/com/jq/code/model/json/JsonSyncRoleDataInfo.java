package com.jq.code.model.json;

import com.jq.code.model.WeightEntity;

import java.util.List;


public class JsonSyncRoleDataInfo {

	public static final String KEY = "syncAndCalculate";
	public static final String ACTION = "m=Profile&a=" + KEY;

	private int account_id;
	private int role_id;
	private String sync_time;

	private List<WeightEntity> role_data;

	public List<WeightEntity> getRole_data() {
		return role_data;
	}

	public void setRole_data(List<WeightEntity> role_data) {
		this.role_data = role_data;
	}

	public int getAccount_id() {
		return account_id;
	}

	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}

	public int getRole_id() {
		return role_id;
	}

	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}

	public String getSync_time() {
		return sync_time;
	}

	public void setSync_time(String sync_time) {
		this.sync_time = sync_time;
	}

	@Override
	public String toString() {
		return "JsonSaveWeighInfo [account_id=" + account_id + ", role_id="
				+ role_id + ", sync_time=" + sync_time + ", role_data="
				+ role_data + "]";
	}

}
