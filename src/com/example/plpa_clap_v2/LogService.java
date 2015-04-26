package com.example.plpa_clap_v2;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;

import com.example.pipa.item.CallItem;
import com.example.pipa.item.ExpItemAttribute;
import com.example.pipa.item.ExpItemBase;
import com.example.plpa.utils.CommonAlertDialog;
import com.example.plpa.utils.Connectivity;
import com.example.plpa.utils.DBHelper;
import com.example.plpa.utils.DbConstants;
import com.example.plpa.utils.ExpJson;
import com.example.plpa.utils.PreferenceHelper;
import com.example.plpa.utils.SettingString;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class LogService extends Service {

	private final String mTag = SettingString.TAG;
	private final boolean mIsDebug = MainActivity.mIsDebug;

	private final String SCREENOFFUPLOAD = "screenoffupload";
	private final String CHARGINGUPLOAD = "chargingupload";
	private final String TIMELIMIT = "timelimit";
	private final String RECORDLIMIT = "recordlimit";
	public static final String KEY_UPLOAD_DATE = "key_upload_date";
	
	private static String AMQPHOST = "140.119.221.34";
	private static String AMQPVHOST = "clap";
	private static String AMQPUSER = "clap";
	private static String AMQPPASSWORD = "clap@nccu";
	private static String QUEUE_NAME = "clap";

	private int mTimeLimit = 10000;// default
	private int mRecordLimit = 10000;// default

	private ArrayList<ExpItemBase> mRealExpItems;
	private ExpItemBase[] mAllExpItems;

	private EventReceiver mEventReceiver;
	private static DBHelper mDbHelper = null;

	private void initService() {
		mAllExpItems = new ExpItemBase[] { new CallItem(this) };
		mDbHelper = new DBHelper(this);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		super.onStartCommand(intent, flags, startId);
		
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		for (ExpItemBase item : mRealExpItems) {
			item.onDestroy();
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {

		Log.d(mTag, "Start Service");
		initService();

		ArrayList<ExpItemAttribute> items = (ArrayList<ExpItemAttribute>) intent
				.getSerializableExtra(SettingString.INTENTKEY_EXP_ITEMS);
		mRealExpItems = determineExpItem(items);

		String[] uploadTimes = intent
				.getStringArrayExtra(SettingString.INTENTKEY_EXP_UPLOADTIME);
		determineUploadTime(uploadTimes);

		for (ExpItemBase item : mRealExpItems) {
			registerBroadcastReciever(item.getIntentFilter());
		}
		
		// IntentFilter filter = new
		// IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		// getApplication().getApplicationContext().registerReceiver(EReceiver,
		// filter); // 電量初始畫
		//
		// Log.v(TAG, "Log onStart");
		//
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		// java.util.Date today = new Date();
		// todayTime = String.valueOf(sdf.format(today));
		// Log.v("TAG", "todayTime" + todayTime);
		//
		// sqlCase = "select * from clap ";
		// // registerScreenBroadcastReceiver();
		//
		// getPref(); // 取得頗析後的參數
		// if (screenupload) {
		// registerScreenBroadcastReceiver(); // 螢幕關閉待機時上傳事件註冊
		// }
		// if (chageupload) {
		// registerPowerconnectBroadcastReceiver(); // 充電時上傳事件註冊
		// }
		// Log.v(TAG, "screenupload" + String.valueOf(screenupload));
		// Log.v(TAG, "chageupload" + String.valueOf(chageupload));
		//
		// logdetermine();
		// checkstart();
		// /*
		// * SharedPreferences settings = getSharedPreferences(PREF_FILENAME,0);
		// * checktest = settings.getBoolean("checktest",checktest); if
		// * (checktest){ openDatabase();
		// *
		// * Log.v("TAG","logdetermine startRecording"); startRecording();
		// }else{
		// *
		// * }
		// */

	}

	private void determineUploadTime(String[] uploadTime) {
		if (uploadTime != null) {
			IntentFilter filter = new IntentFilter();

			for (String item : uploadTime) {
				String trimItem = item.trim();
				
				if (trimItem.equals(SCREENOFFUPLOAD))
					filter.addAction(Intent.ACTION_SCREEN_OFF);
				else if (trimItem.equals(CHARGINGUPLOAD))
					filter.addAction(Intent.ACTION_POWER_CONNECTED);
				else {
					String[] token = trimItem.split(" ");

					if (token.length == 2) {
						try {
							if (TIMELIMIT.equals(token[0]))
								mTimeLimit = Integer.valueOf(token[1]);
							else if (RECORDLIMIT.equals(token[1]))
								mRecordLimit = Integer.valueOf(token[1]);
						} catch (Exception e) {
							// TODO: handle exception

							Log.d(mTag, e.getStackTrace() + "");
						}

					}
				}
			}

			registerBroadcastReciever(filter);

		} else {
			mTimeLimit = 10000;
			mRecordLimit = 10000;
		}

	}

	private void registerBroadcastReciever(IntentFilter filter) {
		if (mEventReceiver == null)
			mEventReceiver = new EventReceiver();

		if (filter != null)
			registerReceiver(mEventReceiver, filter);
	}

	private ArrayList<ExpItemBase> determineExpItem(
			ArrayList<ExpItemAttribute> attris) {
		ArrayList<ExpItemBase> realItems = new ArrayList<ExpItemBase>();

		for (ExpItemAttribute realAttr : attris) {
			for (ExpItemBase item : mAllExpItems) {
				if (realAttr.mName.contains(item.mExpPrefix)) {

					if (!realItems.contains(item))
						realItems.add(item);

					realItems.get(realItems.indexOf(item)).mExpRealAttributes
							.add(realAttr);

					break;
				}
			}
		}

		return realItems;
	}

	public class EventReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			String actionString = intent.getAction();

			if (mIsDebug)
				Log.d(mTag, "onReceive, Get Action:" + actionString);

			boolean isReceived = false;

			if (Intent.ACTION_SCREEN_OFF.equals(actionString)) {

				uploadExpRecord();
				isReceived = true;

			} else if (Intent.ACTION_POWER_CONNECTED.equals(actionString)) {

				isReceived = true;

			} else {
				for (ExpItemBase item : mRealExpItems) {
					if (item.receiveBroadcast(context, intent)) {
						isReceived = true;
						break;

					}
				}

			}

			if (!isReceived)
				Log.d(mTag, "No one received this broadcast:" + actionString);
		}

	}

	public void uploadExpRecord() {
		
		if(!Connectivity.isConnected(this)) {
			CommonAlertDialog.showOKAlertDialog(this, "請開啟Wifi以供資料上傳");
			return;
		}
		
		for (ExpItemBase item : mRealExpItems) {
			item.doSomethingBeforeUpload(this);
		}
		
		String message = null;
		message = getResultJSON();
		Log.d(mTag, message);
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(AMQPHOST);
		factory.setVirtualHost(AMQPVHOST);
		factory.setUsername(AMQPUSER);
		factory.setPassword(AMQPPASSWORD);
		Connection connection = null;
		Channel channel = null;

		try {
			connection = factory.newConnection();
			channel = connection.createChannel();

			byte[] base64enc = Base64
					.encode(message.getBytes(), Base64.DEFAULT);
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			channel.basicPublish("", QUEUE_NAME, null, base64enc);

			channel.close();
			connection.close();

			mDbHelper.deleteTable();
//			CheckExperimentUpdate();
			
			PreferenceHelper.setPreference(this, PreferenceHelper.UPLOADED_TIME, 
					System.currentTimeMillis());
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public String getResultJSON() {
		List<DbConstants> dbItems = mDbHelper.getAllTodoItems();

		if (dbItems != null && !dbItems.isEmpty()) {
			ExpJson recordJson = new ExpJson(this);

			return recordJson.transferJsonFormat(dbItems);
		}

		return "";
	}
}
