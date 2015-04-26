package com.example.pipa.item;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

	public final String ALERT_STRING = "電話資訊";

	public CalActItem(Service service) {
		// TODO Auto-generated constructor stub

		mExpPrefix = "calact.";
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void doSomethingBeforeUpload(Context context) {
		// TODO Auto-generated method stub
		super.doSomethingBeforeUpload(context);
		
		if(mExpRealAttributes.size() == 0)
			return;
		
		List<String> realAttributesName = new ArrayList<String>();
		for (ExpItemAttribute attr : mExpRealAttributes) {
			realAttributesName.add(attr.mName);
		}
		
		long uploadTime = PreferenceHelper.getLong(context, PreferenceHelper.UPLOADED_TIME);
		
		ContentResolver resolver = context.getContentResolver();
		final Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI,
				new String[] { CallLog.Calls.NUMBER, CallLog.Calls.CACHED_NAME,
						CallLog.Calls.TYPE, CallLog.Calls.DATE,
						CallLog.Calls.DURATION }, null, null,
				CallLog.Calls.DEFAULT_SORT_ORDER);

		SimpleDateFormat formatter = new SimpleDateFormat(
				"yy-MM-dd HH:mm:ss");
		
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
				
				if(realAttributesName.contains(RECEIVE_NUMBER))
					insertRecord(context, RECEIVE_NUMBER, 
						cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
				
				receiveTime += duration;
				if(realAttributesName.contains(RECEIVE_TIME))
					insertRecord(context, RECEIVE_TIME, String.valueOf(receiveTime));
				
			//out call
			} else if (cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)) == 2) {
				Log.d("Jim", "Name:" + name + "Outcoming:" + number + ", Time:"
						+ time);

				if(realAttributesName.contains(CALL_NUMBER))
					insertRecord(context, CALL_NUMBER, 
						cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
				
				callTime += duration;
				if(realAttributesName.contains(CALL_TIME))
					insertRecord(context, CALL_TIME, String.valueOf(callTime));
			}
		}
	}

}
