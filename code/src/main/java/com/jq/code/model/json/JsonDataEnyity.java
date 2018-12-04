package com.jq.code.model.json;

import com.jq.code.model.PutBase;

import java.util.ArrayList;


public class JsonDataEnyity {

	private String mtype;
	private long role_id;
	private long lastsync;
	private ArrayList<PutBase> mdata = new ArrayList<PutBase>();

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

	public ArrayList<PutBase> getMdata() {
		return mdata;
	}

	public void setMdata(ArrayList<PutBase> mdata) {
		this.mdata = mdata;
	}

	public String getMtype() {
		return mtype;
	}

	public void setMtype(String mtype) {
		this.mtype = mtype;
	}

	@Override
	public String toString() {
		return "JsonDataEnyity{" +
				"mtype='" + mtype + '\'' +
				", role_id=" + role_id +
				", lastsync=" + lastsync +
				", mdata=" + mdata +
				'}';
	}
}
