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
import com.example.plpa.utils.SettingString;

public class CallItem extends ExpItemBase {

	private final String mTag = SettingString.TAG;

	private final String IN_COMING_COUNT = "incomingcount";
	private final String OUT_COMING_COUNT = "outcomingcount";
	private final String IN_ALLTIME = "callinalltime";
	private final String OUT_ALLTIME = "calloutalltime";

//	private final String PREF_IN_COMING_COUNT = IN_COMING_COUNT;
//	private final String PREF_OUT_COMING_COUNT = OUT_COMING_COUNT;
//	private final String PREF_IN_ALLTIME = IN_ALLTIME;
//	private final String PREF_OUT_ALLTIME = OUT_ALLTIME;

	public final String ALERT_STRING = "電話資訊";

	public CallItem(Service service) {
		// TODO Auto-generated constructor stub

		super(service);
		mExpPrefix = "Call";
		
//		PREF_IN_COMING_COUNT = mExpPrefix + IN_COMING_COUNT;
//		PREF_OUT_COMING_COUNT = mExpPrefix + OUT_COMING_COUNT;
//		PREF_IN_ALLTIME = mExpPrefix + IN_ALLTIME;
//		PREF_OUT_ALLTIME = mExpPrefix + OUT_ALLTIME;

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
		
		int outComingCount = 0;
		int inComingCount = 0;
		int outAllTime = 0;
		int inAllTime = 0;
		
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
				Log.d(mTag, "Name:" + name + "Incoming:" + number + ", Time:"
						+ time);
				
				inComingCount++;
//				if(mExpRealAttributes.contains(PREF_IN_COMING_COUNT)) 
					insertRecord(context, IN_COMING_COUNT, String.valueOf(inComingCount), time);
				
				inAllTime += duration;
//				if(mExpRealAttributes.contains(PREF_IN_ALLTIME))
					insertRecord(context, IN_ALLTIME, String.valueOf(inAllTime), time);
				
			//out call
			} else if (cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)) == 2) {
				Log.d(mTag, "Name:" + name + "Outcoming:" + number + ", Time:"
						+ time);

				outComingCount++;
//				if(mExpRealAttributes.contains(PREF_OUT_COMING_COUNT))
					insertRecord(context, OUT_COMING_COUNT, String.valueOf(outComingCount), time);
				
				outAllTime += duration;
//				if(mExpRealAttributes.contains(PREF_OUT_ALLTIME))
					insertRecord(context, OUT_ALLTIME, String.valueOf(outAllTime), time);
			}
		}
	}

	@Override
	public boolean needTimeLimit() {
		// TODO Auto-generated method stub
		return false;
	}

}
