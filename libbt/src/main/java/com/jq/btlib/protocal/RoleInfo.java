package com.jq.btlib.protocal;

public class RoleInfo {
	/**
     * @Description 角色Id,平台唯一     
     */
	public int roleId;
	/**
     * @Description 性别     
     */
	public byte sex;
	/**
     * @Description 年龄     
     */
	public byte age;
	/**
     * @Description 身高     
     */
	public byte height;
	/**
     * @Description 体重     
     */
	public short weight;
	
	@Override
	public String toString() {
		return "RoleInfo [roleId=" + roleId + ", sex=" + sex + ", age=" + age
				+ ", height=" + height + ", weight=" + weight + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

}
