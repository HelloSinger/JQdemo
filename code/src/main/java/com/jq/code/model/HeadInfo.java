package com.jq.code.model;

import java.io.Serializable;

public class HeadInfo implements Serializable {

	/**
	 * https请求头信息
	 */
	private static final long serialVersionUID = 1L;

	private String token;
	private String token_expirytime;
	private String device_id;
	private String user_agent;
	private String cs_scale;

	public String getCs_scale() {
		return cs_scale;
	}

	public void setCs_scale(String cs_scale) {
		this.cs_scale = cs_scale;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getToken_expirytime() {
		return token_expirytime;
	}

	public void setToken_expirytime(String token_expirytime) {
		this.token_expirytime = token_expirytime;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getUser_agent() {
		return user_agent;
	}

	public void setUser_agent(String user_agent) {
		this.user_agent = user_agent;
	}

	@Override
	public String toString() {
		return "HeadInfo [token=" + token
				+ ", token_expirytime=" + token_expirytime + ", device_id="
				+ device_id + ", user_agent=" + user_agent + ", cs_scale="+cs_scale+"]";
	}
}
