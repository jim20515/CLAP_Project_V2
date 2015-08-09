package com.example.pipa.item;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.util.Log;

import com.example.plpa.utils.SettingString;

public class RingerItem extends ExpItemBase {

	private final String mTag = SettingString.TAG;

	private final String ATTRI_RINGER = "ringer";

	public final String ALERT_STRING = "鈴聲資訊";

	public RingerItem(Service service) {
		// TODO Auto-generated constructor stub

		super(service);
		mExpPrefix = "Ringer";
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

		filter.addAction(AudioManager.RINGER_MODE_CHANGED_ACTION);

		return filter;
	}

	@Override
	public boolean receiveBroadcast(Context context, Intent intent) {
		// TODO Auto-generated method stub

		String action = intent.getAction();

		if (AudioManager.RINGER_MODE_CHANGED_ACTION.equals(action)) {

			AudioManager am = (AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE);
			int currentRingerMode = am.getRingerMode();

			String statusString = "";
			switch (currentRingerMode) {
			case AudioManager.RINGER_MODE_NORMAL:
				statusString = "鈴聲";
				break;
			case AudioManager.RINGER_MODE_SILENT:
				statusString = "靜音";
				break;
			case AudioManager.RINGER_MODE_VIBRATE:
				statusString = "震動";
				break;

			default:
				break;
			}

			if (SettingString.mIsDebug)
				Log.d(mTag, "Status:" + statusString);

			insertRecord(context, ATTRI_RINGER, statusString);

			return true;
		}

		return super.receiveBroadcast(context, intent);
	}

}
