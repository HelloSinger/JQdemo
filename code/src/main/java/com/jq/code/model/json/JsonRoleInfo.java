package com.jq.code.model.json;


public class JsonRoleInfo {

	private String nickname;
	private String height;
	private String birthday;
	private String sex;
	private String weight_goal;
	private String modify_time;
	private String icon_image_path;
	public String getIcon_image_path() {
		return icon_image_path;
	}

	public void setIcon_image_path(String icon_image_path) {
		this.icon_image_path = icon_image_path;
	}

	public String getModify_time() {
		return modify_time;
	}

	public void setModify_time(String modify_time) {
		this.modify_time = modify_time;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getWeight_goal() {
		return weight_goal;
	}

	public void setWeight_goal(String weight_goal) {
		this.weight_goal = weight_goal;
	}

	@Override
	public String toString() {
		return "JsonFRoleUpdateInfo [nickname=" + nickname + ", height="
				+ height + ", birthday=" + birthday + ", sex=" + sex
				+ ", weight_goal=" + weight_goal + ", modify_time="
				+ modify_time + ", icon_image_path=" + icon_image_path + "]";
	}
}
