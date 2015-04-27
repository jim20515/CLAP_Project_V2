package com.example.pipa.item;

import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.plpa.utils.SettingString;

public class AccItem extends ExpItemBase implements SensorEventListener {

	private final String mTag = SettingString.TAG;

	private Service mService;
	private SensorManager mSensorManager;
    private Sensor mAccelerometer;
	
	private final String ACCELEROMETER_X = "ax";
	private final String ACCELEROMETER_Y = "ay";
	private final String ACCELEROMETER_Z = "az";
	
	private final String PREF_ACCELEROMETER_X;
	private final String PREF_ACCELEROMETER_Y;
	private final String PREF_ACCELEROMETER_Z;

	public final String ALERT_STRING = "加速度感測器";

	public AccItem(Service service) {
		// TODO Auto-generated constructor stub

		mExpPrefix = "acc.";
		mService = service;
		needTimeLimit = true;
		
		PREF_ACCELEROMETER_X = mExpPrefix + ACCELEROMETER_X;
		PREF_ACCELEROMETER_Y = mExpPrefix + ACCELEROMETER_Y;
		PREF_ACCELEROMETER_Z = mExpPrefix + ACCELEROMETER_Z;
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

			float newX = event.values[SensorManager.DATA_X];
			float newY = event.values[SensorManager.DATA_Y];
			float newZ = event.values[SensorManager.DATA_Z];
			
			if(SettingString.mIsDebug) Log.d(mTag, "x:" + newX + " y:" + newY + " z:" + newZ);
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

	
}
