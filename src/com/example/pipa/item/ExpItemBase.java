package com.example.pipa.item;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;

import com.example.plpa.utils.DBHelper;
import com.example.plpa.utils.DbConstants;

public abstract class ExpItemBase {
	
	public Service mService;
	private DBHelper mHelper;
	public String mExpPrefix;
//	public ArrayList<ExpApplyJson.Item> mExpRealAttributes;
//	public final int TimeLimit = 30;

	public PendingIntent pi;
    public BroadcastReceiver br;
    public AlarmManager am;
    
    public static long ONE_SECOND = 1000;
    public long DEFAULT_ALARM_TIME = ONE_SECOND * 20;
    
//	public boolean needTimeLimit;
	
    public class RecordPair {
    	public String key;
    	public String value;
    }
    
    public class RecordPairWithTime extends RecordPair{
    	public String dateTime;
    }
    
	public ExpItemBase(Service service){
//		mExpRealAttributes = new ArrayList<ExpApplyJson.Item>();
		
		mService = service;

		mHelper = new DBHelper(service);
	};

	public abstract boolean needTimeLimit();
	public IntentFilter getIntentFilter() {return null;}
	public boolean onStart(Context context){
		
		if(needTimeLimit()) {
			br = new BroadcastReceiver() {
				@Override
				public void onReceive(Context c, Intent i) {
					
					if(i.getAction().equals(mExpPrefix)) 
						receiveIntervalEvent(c, i);
				}
			};
			context.registerReceiver(br, new IntentFilter(mExpPrefix));
			
	        Intent dialogIntent = new Intent(mExpPrefix);
	        
	        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	        pi = PendingIntent.getBroadcast(context, 0, dialogIntent,PendingIntent.FLAG_CANCEL_CURRENT);

	        am.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), DEFAULT_ALARM_TIME, pi);
		}
		return true;
	}
	public void receiveIntervalEvent(Context c, Intent i) {return ;}
	public void onDestroy(){
		try {
			if(br != null)
				mService.unregisterReceiver(br);
			
			if(pi != null)
				am.cancel(pi);
			
		} catch (Exception e){
			
		}
	}
	
	public boolean receiveBroadcast(Context context, Intent intent) {return false;}
	public void doSomethingBeforeUpload(Context context){};
	
	public void insertRecord(Context context, String attr, double value) {
		insertRecord(context, attr, String.valueOf(value), null);
	}
	
	public void insertRecord(Context context, String attr, String value) {
		insertRecord(context, attr, value, null);
	}
	
	@SuppressLint("SimpleDateFormat")
	public void insertRecord(Context context, List<RecordPair> pairs) {

		SimpleDateFormat s = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		String format = s.format(new Date());
		
		for(RecordPair pair : pairs){
			insertRecord(context, pair.key, pair.value, format);
		}
		
	}
	
	public void insertRecordWithTime(Context context, List<RecordPairWithTime> pairs) {
		
		for(RecordPairWithTime pair : pairs){
			insertRecord(context, pair.key, pair.value, pair.dateTime);
		}
	}
	
	@SuppressLint("SimpleDateFormat")
	public void insertRecord(Context context, String attr, String value, String dateTime) {
		DbConstants dbItem = new DbConstants();

//		dbItem.setItem(mExpPrefix.replace(".", ""));
		dbItem.setItem(mExpPrefix);
		dbItem.setAttr(attr);
		dbItem.setAttrval(value);
		
		if (dateTime == null) {
			SimpleDateFormat s = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
			dateTime = s.format(new Date());
		}
		
		dbItem.setDateTime(dateTime);
//		if(!checkUpdate)
			mHelper.addRecord(dbItem);
//		else
//			mHelper.AddOrUpdateRecord(dbItem);
	}
	
}
