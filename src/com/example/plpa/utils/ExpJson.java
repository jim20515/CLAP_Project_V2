package com.example.plpa.utils;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;

public class ExpJson {
	private String mClientDeviceId;
	private String mExId;
	
	private final String SENSOR_KEY = "sensor";
	private final String TIME_KEY = "time";
	private final String LOGS_KEY = "logs";
	
	private final String EXPERIMENT_ID_KEY = "experimentid";
	private final String CLIENT_DEVICEDID_KEY = "clientdeviceid";
	
	public ExpJson(){}
	public ExpJson(Context context) {
		
		mClientDeviceId = PreferenceHelper.getString(context, PreferenceHelper.CLIENT_DEVICE_ID);
		mExId = PreferenceHelper.getString(context, PreferenceHelper.ID_CHECK);
		
	}
	
	public String transferJsonFormat(List<DbConstants> constants) {
		
		if(constants != null) {
			
			try {
				JSONObject uploadJson = new JSONObject();
				uploadJson.put(EXPERIMENT_ID_KEY, mExId);
				uploadJson.put(CLIENT_DEVICEDID_KEY, mClientDeviceId);

				JSONArray logs = new JSONArray();
				
				for(DbConstants constant : constants) {
					JSONObject sensorJson = new JSONObject();
					sensorJson.put(constant.getAttr(), constant.getAttrval());
					
					JSONObject record = new JSONObject();
					record.put(SENSOR_KEY, sensorJson);
					record.put(TIME_KEY, constant.getItem());
					
					logs.put(record);
				}
				
				uploadJson.put(LOGS_KEY, logs);
				
				return uploadJson.toString();
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} 
		
		return "";
	}
}
