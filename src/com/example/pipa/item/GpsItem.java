package com.example.pipa.item;

import android.app.Service;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.plpa.utils.DateTimeHelper;
import com.example.plpa.utils.SettingString;

public class GpsItem extends ExpItemBase implements LocationListener {

	private final String mTag = SettingString.TAG;

	private Context mContext;
	private LocationManager lms;
	private String bestProvider = LocationManager.GPS_PROVIDER;	//最佳資訊提供者
	private final int DURATION_SECOND = 1;
	
	private final String LONGITUDE = "longitude";
	private final String LATITUDE = "latitude";
	private final String ACCURACY = "accuracy";
	private final String SPEED = "speed";
	private final String BEARING = "bearing";
	private final String ALTITUDE = "altitude";
	
	private final String PREF_LONGITUDE;
	private final String PREF_LATITUDE;
	private final String PREF_ACCURACY;
	private final String PREF_SPEED;
	private final String PREF_BEARING;
	private final String PREF_ALTITUDE;

	public final String ALERT_STRING = "GPS資訊";

	public GpsItem(Service service) {
		// TODO Auto-generated constructor stub

		mExpPrefix = "gps.";
		needTimeLimit = false;
		
		PREF_LONGITUDE = mExpPrefix + LONGITUDE;
		PREF_LATITUDE = mExpPrefix + LATITUDE;
		PREF_ACCURACY = mExpPrefix + ACCURACY;
		PREF_SPEED = mExpPrefix + SPEED;
		PREF_BEARING = mExpPrefix + BEARING;
		PREF_ALTITUDE = mExpPrefix + ALTITUDE;
	}

	@Override
	public boolean onStart(Context context) {
		// TODO Auto-generated method stub
		
		mContext = context;
		
		//取得系統定位服務
		LocationManager status = (LocationManager) (context.getSystemService(Context.LOCATION_SERVICE));
		if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) || status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			//如果GPS或網路定位開啟，呼叫locationServiceInitial()更新位置
			locationServiceInitial(context);
		} else {
			Toast.makeText(context, "請開啟定位服務\n開啟後請重新啟動實驗", Toast.LENGTH_LONG).show();//開啟設定頁面
			return false;
		}
		
		return super.onStart(context);
	}

	private void locationServiceInitial(Context context) {
		lms = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);	//取得系統定位服務
		Criteria criteria = new Criteria();	//資訊提供者選取標準
		bestProvider = lms.getBestProvider(criteria, true);	//選擇精準度最高的提供者

		//服務提供者、更新頻率60000毫秒=1分鐘、最短距離、地點改變時呼叫物件
		lms.requestLocationUpdates(bestProvider, DURATION_SECOND * 1000, 1, this);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
		if(lms != null)
			lms.removeUpdates(this);	//離開頁面時停止更新
		
		super.onDestroy();
	}

	@Override
	public void onLocationChanged(Location arg0) {	
		//當地點改變時
		// TODO Auto-generated method stub

		if(mExpRealAttributesName.contains(PREF_LONGITUDE))
			insertRecord(mContext, LONGITUDE, arg0.getLongitude());
		
		if(mExpRealAttributesName.contains(PREF_LATITUDE))
			insertRecord(mContext, LATITUDE, arg0.getLatitude());
		
		if(mExpRealAttributesName.contains(PREF_ACCURACY))
			insertRecord(mContext, ACCURACY, arg0.getAccuracy());
		
		if(mExpRealAttributesName.contains(PREF_SPEED))
			insertRecord(mContext, SPEED, arg0.getSpeed());
		
		if(mExpRealAttributesName.contains(PREF_BEARING))
			insertRecord(mContext, BEARING, arg0.getBearing());
		
		if(mExpRealAttributesName.contains(PREF_ALTITUDE))
			insertRecord(mContext, ALTITUDE, arg0.getAltitude());
		
		if(SettingString.mIsDebug) Log.d(mTag, DateTimeHelper.getNowFormat() + " Location Lat:" + arg0.getLatitude() + " Long:" + arg0.getLongitude());
	}
 
	@Override
	public void onProviderDisabled(String arg0) {	
		//當GPS或網路定位功能關閉時
		// TODO Auto-generated method stub
 
		if(lms != null)
			lms.removeUpdates(this);
	}
 
	@Override
	public void onProviderEnabled(String arg0) {	
		//當GPS或網路定位功能開啟
		// TODO Auto-generated method stub
		
		locationServiceInitial(mContext);
	}
 
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {	
		//定位狀態改變
		//status=OUT_OF_SERVICE 供應商停止服務
		//status=TEMPORARILY_UNAVAILABLE 供應商暫停服務
	}

	
}