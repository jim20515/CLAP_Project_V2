package com.example.plpa.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {
	public static final String PREF_FILENAME = "projectclap"; // �w���R����]�w�Ѽ�XML�ɦW
//	public static final String PREF_SCRIPT = "projectscript"; // �w���R����]�w�Ѽ�Script����
//	public static final String PREF_MODIFYTIME = "ModifyTime";
//	public static final String PREF_ITEM = "Item";
//	public static final String PREF_POLICY = "Policy";
//	public static final String PREF_DESCRIPTION = "Description";
	public static final String PREF_EXPAPPLY = "pref_expapply";
	
	@SuppressLint("SdCardPath")
	public static final String XML_PATH = "/data/data/com.example.plpa_clap_v2/shared_prefs/" + PREF_FILENAME + ".xml";

	public static final String ID_CHECK = "checkid"; // ����ID�Ѽ�XML�ɦW
	public static final String CLIENT_DEVICE_ID = "DevicesID";
	public static final String AUTH_CODE = "AuthCode";
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
	
	public static boolean setPreference(Context context, String key, int value) {
		SharedPreferences.Editor editor = context.getSharedPreferences(PREF_FILENAME, 
				Context.MODE_PRIVATE).edit();
		
		editor.putInt(key, value);
		editor.commit();
		
		return true;
	}
	
	public static long getLong(Context context, String key) {
		SharedPreferences preferences = context.getSharedPreferences(PREF_FILENAME, 
				Context.MODE_PRIVATE);
		
		return preferences.getLong(key, -1);
				
	}
	
	public static int getInt(Context context, String key) {
		SharedPreferences preferences = context.getSharedPreferences(PREF_FILENAME, 
				Context.MODE_PRIVATE);
		
		return preferences.getInt(key, -1);
				
	}
}
