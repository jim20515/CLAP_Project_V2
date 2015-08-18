package com.example.pipa.item;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.example.plpa.utils.SettingString;

public class BTItem extends ExpItemBase {

	private final String mTag = SettingString.TAG;

	private final String ATTRI_BT = "bluetooth";
	private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();
	
	public final String ALERT_STRING = "藍牙偵測";

	public BTItem(Service service) {
		// TODO Auto-generated constructor stub

		super(service);
		mExpPrefix = "Bluetooth";
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

		filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
//		filter.addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED);
//		filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		
		return filter;
	}

	@Override
	public boolean receiveBroadcast(Context context, Intent intent) {
		// TODO Auto-generated method stub

		String action = intent.getAction();

		if (BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED.equals(action) ||
				BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action) ||
				BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action) ||
				BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED.equals(action) ||
				BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action) ||
				BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
			
			String statusString = "";
			if (BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {

				int connectState = intent.getExtras().getInt(BluetoothAdapter.EXTRA_CONNECTION_STATE);
				if (connectState == BluetoothAdapter.STATE_CONNECTED)
					statusString = "連結成功";
				else if (connectState == BluetoothAdapter.STATE_CONNECTING)
					statusString = "正在連結";
				else if (connectState == BluetoothAdapter.STATE_DISCONNECTED)
					statusString = "取消連結";
				
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
				statusString = "藍牙搜尋裝置結束";
			else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
				statusString = "藍牙搜尋裝置開始";
//			else if (BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED.equals(action))
//				statusString = "";
//			else if (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action))
//				statusString = "Eject";
			else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
				if(_bluetooth.isEnabled())
					statusString = "藍牙開啟";
				else
					statusString = "藍牙關閉";
				
			}
				
			
			if (SettingString.mIsDebug)
				Log.d(mTag, "Status:" + statusString);

			insertRecord(context, ATTRI_BT, statusString);

			return true;
		}

		return super.receiveBroadcast(context, intent);
	}

}
