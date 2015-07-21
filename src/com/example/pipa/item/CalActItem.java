package com.example.pipa.item;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;

import com.example.plpa.utils.PreferenceHelper;

public class CalActItem extends ExpItemBase {

//	private final String mTag = SettingString.TAG;

	private final String CALL_NUMBER = "callnumber";
	private final String CALL_TIME = "calltime";
	private final String RECEIVE_NUMBER = "receivenumber";
	private final String RECEIVE_TIME = "receivetime";

//	private final String PREF_CALL_NUMBER = CALL_NUMBER;
//	private final String PREF_CALL_TIME = CALL_TIME;
//	private final String PREF_RECEIVE_NUMBER = RECEIVE_NUMBER;
//	private final String PREF_RECEIVE_TIME = RECEIVE_TIME;
	
	public final String ALERT_STRING = "通話資訊";

	public CalActItem(Service service) {
		// TODO Auto-generated constructor stub

		super(service);
		mExpPrefix = "CalAct";
		
//		PREF_CALL_NUMBER = mExpPrefix + CALL_NUMBER;
//		PREF_CALL_TIME = mExpPrefix + CALL_TIME;
//		PREF_RECEIVE_NUMBER = mExpPrefix + RECEIVE_NUMBER;
//		PREF_RECEIVE_TIME = mExpPrefix + RECEIVE_TIME;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void doSomethingBeforeUpload(Context context) {
		// TODO Auto-generated method stub
		super.doSomethingBeforeUpload(context);
		
//		if(mExpRealAttributes.size() == 0)
//			return;
		
		long uploadTime = PreferenceHelper.getLong(context, PreferenceHelper.UPLOADED_TIME);
		
		ContentResolver resolver = context.getContentResolver();
		final Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI,
				new String[] { CallLog.Calls.NUMBER, CallLog.Calls.CACHED_NAME,
						CallLog.Calls.TYPE, CallLog.Calls.DATE,
						CallLog.Calls.DURATION }, null, null,
				CallLog.Calls.DEFAULT_SORT_ORDER);

		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		
		int callTime = 0;
		int receiveTime = 0;
		
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			int secondindex = cursor.getColumnIndex(CallLog.Calls.DATE);
			long seconds = cursor.getLong(secondindex);
			
			if(seconds < uploadTime)
				break;
			
			String time = formatter.format(new Date(seconds));
			String name = cursor.getString(cursor
					.getColumnIndex(CallLog.Calls.CACHED_NAME));
			String number = cursor.getString(cursor
					.getColumnIndex(CallLog.Calls.NUMBER));
			int duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));

			//in call
			if (cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)) == 1) {
				Log.d("Jim", "Name:" + name + "Incoming:" + number + ", Time:"
						+ time);
				
//				if(mExpRealAttributes.contains(PREF_RECEIVE_NUMBER))
					insertRecord(context, RECEIVE_NUMBER, 
						cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)), time);
				
				receiveTime += duration;
//				if(mExpRealAttributes.contains(PREF_RECEIVE_TIME))
					insertRecord(context, RECEIVE_TIME, String.valueOf(receiveTime), time);
				
			//out call
			} else if (cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)) == 2) {
				Log.d("Jim", "Name:" + name + "Outcoming:" + number + ", Time:"
						+ time);

//				if(mExpRealAttributes.contains(PREF_CALL_NUMBER))
					insertRecord(context, CALL_NUMBER, 
						cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)), time);
				
				callTime += duration;
//				if(mExpRealAttributes.contains(PREF_CALL_TIME))
					insertRecord(context, CALL_TIME, String.valueOf(callTime), time);
			}
		}
	}

	@Override
	public boolean needTimeLimit() {
		// TODO Auto-generated method stub
		return false;
	}

}
