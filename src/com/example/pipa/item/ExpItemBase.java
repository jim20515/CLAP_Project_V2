package com.example.pipa.item;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.example.plpa.utils.DBHelper;
import com.example.plpa.utils.DbConstants;

public abstract class ExpItemBase {
	
	public String mExpPrefix;
	public ArrayList<ExpItemAttribute> mExpRealAttributes;
	
	public ExpItemBase(){
		mExpRealAttributes = new ArrayList<ExpItemAttribute>();
		
	};
	public IntentFilter getIntentFilter() {return null;}
	public void onDestroy(){}
	public boolean receiveBroadcast(Context context, Intent intent) {return false;}
	public void doSomethingBeforeUpload(Context context){};
	
	public void insertRecord(Context context, String attr, String value) {
		DbConstants dbItem = new DbConstants();
		
		dbItem.setItem(mExpPrefix.replace(".", ""));
		dbItem.setAttr(attr);
		dbItem.setAttrval(value);
//		dbItem.setExId(exId);
//		dbItem.setDeviceId(deviceId);
		
		DBHelper helper = new DBHelper(context);
		helper.addRecord(dbItem);
		
	}
	
//	private List<DbConstants> countConstantsBetween(DbConstants preConstants, 
//			DbConstants afterConstants, int interval) {
//		List<DbConstants> constants = new ArrayList<DbConstants>();
//		
//		SimpleDateFormat iso8601Format = new SimpleDateFormat(
//	            "yyyy-MM-dd HH:mm:ss");
//		
//		Date preDateTime = null;
//		Date afterDateTime = null;
//		
//	    if (preConstants.getDateTime() != null && afterConstants.getDateTime() != null) {
//	        try {
//	        	preDateTime = iso8601Format.parse(preConstants.getDateTime());
//	        	afterDateTime = iso8601Format.parse(afterConstants.getDateTime());
//	        	
//	        } catch (ParseException e) {
//	        	preDateTime = null;
//	        	afterDateTime = null;
//	        	
//	        } catch (java.text.ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	    }
//	    
//	    if(preDateTime != null && afterDateTime != null) {
//	    	int diff = preDateTime.compareTo(afterDateTime);
//	    	
//	    	if(diff > interval) {
//	    		
//	    	}
//	    }
//	    
//		return constants;
//	}
}
