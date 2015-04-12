package com.example.plpa_clap_v2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.plpa.utils.SettingString;

public class UpdateCheck extends BroadcastReceiver {

	public static final String HIPPO_SERVICE_IDENTIFIER = "HIPPO_ON_SERVICE_001";
	private static final String TAG = MainActivity.mTag;

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().toString().equals(HIPPO_SERVICE_IDENTIFIER)) {
			/* 以Bundle物件解開傳來的參數 */
			Bundle mBundle01 = intent.getExtras();
			String strParam1 = "";
			String strParam2 = "";
			String strParam3 = "";
			/* 若Bundle物件不為空值，取出參數 */
			if (mBundle01 != null) {
				/* 將取出的STR_PARAM01參數，存放於strParam1字串中 */
				strParam1 = mBundle01.getString(SettingString.TEST_ID);
				strParam2 = mBundle01.getString(SettingString.TEST_SCRIPT);
				strParam3 = mBundle01.getString(SettingString.TEST_DESCRIPT);
				Log.v(TAG, "UpdateCheck" + strParam1 + strParam2 + strParam3);
			}

			/* 呼叫母體Activity，喚醒原主程式 */
			Intent mRunPackageIntent = new Intent(context, MainActivity.class);
			mRunPackageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			if (strParam1 != "") {
				/* 重新實驗ID回傳 */
				mRunPackageIntent.putExtra(SettingString.ORIGIN_TEST_ID,
						strParam1);
				mRunPackageIntent.putExtra(SettingString.NEW_TEST_SCRIPT,
						strParam2);
				mRunPackageIntent.putExtra(SettingString.NEW_TEST_DESCRIPT,
						strParam3);
			} else {
				mRunPackageIntent.putExtra(SettingString.ORIGIN_TEST_ID,
						"From Service notification...");
			}
			context.startActivity(mRunPackageIntent);
		}
	}

}
