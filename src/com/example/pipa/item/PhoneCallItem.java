package com.example.pipa.item;

import com.example.plpa.utils.SettingString;
import com.example.plpa_clap_v2.MainActivity;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;

public class PhoneCallItem extends ExpItemBase {
	private Service mService;
	private final String mTag = MainActivity.mTag;
	
	private final String IN_COMING_COUNT = "call.incomingcount";
	private final String OUT_COMING_COUNT = "call.outcomingcount";
	private final String IN_ALLTIME = "call.inalltime";
	private final String OUT_ALLTIME = "call.outalltime";

	public int IN_COMING_COUNT_INTERVAL;
	public int OUT_COMING_COUNT_INTERVAL;
	public int IN_ALLTIME_INTERVAL;
	public int OUT_ALLTIME_INTERVAL;
	
	public final String ALERT_STRING = "電話資訊";
	
	public PhoneCallItem() {}
	public PhoneCallItem(Service service) {
		// TODO Auto-generated constructor stub

		mService = service;
		mExpPrefix = "call.";
	}
	
	
	// 電話撥打參數初始化
	private void countCallTimes() {
		int inComingCount = 0;
		int outComingCount = 0;
		int inAlltime = 0;
		int outAlltime = 0;
//		String callNumber = "";
		
		// MD5("0919336386");
		ContentResolver cr = mService.getContentResolver();
		
		final Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI,
				new String[] { CallLog.Calls.NUMBER, CallLog.Calls.CACHED_NAME,
						CallLog.Calls.TYPE, CallLog.Calls.DATE,
						CallLog.Calls.DURATION }, null, null,
				CallLog.Calls.DEFAULT_SORT_ORDER);
		
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			if (cursor.getInt(2) == 1) {
				//in Coming
				inComingCount += 1;
//				callNumber = String.valueOf(cursor.getInt(0));
				inAlltime = inAlltime + cursor.getInt(4);
				
			}else if (cursor.getInt(2) == 2) {
				//out coming
				outComingCount += 1;
//				callNumber = String.valueOf(cursor.getInt(0));
				outAlltime = outAlltime + cursor.getInt(4);
				
			}

		}

		Log.v(mTag, String.format("PLPA_CLAP: in coming count => %s \n " +
				"out coming count => %s \n " +
				"in all time => %s \n" + 
				"out all time => %s ", inComingCount, outComingCount, inAlltime, outAlltime));
		
		SharedPreferences settings = mService.getSharedPreferences(SettingString.PREF_FILENAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(IN_COMING_COUNT, inComingCount);
		editor.putInt(OUT_COMING_COUNT, outComingCount);
		editor.putInt(IN_ALLTIME, inAlltime);
		editor.putInt(OUT_ALLTIME, outAlltime);
//		editor.putInt(MSG_TIMES, msgtimes);
		editor.commit();
	}

	@Override
	public void initAttributeKey() {
		// TODO Auto-generated method stub

		mExpJudgeStrings = new String[]{IN_COMING_COUNT, OUT_COMING_COUNT, 
		                            IN_ALLTIME, OUT_ALLTIME};
	}
}
