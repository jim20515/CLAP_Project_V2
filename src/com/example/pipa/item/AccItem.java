package com.example.pipa.item;

import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.plpa.utils.SettingString;

public class AccItem extends ExpItemBase implements SensorEventListener {

	private final String mTag = SettingString.TAG;

	private final SensorManager mSensorManager;
    private final Sensor mAccelerometer;
	
	private final String ACCELEROMETER_X = "ax";
	private final String ACCELEROMETER_Y = "ay";
	private final String ACCELEROMETER_Z = "az";

	public final String ALERT_STRING = "加速度感測器";

	public AccItem(Service service) {
		// TODO Auto-generated constructor stub

		mExpPrefix = "acc.";
		mSensorManager = (SensorManager)service.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mSensorManager.unregisterListener(this);
		
		super.onDestroy();
	}

	
}
