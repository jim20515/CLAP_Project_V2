package com.example.plpa.utils;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ExpResultJson {
	private static final String EXPERIMENT_ID_KEY = "ExperimentId";
	private static final String CLIENT_DEVICEDID_KEY = "DeviceId";
	private static final String ITEMS_KEY = "Items";
	
	private static final String ATTR_KEY = "Attr";
	private static final String ATTRVAL_KEY = "AttrVal";
	private static final String TIME_KEY = "DateTime";
	
	public static String transferJsonFormat(int deviceId, int expId, List<DbConstants> constants) {
		
		if(constants != null) {
			
			try {
				JSONObject uploadJson = new JSONObject();
				uploadJson.put(EXPERIMENT_ID_KEY, expId);
				uploadJson.put(CLIENT_DEVICEDID_KEY, deviceId);

				JSONArray logs = new JSONArray();
				
				for(DbConstants constant : constants) {
					JSONObject sensorJson = new JSONObject();
					sensorJson.put(ATTR_KEY, constant.getAttr());
					sensorJson.put(ATTRVAL_KEY, constant.getAttrval());
					sensorJson.put(TIME_KEY, constant.getDateTime());
					
					logs.put(sensorJson);
				}
				
				uploadJson.put(ITEMS_KEY, logs);
				
				return uploadJson.toString();
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} 
		
		return "";
	}
}
