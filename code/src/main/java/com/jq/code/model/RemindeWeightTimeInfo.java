package com.jq.code.model;

import java.io.Serializable;

public class RemindeWeightTimeInfo implements Serializable {

	/**
	 * 提醒体重称量信息
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private int is_open;
	private int mon_open;
	private int tue_open;
	private int wed_open;
	private int thu_open;
	private int fri_open;
	private int sat_open;
	private int sun_open;
	private int once_open;
	private String remind_time = "00:00";
	private int account_id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIs_open() {
		return is_open;
	}

	public void setIs_open(int is_open) {
		this.is_open = is_open;
	}

	public int getMon_open() {
		return mon_open;
	}

	public void setMon_open(int mon_open) {
		this.mon_open = mon_open;
	}

	public int getTue_open() {
		return tue_open;
	}

	public void setTue_open(int tue_open) {
		this.tue_open = tue_open;
	}

	public int getWed_open() {
		return wed_open;
	}

	public void setWed_open(int wed_open) {
		this.wed_open = wed_open;
	}

	public int getThu_open() {
		return thu_open;
	}

	public void setThu_open(int thu_open) {
		this.thu_open = thu_open;
	}

	public int getFri_open() {
		return fri_open;
	}

	public void setFri_open(int fri_open) {
		this.fri_open = fri_open;
	}

	public int getSat_open() {
		return sat_open;
	}

	public void setSat_open(int sat_open) {
		this.sat_open = sat_open;
	}

	public int getSun_open() {
		return sun_open;
	}

	public void setSun_open(int sun_open) {
		this.sun_open = sun_open;
	}

	public int getOnce_open() {
		return once_open;
	}

	public void setOnce_open(int once_open) {
		this.once_open = once_open;
	}

	public String getRemind_time() {
		return remind_time;
	}

	public void setRemind_time(String remind_time) {
		this.remind_time = remind_time;
	}

	public int getAccount_id() {
		return account_id;
	}

	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}

	@Override
	public String toString() {
		return "RemindeWeightTimeInfo [id=" + id + ", is_open=" + is_open
				+ ", mon_open=" + mon_open + ", tue_open=" + tue_open
				+ ", wed_open=" + wed_open + ", thu_open=" + thu_open
				+ ", fri_open=" + fri_open + ", sat_open=" + sat_open
				+ ", sun_open=" + sun_open + ", once_open=" + once_open
				+ ", remind_time=" + remind_time + ", account_id=" + account_id
				+ "]";
	}
}
