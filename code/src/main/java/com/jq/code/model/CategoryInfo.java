package com.jq.code.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryInfo implements Parcelable {

	private String bgcolor;
	private String title_en;
	private String title;
	private int id;

	public String getBgcolor() {
		return bgcolor;
	}

	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}

	public String getTitle_en() {
		return title_en;
	}

	public void setTitle_en(String title_en) {
		this.title_en = title_en;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "JsonCategoryInfo{" +
				"bgcolor=" + bgcolor +
				", title_en='" + title_en + '\'' +
				", title='" + title + '\'' +
				", id=" + id +
				'}';
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.bgcolor);
		dest.writeString(this.title_en);
		dest.writeString(this.title);
		dest.writeInt(this.id);
	}

	public CategoryInfo() {
	}

	protected CategoryInfo(Parcel in) {
		this.bgcolor = in.readString();
		this.title_en = in.readString();
		this.title = in.readString();
		this.id = in.readInt();
	}

	public static final Creator<CategoryInfo> CREATOR = new Creator<CategoryInfo>() {
		@Override
		public CategoryInfo createFromParcel(Parcel source) {
			return new CategoryInfo(source);
		}

		@Override
		public CategoryInfo[] newArray(int size) {
			return new CategoryInfo[size];
		}
	};
}
