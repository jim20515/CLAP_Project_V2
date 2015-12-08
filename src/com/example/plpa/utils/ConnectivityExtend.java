package com.example.plpa.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class ConnectivityExtend extends Connectivity {
	public static boolean isConnectedWifi3G(Context context) {
		// Only update if WiFi or 3G is connected and not roaming
		
		NetworkInfo info = getNetworkInfo(context);
		TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		
		int netType = info.getType();
		int netSubtype = info.getSubtype();
		if (netType == ConnectivityManager.TYPE_WIFI) {
		    return info.isConnected();
		} else if (netType == ConnectivityManager.TYPE_MOBILE
		        && netSubtype == TelephonyManager.NETWORK_TYPE_UMTS
		        && !mTelephony.isNetworkRoaming()) {
		    return info.isConnected();
		} else {
		    return false;
		}
	}
}
