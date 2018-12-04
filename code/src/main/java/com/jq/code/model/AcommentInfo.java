package com.jq.code.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AcommentInfo implements Parcelable {

	private int id;
	private int bid;
	private int account_id;
	private int role_id;
	private String commenter_nickname;

	private String commenter_icon;
	private long ts;
	private String content;

	@Override
	public String toString() {
		return "AcommentInfo{" +
				"id=" + id +
				", bid=" + bid +
				", account_id=" + account_id +
				", role_id=" + role_id +
				", commenter_nickname='" + commenter_nickname + '\'' +
				", commenter_icon='" + commenter_icon + '\'' +
				", ts=" + ts +
				", content='" + content + '\'' +
				'}';
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBid() {
		return bid;
	}

	public void setBid(int bid) {
		this.bid = bid;
	}

	public int getAccount_id() {
		return account_id;
	}

	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}

	public int getRole_id() {
		return role_id;
	}

	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}

	public String getCommenter_nickname() {
		return commenter_nickname;
	}

	public void setCommenter_nickname(String commenter_nickname) {
		this.commenter_nickname = commenter_nickname;
	}

	public String getCommenter_icon() {
		return commenter_icon;
	}

	public void setCommenter_icon(String commenter_icon) {
		this.commenter_icon = commenter_icon;
	}

	public long getTs() {
		return ts;
	}

	public void setTs(long ts) {
		this.ts = ts;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeInt(this.bid);
		dest.writeInt(this.account_id);
		dest.writeInt(this.role_id);
		dest.writeString(this.commenter_nickname);
		dest.writeString(this.commenter_icon);
		dest.writeLong(this.ts);
		dest.writeString(this.content);
	}

	public AcommentInfo() {
	}

	protected AcommentInfo(Parcel in) {
		this.id = in.readInt();
		this.bid = in.readInt();
		this.account_id = in.readInt();
		this.role_id = in.readInt();
		this.commenter_nickname = in.readString();
		this.commenter_icon = in.readString();
		this.ts = in.readLong();
		this.content = in.readString();
	}

	public static final Creator<AcommentInfo> CREATOR = new Creator<AcommentInfo>() {
		@Override
		public AcommentInfo createFromParcel(Parcel source) {
			return new AcommentInfo(source);
		}

		@Override
		public AcommentInfo[] newArray(int size) {
			return new AcommentInfo[size];
		}
	};
}
