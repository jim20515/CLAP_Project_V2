package com.example.plpa_clap_v2;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.example.pipa.item.AccItem;
import com.example.pipa.item.BrowserItem;
import com.example.pipa.item.CalActItem;
import com.example.pipa.item.CallItem;
import com.example.pipa.item.ExpItemBase;
import com.example.pipa.item.ExtmediaItem;
import com.example.pipa.item.GpsItem;
import com.example.pipa.item.GsmItem;
import com.example.pipa.item.LiItem;
import com.example.pipa.item.MagnItem;
import com.example.pipa.item.OriItem;
import com.example.pipa.item.PowItem;
import com.example.pipa.item.PresItem;
import com.example.pipa.item.PxItem;
import com.example.pipa.item.ScreenItem;
import com.example.pipa.item.TempItem;
import com.example.pipa.item.WifiItem;
import com.example.plpa.utils.DBHelper;
import com.example.plpa.utils.DbConstants;
import com.example.plpa.utils.ExpApplyJson;
import com.example.plpa.utils.ExpResultJson;
import com.example.plpa.utils.PreferenceHelper;
import com.example.plpa.utils.ReadREST;
import com.example.plpa.utils.ReadREST.AsyncResponse;
import com.example.plpa.utils.SettingString;
import com.google.gson.Gson;

public class LogService extends Service implements AsyncResponse{

	private final String mTag = SettingString.TAG;
	public static final boolean mIsDebug = SettingString.mIsDebug;

//	private final String SCREENOFFUPLOAD = "screenoffupload";
//	private final String CHARGINGUPLOAD = "chargingupload";
//	private final String TIMELIMIT = "timelimit";
//	private final String RECORDLIMIT = "recordlimit";
	public static final String KEY_UPLOAD_DATE = "key_upload_date";
	
//	private static final String AMQPHOST = "140.119.221.34";
//	private static final String AMQPVHOST = "clap";
//	private static final String AMQPUSER = "clap";
//	private static final String AMQPPASSWORD = "clap@nccu";
//	private static final String QUEUE_NAME = "clap";

//	private int mTimeLimit = 10000;// default
//	private int mRecordLimit = 10000;// default
	private List<ExpApplyJson.Policy> mPolicy;
	
	private ArrayList<ExpItemBase> mRealExpItems;
	private ExpItemBase[] mAllExpItems;

	private int mDeviceId;
	private int mExpId;
	
	private static DBHelper mDbHelper = null;

	private void initService() {
		mAllExpItems = new ExpItemBase[] { 

				new AccItem(this),
				new BrowserItem(this),
				new CalActItem(this), 
				new CallItem(this),
				new ExtmediaItem(this),
				new GpsItem(this),
				new GsmItem(this),
				new LiItem(this),
				new MagnItem(this),
				new OriItem(this),
				new PowItem(this),
				new PresItem(this),
				new PxItem(this),
				new ScreenItem(this),
				new TempItem(this),
				new WifiItem(this)
				};
		
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
		
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		Log.d(mTag, "onDestroy");
		
		this.unregisterReceiver(mEventReceiver);
		
		for (ExpItemBase item : mRealExpItems) {
			item.onDestroy();
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {

		Log.d(mTag, "Start Service");
		initService();

		String applyString = PreferenceHelper.getString(this, PreferenceHelper.PREF_EXPAPPLY);
		Gson gson = new Gson();
		ExpApplyJson applyJson = gson.fromJson(applyString, ExpApplyJson.class);
		mDeviceId = PreferenceHelper.getInt(this, PreferenceHelper.CLIENT_DEVICE_ID);
		mExpId = applyJson.ExpId;
				
		mRealExpItems = determineExpItem(applyJson.Items);
		mPolicy = applyJson.Policies;
//		
//		determineUploadTime(applyJson.Policies);

//		uploadExpRecord();
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_BOOT_COMPLETED);
		filter.addAction(Intent.ACTION_BATTERY_LOW);
		filter.addAction(Intent.ACTION_BATTERY_OKAY);
		filter.addAction(Intent.ACTION_POWER_CONNECTED);
		
		//for test
//		filter.addAction(Intent.ACTION_USER_PRESENT);
		
		registerBroadcastReciever(filter);
		
		for (ExpItemBase item : mRealExpItems) {
			registerBroadcastReciever(item.getIntentFilter());
			if(!item.onStart(this))
				stopSelf();
		}

	}

	private void registerBroadcastReciever(IntentFilter filter) {
//		if (mEventReceiver == null)
//			mEventReceiver = new EventReceiver();

		if (filter != null)
			registerReceiver(mEventReceiver, filter);
		
	}
	
	private BroadcastReceiver mEventReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String actionString = intent.getAction();

			if (mIsDebug)
				Log.d(mTag, "onReceive, Get Action:" + actionString);

			boolean isReceived = false;

			if (Intent.ACTION_SCREEN_OFF.equals(actionString) || 
					Intent.ACTION_POWER_CONNECTED.equals(actionString)) {

				uploadExpRecord();
				isReceived = true;

			}
			
			if (Intent.ACTION_BATTERY_LOW.equals(actionString)) {
				//stop service
				
			}

			if (Intent.ACTION_BATTERY_OKAY.equals(actionString) ||
					Intent.ACTION_BOOT_COMPLETED.equals(actionString)) {
				//start service
				
			}
			
			for (ExpItemBase item : mRealExpItems) {
				if (item.receiveBroadcast(context, intent)) {
					isReceived = true;
					
					if (mIsDebug)
						Log.d(mTag, "Item:" + item.mExpPrefix );
					
					break;

				}
			}

			if (!isReceived)
				Log.d(mTag, "No one received this broadcast:" + actionString);
		}
		
	};
	       
	
	private ArrayList<ExpItemBase> determineExpItem(ArrayList<ExpApplyJson.Item> itemJson) {
		ArrayList<ExpItemBase> realItems = new ArrayList<ExpItemBase>();

		for (ExpApplyJson.Item jsonItem : itemJson) {
			for (ExpItemBase item : mAllExpItems) {
				
				if(jsonItem.ItemName.equals(item.mExpPrefix)) {
					
					if (!realItems.contains(item)) {
						realItems.add(item);
						
						Log.d(mTag, "Add exp item:" + item.mExpPrefix);
						break;
					}
					
//					Log.d(mTag, "Add exp item:" + item.mExpPrefix + " Attr:" + jsonItem.ItemName);
					
//					ExpItemBase realItem = realItems.get(realItems.indexOf(item));
//					realItem.mExpRealAttributes.add(jsonItem);
					
					//for individual item judge.
//					realItem.mExpRealAttributesName.add(realAttr.mName);
					
					break;
				}
			}
		}

		return realItems;
	}

//	public static class EventReceiver extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			// TODO Auto-generated method stub
//
//			
//			String actionString = intent.getAction();
//			Log.d(mTag, "onReceive, Get Action:" + actionString);
//			
//			if (mIsDebug)
//				Log.d(mTag, "onReceive, Get Action:" + actionString);
//			
//				Toast.makeText(context, actionString, Toast.LENGTH_LONG).show();
//
//			boolean isReceived = false;
//
//			if (Intent.ACTION_SCREEN_OFF.equals(actionString) || 
//					Intent.ACTION_POWER_CONNECTED.equals(actionString)) {
//
//				for (ExpItemBase item : mRealExpItems) {
//					item.doSomethingBeforeUpload(context);
//				}
//				
//				uploadExpRecord();
//				isReceived = true;
//
//			} else if (Intent.ACTION_BATTERY_LOW.equals(actionString)) {
//				//stop service
//				
//			} else if (Intent.ACTION_BATTERY_OKAY.equals(actionString) ||
//					Intent.ACTION_BOOT_COMPLETED.equals(actionString)) {
//				//start service
//				
//			} else {
//				for (ExpItemBase item : mRealExpItems) {
//					if (item.receiveBroadcast(context, intent)) {
//						isReceived = true;
//						break;
//
//					}
//				}
//
//			}
//
//			if (!isReceived)
//				Log.d(mTag, "No one received this broadcast:" + actionString);
//		}
//
//	}

	public void uploadExpRecord() {
		
//		if(!Connectivity.isConnected(this)) {
//			CommonAlertDialog.showOKAlertDialog(this, "�ж}��Wifi�H�Ѹ�ƤW��");
//			return;
//		}
		
		for (ExpItemBase item : mRealExpItems) {
			item.doSomethingBeforeUpload(this);
		}
		
		String message = null;
		message = getResultJSON();
		
//		Log.d(mTag, message);
		
		HashMap<String, String> paras = new HashMap<String, String>();
		paras.put(ReadREST.PARAMETER_EXPPOST_VALUE, message);

		try {

			ReadREST readREST = new ReadREST();
			readREST.execute(ReadREST.WEBSERVICE_EXPPOST_URL,
					ReadREST.getPostDataString(paras)); // 受測者ID和授權碼RESTFUL
			readREST.mAsyncDelegate = this;

			mDbHelper.deleteTable();
//			CheckExperimentUpdate();
			
			PreferenceHelper.setPreference(this, PreferenceHelper.UPLOADED_TIME, 
					System.currentTimeMillis());
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		ConnectionFactory factory = new ConnectionFactory();
//		factory.setHost(AMQPHOST);
//		factory.setVirtualHost(AMQPVHOST);
//		factory.setUsername(AMQPUSER);
//		factory.setPassword(AMQPPASSWORD);
//		Connection connection = null;
//		Channel channel = null;
//
//		try {
//			connection = factory.newConnection();
//			channel = connection.createChannel();
//
//			byte[] base64enc = Base64
//					.encode(message.getBytes(), Base64.DEFAULT);
//			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//			channel.basicPublish("", QUEUE_NAME, null, base64enc);
//
//			channel.close();
//			connection.close();
//
//			mDbHelper.deleteTable();
////			CheckExperimentUpdate();
//			
//			PreferenceHelper.setPreference(this, PreferenceHelper.UPLOADED_TIME, 
//					System.currentTimeMillis());
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

	}

	public String getResultJSON() {
		List<DbConstants> dbItems = mDbHelper.getAllTodoItems();

		if (dbItems != null && !dbItems.isEmpty()) {

			return ExpResultJson.transferJsonFormat(mDeviceId, mExpId, dbItems);
		}

		return "";
	}

	@Override
	public void processFinish(String[] urls, String result) {
		// TODO Auto-generated method stub
		
	}
}
