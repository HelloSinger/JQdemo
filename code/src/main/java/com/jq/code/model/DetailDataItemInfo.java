package com.jq.code.model;

import java.io.Serializable;

public class DetailDataItemInfo implements Serializable {


	private static final long serialVersionUID = 1L;

	private int imageId;
	private String value;
	private String unit;
	private int bgId;

	public String getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	private String displayValue;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getBgId() {
		return bgId;
	}

	public void setBgId(int bgId) {
		this.bgId = bgId;
	}
}
