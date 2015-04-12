package com.example.plpa_clap_v2;

import java.util.ArrayList;

import com.example.pipa.item.ExpItemAttribute;
import com.example.pipa.item.ExpItemBase;
import com.example.pipa.item.PhoneCallItem;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class LogService extends Service{

	public static final String INTENTKEY_EXP_ITEMS = "intent_exp_items";
	public static final String INTENTKEY_EXP_UPLOADTIME = "intent_exp_uploadtime";
	
	private final String SCREENOFFUPLOAD = "screenoffupload";
	private final String CHARGINGUPLOAD = "chargingupload";
	private final String TIMELIMIT = "timelimit";
	private final String RECORDLIMIT = "recordlimit";
	
	private boolean mIsScreenOffUpload = false;
	private boolean mIsChargingUpload = false;
	private int mTimeLimit = 10000;//default
	private int mRecordLimit = 10000;//default
	
	private ArrayList<ExpItemBase> mRealExpItems;
	private ExpItemBase[] mAllExpItems;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {

		initService();
		
		EventReceiver EReceiver = new EventReceiver();

		String[] uploadTime = intent.getStringArrayExtra(INTENTKEY_EXP_UPLOADTIME);
		detectUploadLimit(uploadTime);
		
		ArrayList<ExpItemAttribute> items = intent.getParcelableExtra(INTENTKEY_EXP_ITEMS);
		mRealExpItems = determineExpItem(items);
		
//		IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//		getApplication().getApplicationContext().registerReceiver(EReceiver,
//				filter); // 電量初始畫
//
//		Log.v(TAG, "Log onStart");
//
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//		java.util.Date today = new Date();
//		todayTime = String.valueOf(sdf.format(today));
//		Log.v("TAG", "todayTime" + todayTime);
//
//		sqlCase = "select * from clap ";
//		// registerScreenBroadcastReceiver();
//
//		getPref(); // 取得頗析後的參數
//		if (screenupload) {
//			registerScreenBroadcastReceiver(); // 螢幕關閉待機時上傳事件註冊
//		}
//		if (chageupload) {
//			registerPowerconnectBroadcastReceiver(); // 充電時上傳事件註冊
//		}
//		Log.v(TAG, "screenupload" + String.valueOf(screenupload));
//		Log.v(TAG, "chageupload" + String.valueOf(chageupload));
//
//		logdetermine();
//		checkstart();
//		/*
//		 * SharedPreferences settings = getSharedPreferences(PREF_FILENAME,0);
//		 * checktest = settings.getBoolean("checktest",checktest); if
//		 * (checktest){ openDatabase();
//		 * 
//		 * Log.v("TAG","logdetermine startRecording"); startRecording(); }else{
//		 * 
//		 * }
//		 */

		super.onStart(intent, startId);

	}
	
	private void detectUploadLimit(String[] uploadTime) {
		if(uploadTime != null) {
			for(String item : uploadTime) {
				String trimItem = item.trim();
				
				if(trimItem.equals(SCREENOFFUPLOAD))
					mIsScreenOffUpload = true;
				else if (trimItem.equals(CHARGINGUPLOAD))
					mIsChargingUpload = true;
				else {
					String[] token = trimItem.split(" ");
					
					if(token.length == 2) {
						try {
							if(TIMELIMIT.equals(token[0]))
								mTimeLimit = Integer.valueOf(token[1]);
							else if (RECORDLIMIT.equals(token[1]))
								mRecordLimit = Integer.valueOf(token[1]);
						} catch (Exception e) {
							// TODO: handle exception
							
							Log.d(MainActivity.mTag, e.getStackTrace() + "");
						}
						
					}
				}
			}
			
		} else {
			mIsScreenOffUpload = mIsChargingUpload = false;
			mTimeLimit = 10000;
			mRecordLimit = 10000;
		}
		
	}
	
	private ArrayList<ExpItemBase> determineExpItem(ArrayList<ExpItemAttribute> attris) {
		ArrayList<ExpItemBase> realItems = new ArrayList<ExpItemBase>();
		
		for(ExpItemAttribute realAttr : attris) {
			for(ExpItemBase item : mAllExpItems) {
				if(realAttr.mName.contains(item.mExpPrefix)) {
				
					if(!realItems.contains(item))
						realItems.add(item);
					
					realItems.get(realItems.indexOf(item)).mExpRealAttributes.add(realAttr);
				}
			}
		}
			
		return realItems;
	}
	
	public class EventReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private void initService(){
		mAllExpItems = new ExpItemBase[]{new PhoneCallItem(this)};
	}
}
