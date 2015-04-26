package com.example.pipa.item;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.plpa.utils.PreferenceHelper;
import com.example.plpa.utils.SettingString;

public class CallItem extends ExpItemBase {

	private final String mTag = SettingString.TAG;

	private final String IN_COMING_COUNT = "incomingcount";
	private final String OUT_COMING_COUNT = "outcomingcount";
	private final String IN_ALLTIME = "inalltime";
	private final String OUT_ALLTIME = "outalltime";

//	private String PREF_IN_COMING_COUNT;
//	private String PREF_OUT_COMING_COUNT;
//	private String PREF_IN_ALLTIME;
//	private String PREF_OUT_ALLTIME;

	public final String ALERT_STRING = "電話資訊";

	public CallItem(Service service) {
		// TODO Auto-generated constructor stub
		
		mExpPrefix = "call.";

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
				Log.d("Jim", "Name:" + name + "Incoming:" + number + ", Time:"
						+ time);
				
				inComingCount++;
				if(realAttributesName.contains(IN_COMING_COUNT)) 
					insertRecord(context, IN_COMING_COUNT, String.valueOf(inComingCount));
				
				inAllTime += duration;
				if(realAttributesName.contains(IN_ALLTIME))
					insertRecord(context, IN_ALLTIME, String.valueOf(inAllTime));
				
			//out call
			} else if (cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)) == 2) {
				Log.d("Jim", "Name:" + name + "Outcoming:" + number + ", Time:"
						+ time);

				outComingCount++;
				if(realAttributesName.contains(OUT_COMING_COUNT))
					insertRecord(context, OUT_COMING_COUNT, String.valueOf(outComingCount));
				
				outAllTime += duration;
				if(realAttributesName.contains(OUT_ALLTIME))
					insertRecord(context, OUT_ALLTIME, String.valueOf(outAllTime));
			}
		}
	}

//	@Override
//	public boolean receiveBroadcast(Context context, Intent intent) {
//		// TODO Auto-generated method stub

//		String action = intent.getAction();
//		// if(MainActivity.mIsDebug) Log.d(mTag, action);
//
//		if (action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
//			String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE); // 3
//			String msg = "Phone state changed to " + state;
//
//			// In coming
//			if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) { // 4
//				String incomingNumber = intent
//						.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER); // 5
//				msg += ". Incoming number is " + incomingNumber;
//				if (MainActivity.mIsDebug)
//					Log.d(mTag, msg);
//
//				mInComingCount++;
//				insertRecord(context, IN_COMING_COUNT,
//						String.valueOf(mInComingCount));
//
//				return true;
//			} else if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
//
//				ContentResolver resolver = context.getContentResolver();
//				final Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI,
//						new String[] { CallLog.Calls.NUMBER,
//								CallLog.Calls.CACHED_NAME, CallLog.Calls.TYPE,
//								CallLog.Calls.DATE, CallLog.Calls.DURATION },
//						null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
//
//				for (int i = 0; i < cursor.getCount(); i++) {
//					cursor.moveToPosition(i);
//					int secondindex = cursor.getColumnIndex(CallLog.Calls.DATE);
//					long seconds = cursor.getLong(secondindex);
//					SimpleDateFormat formatter = new SimpleDateFormat(
//							"yy-MM-dd HH:mm:ss");
//					String time = formatter.format(new Date(seconds));
//
//					String name = cursor.getString(cursor
//							.getColumnIndex(CallLog.Calls.CACHED_NAME));
//					String number = cursor.getString(cursor
//							.getColumnIndex(CallLog.Calls.NUMBER));
//
//					if (cursor
//							.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)) == 1) {
//						Log.d("Jim", "Name:" + name + "Incoming:" + number
//								+ ", Time:" + time);
//
//					} else if (cursor.getInt(cursor
//							.getColumnIndex(CallLog.Calls.TYPE)) == 2) {
//						Log.d("Jim", "Name:" + name + "Outcoming:" + number
//								+ ", Time:" + time);
//
//					}
//				}
//
//				return true;
//			}
//
//		} else if (action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
//			String outgoing_number = intent
//					.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
//			String msg = "Phone state changed to out call:";
//			msg += ". outncoming number is " + outgoing_number;
//			if (MainActivity.mIsDebug)
//				Log.d(mTag, msg);
//
//			mOutComingCount++;
//			insertRecord(context, OUT_COMING_COUNT,
//					String.valueOf(mOutComingCount));
//
//			return true;
//		}

//		return false;
//
//	}

//	@Override
//	public IntentFilter getIntentFilter() {
//		// TODO Auto-generated method stub
//
//		IntentFilter filter = new IntentFilter();
//		 for(ExpItemAttribute attr : mExpRealAttributes) {
//		 if(PREF_OUT_COMING_COUNT.equals(attr.mName) ||
//		 PREF_OUT_ALLTIME.equals(attr.mName))
//		 filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
//		 else if (PREF_IN_COMING_COUNT.equals(attr.mName) ||
//		 PREF_IN_ALLTIME.equals(attr.mName))
//		 filter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
		
//		 }

//		return filter;
//	}

}
