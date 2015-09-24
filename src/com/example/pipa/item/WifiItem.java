package com.example.pipa.item;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.example.plpa.utils.SettingString;

public class WifiItem extends ExpItemBase {

	private final String mTag = SettingString.TAG;

	private final String SSID = "ssid";
	private final String RSSI = "rssi";

//	private final String PREF_SSID = SSID;
//	private final String PREF_RSSI = RSSI;

	public final String ALERT_STRING = "WiFI資訊";

	public WifiItem(Service service) {
		// TODO Auto-generated constructor stub

		super(service);
		mExpPrefix = "Wifi";

//		PREF_SSID = mExpPrefix + SSID;
//		PREF_RSSI = mExpPrefix + RSSI;
	}

	@Override
	public boolean onStart(Context context) {
		// TODO Auto-generated method stub

		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		
		if (!wifiManager.isWifiEnabled()) {
			Toast.makeText(context, "請開啟Wifi服務\n開啟後請重新啟動實驗", Toast.LENGTH_LONG)
					.show();
			return false;
		}
		
		if (SettingString.mIsDebug)
			Log.d(mTag, "Wifi state:" + wifiManager.getWifiState());
		
		return super.onStart(context);
	}

	@Override
	public IntentFilter getIntentFilter() {
		// TODO Auto-generated method stub
		IntentFilter filter = new IntentFilter();

//		if (mExpRealAttributes.contains(PREF_SSID)) {
//			filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//			filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//		}
		
//		if (mExpRealAttributes.contains(PREF_RSSI))
			filter.addAction(WifiManager.RSSI_CHANGED_ACTION);

		return filter;
	}

	@Override
	public boolean receiveBroadcast(Context context, Intent intent) {
		// TODO Auto-generated method stub

		String action = intent.getAction();

//		if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
//			NetworkInfo networkInfo = intent
//					.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
//			if (networkInfo.isConnected()) {
//				// Wifi is connected
//
//				WifiManager wifiManager = (WifiManager) context
//						.getSystemService(context.WIFI_SERVICE);
//				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//				String ssid = wifiInfo.getSSID();
//
//				if (SettingString.mIsDebug)
//					Log.d(mTag, "Wifi is connected: " + ssid);
//				insertRecord(context, SSID, ssid);
//			}
//
//			return true;

//		} else if (WifiManager.RSSI_CHANGED_ACTION.equals(action)) {

		if (WifiManager.RSSI_CHANGED_ACTION.equals(action)) {
			
			String ssid = "none";
		    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		    if (WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState()) == NetworkInfo.DetailedState.CONNECTED) {
		        ssid = wifiInfo.getSSID();
		    }
		    
			int newRssi = intent.getIntExtra(WifiManager.EXTRA_NEW_RSSI, 0);

			if (SettingString.mIsDebug)
				Log.d(mTag, "Wifi RSSI Change, ssid:" + ssid + " rssi:" + newRssi);
			
			List<ExpItemBase.RecordPair> pairList = new ArrayList<ExpItemBase.RecordPair>();
			ExpItemBase.RecordPair ssidPair = new ExpItemBase.RecordPair();
			
			ssidPair.key = SSID;
			ssidPair.value = ssid;
			pairList.add(ssidPair);
			
			ExpItemBase.RecordPair rssiPair = new ExpItemBase.RecordPair();
			rssiPair.key = RSSI;
			rssiPair.value = String.valueOf(newRssi);
			pairList.add(rssiPair);

			insertRecord(context, pairList);
			
			return true;
		}

		return super.receiveBroadcast(context, intent);
	}

	@Override
	public boolean needTimeLimit() {
		// TODO Auto-generated method stub
		return false;
	}

}
