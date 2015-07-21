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

public class MagnItem extends ExpItemBase implements SensorEventListener {

	private final String mTag = SettingString.TAG;

	private SensorManager mSensorManager;
    private Sensor mMagnetic;

    private String MAGNETIC_X = "mx";
    private String MAGNETIC_Y = "my";
    private String MAGNETIC_Z = "mz";
    
    float newX;
	float newY;
	float newZ;
	
//	public final String ALERT_STRING = "加速度感測器";

	public MagnItem(Service service) {
		// TODO Auto-generated constructor stub

		super(service);
		mExpPrefix = "Magn";
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		Log.d(mTag, sensor.toString() + " Magnetic:" + accuracy);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

			newX = event.values[SensorManager.DATA_X];
			newY = event.values[SensorManager.DATA_Y];
			newZ = event.values[SensorManager.DATA_Z];
			
		}
	}

	@Override
	public boolean onStart(Context context) {
		// TODO Auto-generated method stub
		mSensorManager = (SensorManager)mService.getSystemService(Context.SENSOR_SERVICE);
		mMagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        
        mSensorManager.registerListener(this, mMagnetic, SensorManager.SENSOR_DELAY_UI);
        
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
		
		if(SettingString.mIsDebug) Log.d(mTag, "mx:" + newX + " my:" + newY + " mz:" + newZ);
		
		List<ExpItemBase.RecordPair> pairList = new ArrayList<ExpItemBase.RecordPair>();
		ExpItemBase.RecordPair xyzPair = new ExpItemBase.RecordPair();
		
		xyzPair.key = MAGNETIC_X;
		xyzPair.value = String.valueOf(newX);
		pairList.add(xyzPair);

		xyzPair.key = MAGNETIC_Y;
		xyzPair.value = String.valueOf(newY);
		pairList.add(xyzPair);

		xyzPair.key = MAGNETIC_Z;
		xyzPair.value = String.valueOf(newZ);
		pairList.add(xyzPair);
		
		insertRecord(c, pairList);
		
		super.receiveIntervalEvent(c, i);
	}
}
