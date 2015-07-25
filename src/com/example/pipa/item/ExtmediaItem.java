package com.example.pipa.item;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.example.plpa.utils.SettingString;

public class ExtmediaItem extends ExpItemBase {

	private final String mTag = SettingString.TAG;

	private final String ATTRI_EXTMEDIA = "extmedia";
	
	// float preRssi = 0;
	// float rssi;

	public final String ALERT_STRING = "媒體資訊";

	public ExtmediaItem(Service service) {
		// TODO Auto-generated constructor stub

		super(service);
		mExpPrefix = "Extmedia";
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

		filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		filter.addAction(Intent.ACTION_MEDIA_REMOVED);
		filter.addAction(Intent.ACTION_MEDIA_UNMOUNTABLE);
		filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		filter.addAction(Intent.ACTION_MEDIA_EJECT);
		filter.addDataScheme("file");
		
		return filter;
	}

	@Override
	public boolean receiveBroadcast(Context context, Intent intent) {
		// TODO Auto-generated method stub

		String action = intent.getAction();

		if (Intent.ACTION_MEDIA_MOUNTED.equals(action) ||
				Intent.ACTION_MEDIA_REMOVED.equals(action) ||
				Intent.ACTION_MEDIA_UNMOUNTABLE.equals(action) ||
				Intent.ACTION_MEDIA_UNMOUNTED.equals(action) ||
				Intent.ACTION_MEDIA_EJECT.equals(action)) {

			String statusString = "";
			if (Intent.ACTION_MEDIA_MOUNTED.equals(action))
				statusString = "Mounted";
			else if (Intent.ACTION_MEDIA_REMOVED.equals(action))
				statusString = "Removed";
			else if (Intent.ACTION_MEDIA_UNMOUNTABLE.equals(action))
				statusString = "Unmountable";
			else if (Intent.ACTION_MEDIA_UNMOUNTED.equals(action))
				statusString = "Unmounted";
			else if (Intent.ACTION_MEDIA_EJECT.equals(action))
				statusString = "Eject";
			
			if (SettingString.mIsDebug)
				Log.d(mTag, "Status:" + statusString);

			insertRecord(context, ATTRI_EXTMEDIA, statusString);

			return true;
		}

		return super.receiveBroadcast(context, intent);
	}

}
