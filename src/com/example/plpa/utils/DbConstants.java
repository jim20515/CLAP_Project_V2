package com.example.plpa.utils;

import android.provider.BaseColumns;

public class DbConstants implements BaseColumns {
	
//	public static final String UUID = "uuid";
	public static final String ITEM = "item";
	public static final String ATTR = "attribute";
	public static final String ATTRVAL = "value";
	public static final String DATETIME = "dateime";
//	public static final String EXID = "exid";
//	public static final String DEVICEID = "clientdeviceid";
	
	private int id;
//	private String uuid;
	private String item;
	private String attr;
	private String attrval;
	private String dateTime;
//	private String exId;
//	private String deviceId;
	
	public DbConstants() {}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
//	public void setUuid(String uuid) {
//		this.uuid = uuid;
//	}
//	
//	public String getUuid() {
//		return this.uuid;
//	}
	
	public void setItem(String item) {
		this.item = item;
	}
	
	public String getItem() {
		return this.item;
	}
	
	public void setAttr(String attr) {
		this.attr = attr;
	}
	
	public String getAttr() {
		return this.attr;
	}
	
	public void setAttrval(String attrval) {
		this.attrval = attrval;
	}
	
	public String getAttrval() {
		return this.attrval;
	}
	
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
	public String getDateTime() {
		return this.dateTime;
	}
	
//	public void setExId(String exId) {
//		this.exId = exId;
//	}
//	
//	public String getExId() {
//		return this.exId;
//	}
//	
//	public void setDeviceId(String deviceId) {
//		this.deviceId = deviceId;
//	}
//	
//	public String getDeviceId() {
//		return this.deviceId;
//	}
	
}
