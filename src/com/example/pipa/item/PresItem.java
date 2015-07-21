package com.example.pipa.item;

import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.plpa.utils.SettingString;

public class PresItem extends ExpItemBase implements SensorEventListener {

	private final String mTag = SettingString.TAG;

	private SensorManager mSensorManager;
    private Sensor mPressureSensor;

	private final String PRESSURE = "pres";
	
//	public final String ALERT_STRING = "加速度感測器";

	public PresItem(Service service) {
		// TODO Auto-generated constructor stub

		super(service);
		mExpPrefix = "Pres";
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {

			float pres = event.values[0];
			
			insertRecord(mService, PRESSURE, pres);
			
			if(SettingString.mIsDebug) Log.d(mTag, "Pres:" + pres);
		}
	}

	@Override
	public boolean onStart(Context context) {
		// TODO Auto-generated method stub
		mSensorManager = (SensorManager)mService.getSystemService(Context.SENSOR_SERVICE);
		mPressureSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        
        mSensorManager.registerListener(this, mPressureSensor, SensorManager.SENSOR_DELAY_UI);
        
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
		return false;
	}

	
}
