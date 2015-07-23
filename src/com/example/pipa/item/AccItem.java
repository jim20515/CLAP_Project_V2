package com.example.pipa.item;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.plpa.utils.SettingString;

public class AccItem extends ExpItemBase implements SensorEventListener {

	private final String mTag = SettingString.TAG;

	private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    
    float newX;
	float newY;
	float newZ;
	
	private final String ACCELEROMETER_X = "ax";
	private final String ACCELEROMETER_Y = "ay";
	private final String ACCELEROMETER_Z = "az";
	
//	private final String PREF_ACCELEROMETER_X = ACCELEROMETER_X;
//	private final String PREF_ACCELEROMETER_Y = ACCELEROMETER_Y;
//	private final String PREF_ACCELEROMETER_Z = ACCELEROMETER_Z;

	public final String ALERT_STRING = "加速度感測器";

	public AccItem(Service service) {
		// TODO Auto-generated constructor stub

		super(service);
		mExpPrefix = "Acc";
		mService = service;
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		Log.d(mTag, sensor.toString() + " accuracy:" + accuracy);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

			newX = event.values[SensorManager.DATA_X];
			newY = event.values[SensorManager.DATA_Y];
			newZ = event.values[SensorManager.DATA_Z];
			
		}
	}

	@Override
	public boolean onStart(Context context) {
		// TODO Auto-generated method stub
		mSensorManager = (SensorManager)mService.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        
		return super.onStart(context);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mSensorManager.unregisterListener(this);
		
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
		
		if(SettingString.mIsDebug) Log.d(mTag, "ax:" + newX + " ay:" + newY + " az:" + newZ);
		
		List<RecordPair> pairList = new ArrayList<RecordPair>();
		RecordPair xyzPair = new RecordPair();
		
		xyzPair.key = ACCELEROMETER_X;
		xyzPair.value = String.valueOf(newX);
		pairList.add(xyzPair);

		xyzPair.key = ACCELEROMETER_Y;
		xyzPair.value = String.valueOf(newY);
		pairList.add(xyzPair);

		xyzPair.key = ACCELEROMETER_Z;
		xyzPair.value = String.valueOf(newZ);
		pairList.add(xyzPair);
		
		insertRecord(c, pairList);
		
		super.receiveIntervalEvent(c, i);
	}

}
