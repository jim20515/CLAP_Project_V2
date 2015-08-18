package com.example.pipa.item;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.TrafficStats;
import android.util.Log;

import com.example.plpa.utils.SettingString;

public class TrafficItem extends ExpItemBase {

	private final String mTag = SettingString.TAG;

	TrafficRecord latest = null;
	TrafficRecord previous = null;
//	long latestDate;

//	private final String PREF_TRAFFIC_LATEST_DATE = "traffic_latest_date";

	private final String ACCELEROMETER_TX = "tx";
	private final String ACCELEROMETER_RX = "rx";

	public final String ALERT_STRING = "網路流量監測";

	public TrafficItem(Service service) {
		// TODO Auto-generated constructor stub

		super(service);
		mExpPrefix = "Traffic";
		mService = service;

		// one hour to record the network traffic
		DEFAULT_ALARM_TIME = ONE_SECOND * 60 * 60;

	}

	@Override
	public boolean onStart(Context context) {
		// TODO Auto-generated method stub
		
//		latestDate = DateTimeHelper.getNow();
//		PreferenceHelper.setPreference(context, PREF_TRAFFIC_LATEST_DATE, latestDate);
		
		latest = new TrafficRecord();
		previous = new TrafficRecord();

		return super.onStart(context);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
//		latestDate = PreferenceHelper.getLong(mService, PREF_TRAFFIC_LATEST_DATE);
//		if(latestDate != -1) {
			insertDB(mService);
//		}
		
		super.onDestroy();
	}

	@Override
	public boolean needTimeLimit() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void receiveIntervalEvent(Context c, Intent i) {
		// TODO Auto-generated method stub

		insertDB(c);
		
		super.receiveIntervalEvent(c, i);
	}

	private void insertDB(Context c) {
		//寫資料庫
		latest = new TrafficRecord();
		
		TrafficRecord diffRecord = latest.diffRecord(previous);
		
		if (SettingString.mIsDebug)
			Log.d(mTag, "delta tx:" + diffRecord.tx + " delta rx:" + diffRecord.rx);

		
		List<RecordPair> pairList = new ArrayList<RecordPair>();
		RecordPair TrafficPair = new RecordPair();

		TrafficPair.key = ACCELEROMETER_TX;
		TrafficPair.value = String.valueOf(diffRecord.tx);
		pairList.add(TrafficPair);

		TrafficPair.key = ACCELEROMETER_RX;
		TrafficPair.value = String.valueOf(diffRecord.rx);
		pairList.add(TrafficPair);

		insertRecord(c, pairList);

		previous = latest;
//		PreferenceHelper.setPreference(c, PREF_TRAFFIC_LATEST_DATE, DateTimeHelper.getNow());
	}

	class TrafficRecord {
		long tx = 0;
		long rx = 0;

		// String tag=null;

		TrafficRecord() {
			tx = TrafficStats.getTotalTxBytes();
			rx = TrafficStats.getTotalRxBytes();
		}
		
		TrafficRecord diffRecord(TrafficRecord record){
			TrafficRecord diffRecord = new TrafficRecord();
			diffRecord.rx = Math.abs(this.rx - record.rx);
			diffRecord.tx = Math.abs(this.tx - record.tx);
			
			return diffRecord;
		}
		//
		// TrafficRecord(int uid, String tag) {
		// tx=TrafficStats.getUidTxBytes(uid);
		// rx=TrafficStats.getUidRxBytes(uid);
		// this.tag=tag;
		// }
	}

//	@SuppressLint("UseSparseArrays")
//	class TrafficSnapshot {
//		TrafficRecord device = null;
//		HashMap<Integer, TrafficRecord> apps = new HashMap<Integer, TrafficRecord>();
//
//		TrafficSnapshot(Context ctxt) {
//			device = new TrafficRecord();
//			
//			 HashMap<Integer, String> appNames=new HashMap<Integer, String>();
//			
//			 for (ApplicationInfo app :
//			 ctxt.getPackageManager().getInstalledApplications(0)) {
//			 appNames.put(app.uid, app.packageName);
//			 }
//			
//			 for (Integer uid : appNames.keySet()) {
//			 apps.put(uid, new TrafficRecord(uid, appNames.get(uid)));
//			 }
//		}
//	}
}
