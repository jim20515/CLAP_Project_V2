package com.example.pipa.item;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
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

	private final String PREF_SSID;
	private final String PREF_RSSI;

	public final String ALERT_STRING = "WiFI資訊";

	public WifiItem(Service service) {
		// TODO Auto-generated constructor stub

		mExpPrefix = "wifi.";
		needTimeLimit = true;

		PREF_SSID = mExpPrefix + SSID;
		PREF_RSSI = mExpPrefix + RSSI;
	}

	@Override
	public boolean onStart(Context context) {
		// TODO Auto-generated method stub

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		final NetworkInfo.State state = info.getState();
		if (SettingString.mIsDebug)
			Log.d(mTag, "Wifi state:" + state.toString());

		if (info == null || !state.equals(NetworkInfo.State.CONNECTED)) {
			Toast.makeText(context, "請開啟Wifi服務\n開啟後請重新啟動實驗", Toast.LENGTH_LONG)
					.show();
			return false;
		}

		return super.onStart(context);
	}

	@Override
	public IntentFilter getIntentFilter() {
		// TODO Auto-generated method stub
		IntentFilter filter = new IntentFilter();

		if (mExpRealAttributesName.contains(PREF_SSID)) {
			filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		}
		
		if (mExpRealAttributesName.contains(PREF_RSSI))
			filter.addAction(WifiManager.RSSI_CHANGED_ACTION);

		return filter;
	}

	@SuppressWarnings({ "static-access", "deprecation" })
	@Override
	public boolean receiveBroadcast(Context context, Intent intent) {
		// TODO Auto-generated method stub

		String action = intent.getAction();

		if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
			NetworkInfo networkInfo = intent
					.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
			if (networkInfo.isConnected()) {
				// Wifi is connected

				WifiManager wifiManager = (WifiManager) context
						.getSystemService(context.WIFI_SERVICE);
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				String ssid = wifiInfo.getSSID();

				if (SettingString.mIsDebug)
					Log.d(mTag, "Wifi is connected: " + ssid);
				insertRecord(context, SSID, ssid);
			}

			return true;

		} else if (intent.getAction().equals(
				ConnectivityManager.CONNECTIVITY_ACTION)) {
			NetworkInfo networkInfo = intent
					.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI
					&& !networkInfo.isConnected()) {
				// Wifi is disconnected

				WifiManager wifiManager = (WifiManager) context
						.getSystemService(context.WIFI_SERVICE);
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				String ssid = wifiInfo.getSSID();

				if (SettingString.mIsDebug)
					Log.d(mTag, "Wifi is disconnected: " + ssid);
				insertRecord(context, SSID, ssid);
			}

			return true;

		} else if (WifiManager.RSSI_CHANGED_ACTION.equals(action)) {

			if (mExpRealAttributesName.contains(PREF_RSSI)) {
				if (SettingString.mIsDebug)
					Log.d(mTag, "Wifi RSSI Change");

				int newRssi = intent.getIntExtra(WifiManager.EXTRA_NEW_RSSI, 0);

				insertRecord(context, RSSI, newRssi);
				return true;
			}
		}

		return super.receiveBroadcast(context, intent);
	}

}
