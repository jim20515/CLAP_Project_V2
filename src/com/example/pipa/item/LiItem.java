package com.example.pipa.item;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.plpa.utils.SettingString;

public class LiItem extends ExpItemBase implements SensorEventListener {

	private final String mTag = SettingString.TAG;

	private SensorManager mSensorManager;
    private Sensor mLight;

	private final String LIGHT = "li";
	
	float preLi = 0;
	float li;
	
//	public final String ALERT_STRING = "加速度感測器";

	public LiItem(Service service) {
		// TODO Auto-generated constructor stub

		super(service);
		mExpPrefix = "Li";
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.sensor.getType() == Sensor.TYPE_LIGHT) {

			li = event.values[0];
			
		}
	}

	@Override
	public boolean onStart(Context context) {
		// TODO Auto-generated method stub
		mSensorManager = (SensorManager)mService.getSystemService(Context.SENSOR_SERVICE);
		mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_UI);
        
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
		
		if(li != preLi) {
			insertRecord(mService, LIGHT, li);
			
			if(SettingString.mIsDebug) Log.d(mTag, "Light:" + li);
		}
		
		preLi = li;
		
		super.receiveIntervalEvent(c, i);
	}
}
