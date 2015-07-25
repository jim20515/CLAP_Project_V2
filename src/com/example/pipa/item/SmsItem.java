package com.example.pipa.item;

import android.app.Service;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.plpa.utils.PreferenceHelper;
import com.example.plpa.utils.SettingString;

public class SmsItem extends ExpItemBase {

	private final String mTag = SettingString.TAG;

	private final String ATTRI_TIMES = "times";

	public final String ALERT_STRING = "簡訊資訊";

	public SmsItem(Service service) {
		// TODO Auto-generated constructor stub

		super(service);
		mExpPrefix = "Sms";
		mService = service;

	}
	
	@Override
	public boolean needTimeLimit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void doSomethingBeforeUpload(Context context) {
		// TODO Auto-generated method stub
		super.doSomethingBeforeUpload(context);

		long uploadTime = PreferenceHelper.getLong(context,
				PreferenceHelper.UPLOADED_TIME);

		// column names for above provider:
//		0: _id
//		1: thread_id
//		2: address
//		3: person
//		4: date
//		5: protocol
//		6: read   
//		7: status
//		8: type
//		9: reply_path_present
//		10: subject
//		11: body
//		12: service_center
//		13: locked
		
		// public static final String INBOX = "content://sms/inbox";
		// public static final String SENT = "content://sms/sent";
		// public static final String DRAFT = "content://sms/draft";
		Cursor cursor = context.getContentResolver().query(
				Uri.parse("content://sms/inbox"), null, null, null, null);

		int i = 0;
		if (cursor.moveToFirst()) { // must check the result to prevent
									// exception
			do {
				int secondindex = cursor
						.getColumnIndex("date");
				long seconds = cursor.getLong(secondindex);

				if(SettingString.mIsDebug)
					Log.d(mTag, "now:" + uploadTime + " record:" + seconds);
				
				if (seconds < uploadTime)
					break;

				i++;
//				String msgData = "";
//				for (int idx = 0; idx < cursor.getColumnCount(); idx++) {
//					msgData += " " + cursor.getColumnName(idx) + ":"
//							+ cursor.getString(idx);
//
//				}
				
//				if(SettingString.mIsDebug)
//					Log.d(mTag, msgData);
				
				// use msgData
			} while (cursor.moveToNext());
		} else {
			// empty box, no SMS
		}

		if(i > 0) {
			insertRecord(context, ATTRI_TIMES, i);
			
			if(SettingString.mIsDebug)
				Log.d(mTag, "簡訊計數:" + i + "次");
		}
		
		cursor.close();
	}

}
