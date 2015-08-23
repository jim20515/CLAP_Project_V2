package com.example.pipa.item;

import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.plpa.utils.PreferenceHelper;
import com.example.plpa.utils.SettingString;

public class AppItem extends ExpItemBase {

	private final String mTag = SettingString.TAG;
	private final String PREF_CURRENT_APP = "pref_current_app";
	private final String PREF_DETECT_APP = "pref_detect_app";
	
	private final String ATTRI_APPNAME = "appname";

	private final String VAL_YOUTUBE = "youtube";
	private final String VAL_GMAIL = "gmail";
	private final String VAL_FACEBOOK = "facebook";
	private final String VAL_LINE = "line";
	private final String VAL_GOOGLEMAP = "googlemap";
	private final String VAL_CHROME = "chrome";
	private final String VAL_HOME = "home";
	private final String VAL_SCREENOFF = "screenoff";

	public final String ALERT_STRING = "使用App時間";

	public AppItem(Service service) {
		// TODO Auto-generated constructor stub

		super(service);
		mExpPrefix = "App";
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
			Looper.prepare();

			while (true) {

				try {
					List<ActivityManager.RunningAppProcessInfo> tasks = am
							.getRunningAppProcesses();
					String topAppName = tasks.get(0).processName;

					String currentApp = PreferenceHelper.getString(context, PREF_CURRENT_APP);
					int IsDetectApp = PreferenceHelper.getInt(context, PREF_DETECT_APP);
					
					if (IsDetectApp != 0 && !currentApp.equals(topAppName)) {

						Log.d("Jim", "Top App Name:" + topAppName);
						
						if (topAppName.contains("com.android.chrome")) {
							insertRecord(context, ATTRI_APPNAME, VAL_CHROME);
						} else if (topAppName
								.contains("com.google.android.apps.maps")) {
							insertRecord(context, ATTRI_APPNAME, VAL_GOOGLEMAP);
						} else if (topAppName
								.contains("com.google.android.youtube")) {
							insertRecord(context, ATTRI_APPNAME, VAL_YOUTUBE);
						} else if (topAppName.contains("com.google.android.gm")) {
							insertRecord(context, ATTRI_APPNAME, VAL_GMAIL);
						} else if (topAppName.contains("jp.naver.line.android")) {
							insertRecord(context, ATTRI_APPNAME, VAL_LINE);
						} else if (topAppName.contains("com.facebook.katana")) {
							insertRecord(context, ATTRI_APPNAME, VAL_FACEBOOK);
						} else if (topAppName.contains(".home")) {
							insertRecord(context, ATTRI_APPNAME, VAL_HOME);
						}

						PreferenceHelper.setPreference(context, PREF_CURRENT_APP, topAppName);
					}
					
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	@Override
	public IntentFilter getIntentFilter() {
		// TODO Auto-generated method stub
		IntentFilter filter = new IntentFilter();

		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);

		return filter;
	}

	@Override
	public boolean receiveBroadcast(Context context, Intent intent) {
		// TODO Auto-generated method stub

		String action = intent.getAction();

		if (Intent.ACTION_SCREEN_OFF.equals(action)) {

			if (SettingString.mIsDebug)
				Log.d(mTag, VAL_SCREENOFF);

			insertRecord(context, ATTRI_APPNAME, VAL_SCREENOFF);

			PreferenceHelper.setPreference(context, PREF_CURRENT_APP, VAL_SCREENOFF);
			PreferenceHelper.setPreference(context, PREF_DETECT_APP, 0);
			
			// because the screenItem would use this action.
			return false;
		} else if(Intent.ACTION_SCREEN_ON.equals(action)) {
			PreferenceHelper.setPreference(context, PREF_DETECT_APP, 1);
			
			return false;
		}

		return super.receiveBroadcast(context, intent);
	}

}
