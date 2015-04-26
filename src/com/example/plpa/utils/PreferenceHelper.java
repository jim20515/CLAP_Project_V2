package com.example.plpa.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {
	public static final String PREF_FILENAME = "projectclap"; // 已分析實驗設定參數XML檔名
	public static final String PREF_SCRIPT = "projectscript"; // 已分析實驗設定參數Script說明
	public static final String XML_PATH = "/data/data/com.example.plpa_clap_v2/shared_prefs/" + PREF_FILENAME + ".xml";

	public static final String ID_CHECK = "checkid"; // 實驗ID參數XML檔名
	public static final String CLIENT_DEVICE_ID = "clientdeviceid";
	public static final String AUTH_CODE = "authcode";
	public static final String UPLOADED_TIME = "upload_time";
	
	public static boolean setPreference(Context context, String key, String value) {
		SharedPreferences.Editor editor = context.getSharedPreferences(PREF_FILENAME, 
				Context.MODE_PRIVATE).edit();
		
		editor.putString(key, value);
		editor.commit();
		
		return true;
	}
	
	public static String getString(Context context, String key) {
		SharedPreferences preferences = context.getSharedPreferences(PREF_FILENAME, 
				Context.MODE_PRIVATE);
		
		return preferences.getString(key, "");
				
	}
	
	public static boolean setPreference(Context context, String key, long value) {
		SharedPreferences.Editor editor = context.getSharedPreferences(PREF_FILENAME, 
				Context.MODE_PRIVATE).edit();
		
		editor.putLong(key, value);
		editor.commit();
		
		return true;
	}
	
	public static long getLong(Context context, String key) {
		SharedPreferences preferences = context.getSharedPreferences(PREF_FILENAME, 
				Context.MODE_PRIVATE);
		
		return preferences.getLong(key, -1);
				
	}
}
