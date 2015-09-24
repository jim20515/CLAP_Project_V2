package com.example.pipa.item;

import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.util.Log;

import com.example.plpa.utils.SettingString;

public class PhotoItem extends ExpItemBase {

	private final String mTag = SettingString.TAG;

	private final String ATTRI_APPNAME = "appafterphoto";

	private final String VAL_FACEBOOK = "facebook";
	private final String VAL_LINE = "line";
	private final String VAL_INSTAGRAM = "instagram";
	private final String VAL_NOTHING = "nothing";
	
	public final String ALERT_STRING = "照相後行為";

	public PhotoItem(Service service) {
		// TODO Auto-generated constructor stub

		super(service);
		mExpPrefix = "Photo";
		mService = service;
	}

	@Override
	public boolean onStart(Context context) {
		// TODO Auto-generated method stub

		new CheckRunningActivity(context).start();

		return super.onStart(context);
	}

	@Override
	public boolean needTimeLimit() {
		// TODO Auto-generated method stub
		return false;
	}

	class CheckRunningActivity extends Thread {
		ActivityManager am = null;
		Context context = null;

		public CheckRunningActivity(Context con) {
			context = con;
			am = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
		}

		public void run() {

			int i = 1;
			while (true) {

				try {
					List<ActivityManager.RunningAppProcessInfo> tasks = am
							.getRunningAppProcesses();
					
					String topAppName = tasks.get(0).processName;

					if (topAppName.contains("jp.naver.line.android")) {
						insertRecord(context, ATTRI_APPNAME, VAL_LINE);
						Log.d(mTag, "Open app :" + topAppName);
						break;
					} else if (topAppName.contains("com.facebook.katana")) {
						insertRecord(context, ATTRI_APPNAME, VAL_FACEBOOK);
						Log.d(mTag, "Open app :" + topAppName);
						break;
					} else if (topAppName.contains("com.instagram.android")) {
						insertRecord(context, ATTRI_APPNAME, VAL_INSTAGRAM);
						Log.d(mTag, "Open app :" + topAppName);
						break;
					}

					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				i++;
				
				if(i == 6) {
					insertRecord(context, ATTRI_APPNAME, VAL_NOTHING);
					Log.d("Jim", "not to open any app of dectection");
					break;
				}
			}

		}
	}

	@Override
	public IntentFilter getIntentFilter() {
		// TODO Auto-generated method stub
		IntentFilter filter = new IntentFilter();

		filter.addAction(android.hardware.Camera.ACTION_NEW_PICTURE);
		try {
			filter.addDataType("image/*");
		} catch (MalformedMimeTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return filter;
	}

	@Override
	public boolean receiveBroadcast(Context context, Intent intent) {
		// TODO Auto-generated method stub

		String action = intent.getAction();
		Log.d(mTag, "" + action);
		if (android.hardware.Camera.ACTION_NEW_PICTURE.equals(action)) {

			new CheckRunningActivity(context).start();

			// because the screenItem would use this action.
			return false;
		}

		return super.receiveBroadcast(context, intent);
	}

}
