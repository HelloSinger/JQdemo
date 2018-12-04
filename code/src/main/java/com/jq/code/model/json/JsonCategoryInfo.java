package com.jq.code.model.json;

import com.jq.code.model.CategoryInfo;

import java.util.ArrayList;

public class JsonCategoryInfo {

	private ArrayList<CategoryInfo> cate;


	public ArrayList<CategoryInfo> getCate() {
		return cate;
	}

	public void setCate(ArrayList<CategoryInfo> cate) {
		this.cate = cate;
	}

	@Override
	public String toString() {
		return "JsonCategoryInfo{" +
				"cate=" + cate +
				'}';
	}
}
