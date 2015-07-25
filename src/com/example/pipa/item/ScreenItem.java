package com.example.pipa.item;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.example.plpa.utils.SettingString;

public class ScreenItem extends ExpItemBase {

	private final String mTag = SettingString.TAG;

	private final String ATTRI_SCREEN = "screen";
	
	// float preRssi = 0;
	// float rssi;

	public final String ALERT_STRING = "螢幕資訊";

	public ScreenItem(Service service) {
		// TODO Auto-generated constructor stub

		super(service);
		mExpPrefix = "Screen";
		mService = service;

	}

	@Override
	public boolean needTimeLimit() {
		// TODO Auto-generated method stub
		return false;
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

		if (Intent.ACTION_SCREEN_OFF.equals(action) ||
				Intent.ACTION_SCREEN_ON.equals(action)) {

			String statusString = "";
			if (Intent.ACTION_SCREEN_ON.equals(action))
				statusString = "螢幕打開";
			else if (Intent.ACTION_SCREEN_OFF.equals(action))
				statusString = "螢幕關閉";
			
			if (SettingString.mIsDebug)
				Log.d(mTag, "Status:" + statusString);

			insertRecord(context, ATTRI_SCREEN, statusString);

			return true;
		}

		return super.receiveBroadcast(context, intent);
	}

}
