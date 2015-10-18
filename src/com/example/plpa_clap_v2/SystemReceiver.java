package com.example.plpa_clap_v2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.plpa.utils.SettingString;

public class SystemReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String actionString = intent.getAction();

		Toast.makeText(context, actionString, Toast.LENGTH_LONG).show();
		
		if (SettingString.mIsDebug)
			Log.d(SettingString.TAG, "Activity onReceive, Get Action:" + actionString);

		boolean isReceived = false;
		
		if (Intent.ACTION_BATTERY_LOW.equals(actionString)) {
			Intent serviceIntent = new Intent(context, LogService.class);
			context.stopService(serviceIntent);
			
			isReceived = true;
		}else if (Intent.ACTION_BATTERY_OKAY.equals(actionString) ||
				Intent.ACTION_BOOT_COMPLETED.equals(actionString)) {
			Intent serviceIntent = new Intent(context, LogService.class);
			context.startService(serviceIntent);
			
			isReceived = true;
		}
		
		if (!isReceived)
			Log.d(SettingString.TAG, "No one received this broadcast:" + actionString);
	}

}
