package com.jq.code.model.json;

import com.jq.code.model.RoleInfo;

import java.util.ArrayList;

public class JsonRolesInfo {

	private ArrayList<RoleInfo> roles;


	public ArrayList<RoleInfo> getRoles() {
		return roles;
	}

	public void setRoles(ArrayList<RoleInfo> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "JsonRolesInfo{" +
				"roles=" + roles +
				'}';
	}
}
