package com.example.pipa.item;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.plpa.utils.SettingString;

public class GsmItem extends ExpItemBase {

	private final String mTag = SettingString.TAG;

	TelephonyManager mTeleManager;

	private final String ATTRI_SPN = "spn";
	private final String ATTRI_GSM_RSSI = "gsmrssi";

	float preRssi = 0;
	float rssi;

	public final String ALERT_STRING = "全球行動通訊系統";

	public GsmItem(Service service) {
		// TODO Auto-generated constructor stub

		super(service);
		mExpPrefix = "Gsm";
		mService = service;

	}

	@Override
	public boolean onStart(Context context) {
		// TODO Auto-generated method stub
		/* Update the listener, and start it */

		mTeleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		mTeleManager.listen(mPhoneStateListener,
				PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

		return super.onStart(context);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (mTeleManager != null) {
			mTeleManager.listen(mPhoneStateListener,
					PhoneStateListener.LISTEN_NONE);
			mTeleManager = null;
		}

		super.onDestroy();
	}

	@Override
	public boolean needTimeLimit() {
		// TODO Auto-generated method stub
		return false;
	}

	private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {

		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			super.onSignalStrengthsChanged(signalStrength);

			rssi = signalStrength.getGsmSignalStrength();

			if (rssi != preRssi) {
				// insertRecord(mService, ATTRI_GSM_RSSI, rssi);

				List<RecordPair> list = new ArrayList<RecordPair>();

				RecordPair pair = new RecordPair();
				pair.key = ATTRI_SPN;
				pair.value = mTeleManager.getSimOperatorName();
				list.add(pair);

				pair = new RecordPair();
				pair.key = ATTRI_GSM_RSSI;
				pair.value = String.valueOf(rssi);
				list.add(pair);
				
				insertRecord(mService, list);

				if (SettingString.mIsDebug)
					Log.d(mTag,
							"Rssi:" + rssi + " name:"
									+ mTeleManager.getSimOperatorName());
			}

			preRssi = rssi;

		}

	};
}
