package com.jq.code.model;

/**
 * 当前参数标准
 * 
 * @author zhxiang
 */
public class ParamStandardInfo {
	String name;
	float value;
	int level; // 当前参数等级，1低 2正常 3高 4太高
	float code;

	@Override
	public String toString() {
		return "ParamStandardInfo [name=" + name + ", value=" + value
				+ ", level=" + level + ", code=" + code + "]";
	}

	public float getCode() {
		return code;
	}

	public void setCode(float code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
