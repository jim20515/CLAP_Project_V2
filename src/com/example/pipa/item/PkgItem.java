package com.example.pipa.item;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.example.plpa.utils.SettingString;

public class PkgItem extends ExpItemBase {

	private final String mTag = SettingString.TAG;

	private final String ATTRI_NAME = "pkgname";
	private final String ATTRI_ACTION = "apkact";
	
	// float preRssi = 0;
	// float rssi;

	public final String ALERT_STRING = "應用程式資訊";

	public PkgItem(Service service) {
		// TODO Auto-generated constructor stub

		super(service);
		mExpPrefix = "Pkg";
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

		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addAction(Intent.ACTION_PACKAGE_DATA_CLEARED);
		filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
		filter.addDataScheme("package");
		
		return filter;
	}

	@Override
	public boolean receiveBroadcast(Context context, Intent intent) {
		// TODO Auto-generated method stub

		String action = intent.getAction();

		if (Intent.ACTION_PACKAGE_ADDED.equals(action) ||
				Intent.ACTION_PACKAGE_REMOVED.equals(action) ||
				Intent.ACTION_PACKAGE_DATA_CLEARED.equals(action) ||
				Intent.ACTION_PACKAGE_REPLACED.equals(action)) {

			String statusString = "";
			if (Intent.ACTION_PACKAGE_ADDED.equals(action))
				statusString = "App安裝";
			else if (Intent.ACTION_PACKAGE_REMOVED.equals(action))
				statusString = "App刪除";
			else if (Intent.ACTION_PACKAGE_DATA_CLEARED.equals(action))
				statusString = "App資料清除";
			else if (Intent.ACTION_PACKAGE_REPLACED.equals(action))
				statusString = "App更新";
			
			Integer uid = intent.getIntExtra(Intent.EXTRA_UID, Integer.MIN_VALUE);
			String packageName = context.getPackageManager().getNameForUid(uid);
			
			if (SettingString.mIsDebug)
				Log.d(mTag, "packageName:" + packageName + " Status:" + statusString);

			List<RecordPair> list = new ArrayList<RecordPair>();
        	
			RecordPair pair = new RecordPair();
            pair.key = ATTRI_NAME;
            pair.value = packageName;
            list.add(pair);
            
            pair = new RecordPairWithTime();
            pair.key = ATTRI_ACTION;
            pair.value = statusString;
            list.add(pair);
            
            insertRecord(context, list);

			return true;
		}

		return super.receiveBroadcast(context, intent);
	}

}
