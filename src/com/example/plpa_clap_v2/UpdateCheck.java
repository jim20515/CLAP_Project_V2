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
			/* �HBundle����Ѷ}�ǨӪ��Ѽ� */
			Bundle mBundle01 = intent.getExtras();
			String strParam1 = "";
			String strParam2 = "";
			String strParam3 = "";
			/* �YBundle���󤣬��ŭȡA���X�Ѽ� */
			if (mBundle01 != null) {
				/* �N���X��STR_PARAM01�ѼơA�s���strParam1�r�ꤤ */
				strParam1 = mBundle01.getString(SettingString.TEST_ID);
				strParam2 = mBundle01.getString(SettingString.TEST_SCRIPT);
				strParam3 = mBundle01.getString(SettingString.TEST_DESCRIPT);
				Log.v(TAG, "UpdateCheck" + strParam1 + strParam2 + strParam3);
			}

			/* �I�s����Activity�A�����D�{�� */
			Intent mRunPackageIntent = new Intent(context, MainActivity.class);
			mRunPackageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			if (strParam1 != "") {
				/* ���s����ID�^�� */
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
