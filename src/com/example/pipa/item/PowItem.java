package com.example.pipa.item;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

import com.example.plpa.utils.SettingString;

public class PowItem extends ExpItemBase {

	private final String mTag = SettingString.TAG;

	private final String ATTRI_POWACT = "powact";
	private final String ATTRI_POWVAL = "powval";
	
	// float preRssi = 0;
	// float rssi;

	public final String ALERT_STRING = "電源資訊";

	public PowItem(Service service) {
		// TODO Auto-generated constructor stub

		super(service);
		mExpPrefix = "Pow";
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

		filter.addAction(Intent.ACTION_POWER_CONNECTED);
		filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
		filter.addAction(Intent.ACTION_BATTERY_LOW);
		filter.addAction(Intent.ACTION_BATTERY_OKAY);
		
		return filter;
	}

	@Override
	public boolean receiveBroadcast(Context context, Intent intent) {
		// TODO Auto-generated method stub

		String action = intent.getAction();

		if (Intent.ACTION_POWER_CONNECTED.equals(action) ||
				Intent.ACTION_POWER_DISCONNECTED.equals(action) ||
				Intent.ACTION_BATTERY_LOW.equals(action) ||
				Intent.ACTION_BATTERY_OKAY.equals(action)) {

			String statusString = "";

			if (Intent.ACTION_POWER_CONNECTED.equals(action))
				statusString = "插電中";
			else if (Intent.ACTION_POWER_DISCONNECTED.equals(action))
				statusString = "未插電";
			else if (Intent.ACTION_BATTERY_LOW.equals(action))
				statusString = "低電量";
			else if (Intent.ACTION_BATTERY_OKAY.equals(action))
				statusString = "從低電量恢復";
			
			float batteryLevel = getBatteryLevel();
			
			if (SettingString.mIsDebug)
				Log.d(mTag, "Status:" + statusString + " power level:" + batteryLevel);
			
			List<RecordPair> pairList = new ArrayList<RecordPair>();
			RecordPair actPair = new RecordPair();

			actPair.key = ATTRI_POWACT;
			actPair.value = statusString;
			pairList.add(actPair);

			RecordPair valPair = new RecordPair();
			valPair.key = ATTRI_POWVAL;
			valPair.value = String.valueOf(batteryLevel);
			pairList.add(valPair);

			insertRecord(context, pairList);

			return true;
		}

		return super.receiveBroadcast(context, intent);
	}
	
	public float getBatteryLevel() {
	    Intent batteryIntent = mService.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	    int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
	    int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

	    // Error checking that probably isn't needed but I added just in case.
	    if(level == -1 || scale == -1) {
	        return 50.0f;
	    }

	    return ((float)level / (float)scale) * 100.0f; 
	}
}
