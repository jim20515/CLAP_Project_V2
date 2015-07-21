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

public class OriItem extends ExpItemBase implements SensorEventListener {

	private final String mTag = SettingString.TAG;

	private SensorManager mSensorManager;
    private Sensor mOrientation;

    float newX;
	float newY;
	float newZ;
	
	private final String ORIENTATION_X = "ox";
	private final String ORIENTATION_Y = "oy";
	private final String ORIENTATION_Z = "oz";
	
	public final String ALERT_STRING = "方向感測器";

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

			newX = event.values[SensorManager.DATA_X];
			newY = event.values[SensorManager.DATA_Y];
			newZ = event.values[SensorManager.DATA_Z];
			
//			if(SettingString.mIsDebug) Log.d(mTag, "x:" + newX + " y:" + newY + " z:" + newZ);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onStart(Context context) {
		// TODO Auto-generated method stub
		mSensorManager = (SensorManager)mService.getSystemService(Context.SENSOR_SERVICE);
		mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        
        mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_UI);
        
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
		
		if(SettingString.mIsDebug) Log.d(mTag, "ox:" + newX + " oy:" + newY + " oz:" + newZ);
		
		List<ExpItemBase.RecordPair> pairList = new ArrayList<ExpItemBase.RecordPair>();
		ExpItemBase.RecordPair xyzPair = new ExpItemBase.RecordPair();
		
		xyzPair.key = ORIENTATION_X;
		xyzPair.value = String.valueOf(newX);
		pairList.add(xyzPair);

		xyzPair.key = ORIENTATION_Y;
		xyzPair.value = String.valueOf(newY);
		pairList.add(xyzPair);

		xyzPair.key = ORIENTATION_Z;
		xyzPair.value = String.valueOf(newZ);
		pairList.add(xyzPair);
		
		insertRecord(c, pairList);
		
		super.receiveIntervalEvent(c, i);
	}
}
