package com.jq.code.model.json;

public class JsonCommonInfo {

	private int code;
	private String msg;
	private Object data;
	private String size ;

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "JsonCommonInfo [code=" + code + ", data=" + data + ", msg="
				+ msg + "]";
	}
}
