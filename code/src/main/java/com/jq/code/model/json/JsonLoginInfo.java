package com.jq.code.model.json;

import com.jq.code.model.AccountEntity;
import com.jq.code.model.RemindeWeightTimeInfo;
import com.jq.code.model.RoleInfo;

import java.util.ArrayList;

public class JsonLoginInfo {

	private AccountEntity account;
	private ArrayList<RoleInfo> role;
	private ArrayList<RemindeWeightTimeInfo> remind;

	public AccountEntity getAccount() {
		return account;
	}

	public void setAccount(AccountEntity account) {
		this.account = account;
	}

	public ArrayList<RoleInfo> getRole() {
		return role;
	}

	public void setRole(ArrayList<RoleInfo> role) {
		this.role = role;
	}

	public ArrayList<RemindeWeightTimeInfo> getRemind() {
		return remind;
	}

	public void setRemind(ArrayList<RemindeWeightTimeInfo> remind) {
		this.remind = remind;
	}

	@Override
	public String toString() {
		return "JsonFLoginInfo [account=" + account + ", role=" + role
				+ ", remind=" + remind + "]";
	}
}
