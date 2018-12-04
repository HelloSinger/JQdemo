package com.jq.code.model.json;

import java.util.List;

public class JsonRoleDataIdInfo {

	private long role_id;
	private long lastsync;
	private List<List> added;
	private List<List> deleted;

	@Override
	public String toString() {
		return "JsonRoleDataIdInfo{" +
				"role_id=" + role_id +
				", lastsync=" + lastsync +
				", added=" + added +
				", deleted=" + deleted +
				'}';
	}

	public long getRole_id() {
		return role_id;
	}

	public void setRole_id(long role_id) {
		this.role_id = role_id;
	}

	public long getLastsync() {
		return lastsync;
	}

	public void setLastsync(long lastsync) {
		this.lastsync = lastsync;
	}

	public List<List> getAdded() {
		return added;
	}

	public void setAdded(List<List> added) {
		this.added = added;
	}

	public List<List> getDeleted() {
		return deleted;
	}

	public void setDeleted(List<List> deleted) {
		this.deleted = deleted;
	}
}
