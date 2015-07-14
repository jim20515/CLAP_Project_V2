package com.example.pipa.item;

import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.plpa.utils.SettingString;

public class OriItem extends ExpItemBase implements SensorEventListener {

	private final String mTag = SettingString.TAG;

	private SensorManager mSensorManager;
    private Sensor mOrientation;

	public final String ALERT_STRING = "加速度感測器";

	public OriItem(Service service) {
		// TODO Auto-generated constructor stub

		super(service);
		mExpPrefix = "Ori";
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		Log.d(mTag, sensor.toString() + " Orientation :" + accuracy);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {

			float newX = event.values[SensorManager.DATA_X];
			float newY = event.values[SensorManager.DATA_Y];
			float newZ = event.values[SensorManager.DATA_Z];
			
			if(SettingString.mIsDebug) Log.d(mTag, "x:" + newX + " y:" + newY + " z:" + newZ);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onStart(Context context) {
		// TODO Auto-generated method stub
		mSensorManager = (SensorManager)mService.getSystemService(Context.SENSOR_SERVICE);
		mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        
        mSensorManager.registerListener(this, mOrientation, TimeLimit * 1000 * 1000);
        
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

	
}
