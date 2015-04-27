package com.example.plpa_clap_v2;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.example.plpa.utils.CommonAlertDialog;
import com.example.plpa.utils.Connectivity;
import com.example.plpa.utils.ExpUrlToken;
import com.example.plpa.utils.PreferenceHelper;
import com.example.plpa.utils.ReadREST;
import com.example.plpa.utils.ReadREST.AsyncResponse;
import com.example.plpa.utils.SettingString;

public class MainActivity extends Activity implements AsyncResponse {

	private final boolean mIsDebug = SettingString.mIsDebug;
	private Button mBtnLoadDSL;
	private Button mBtnStart;
	private Button mBtnEnd;
	// private TextView mDSLTxtView;
	private Context mContext = this;

	private ExpUrlToken mExpChoice;

	private String mClicentDeviceID;
	private String mAuthCode;
	private String mDeviceOs;
	private String mDevice;
	private String mExid = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		loadClientId();
		// initExpItem();

		// 取得使用者手機型號和OS版本
		mDeviceOs = android.os.Build.VERSION.RELEASE;
		mDevice = android.os.Build.MODEL.replace(' ', '-');

		// // 實驗設定檔更新
		// Bundle mBundle01 = this.getIntent().getExtras();
		// if (mBundle01 != null) {
		// mExid = mBundle01.getString(SettingString.ORIGIN_TEST_ID);
		// mContents = mBundle01.getString(SettingString.NEW_TEST_SCRIPT);
		// mTestDescript = mBundle01
		// .getString(SettingString.NEW_TEST_DESCRIPT);
		//
		// if (mExid != null) {
		// StopService();
		// Log.v(SettingString.TAG, "refind TestId" + mExid);
		// reFindTest(mContents); // 重新將新實驗設定檔作分析
		// Log.v(SettingString.TAG, "retrun PLPA_CLAP sucessful");
		// mDSLTxtView.setText(mTestDescript);
		// new AlertDialog.Builder(mContext)
		// .setMessage("實驗有更新")
		// .setPositiveButton(R.string.update,
		// new DialogInterface.OnClickListener() { // 確認更新
		// public void onClick(DialogInterface dialog,
		// int whichButton) {
		// testfileCheck(); // 刪除舊實驗參數，產生新實驗參數檔
		// mDSLTxtView.setText(mTestDescript);
		// startRecording();
		// Log.v(SettingString.TAG, "update sucessful");
		// }
		// })
		// .setNegativeButton(R.string.str_no,
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog,
		// int whichButton) {
		// // finish();
		// }
		// }).show();
		// }
		// }

	}

	private void initView() {
		mBtnLoadDSL = (Button) findViewById(R.id.button_load);
		mBtnStart = (Button) findViewById(R.id.button_start);
		mBtnEnd = (Button) findViewById(R.id.button_end);
		// mDSLTxtView = (TextView) findViewById(R.id.DSLContents);

		updateUI(isMyServiceRunning(LogService.class));
	}

	private void updateUI(boolean isRunning) {
		mBtnStart.setEnabled(!isRunning);
		mBtnLoadDSL.setEnabled(!isRunning);
		mBtnEnd.setEnabled(isRunning);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// 取得受測者ID和授權碼
	private void loadClientId() {
		// TODO Auto-generated method stub

		// 應該可以用SharedPreferences來檢查
		File f = new File(PreferenceHelper.XML_PATH);
		if (f.exists()) {
			Log.v(SettingString.TAG, "SharedPreferences checkid : exist");
			mClicentDeviceID = PreferenceHelper.getString(this,
					PreferenceHelper.CLIENT_DEVICE_ID);
			mAuthCode = PreferenceHelper.getString(this,
					PreferenceHelper.AUTH_CODE);

		} else {
			Log.v(SettingString.TAG, "New checkId url:"
					+ ReadREST.WEBSERVICE_ID_URI);
			ReadREST readREST = new ReadREST();
			readREST.execute(ReadREST.WEBSERVICE_ID_URI); // 受測者ID和授權碼RESTFUL
			readREST.mAsyncDelegate = this;

		}

	}

	// 各按鈕觸發事件
	public void onClick(View v) {

		if (mClicentDeviceID == null || mClicentDeviceID.equals("")) {
			CommonAlertDialog.showOKAlertDialog(this, "Server連結錯誤，請嘗試重新連線");
			return;
		}

		switch (v.getId()) {
		case R.id.button_load:
			loadDSL(); // 取得實驗列表
			break;
		case R.id.button_start:
			startRecording(); // 重新恢復復錄

			break;
		case R.id.button_end:
			StopService(); // 停止記錄

			break;
		default:
			break;
		}
	}

	// 取得實驗列表
	private void loadDSL() {
		if (Connectivity.isConnected(this)) { // 偵測有無網路狀態
			loadExperimentList();

		} else { // 偵測沒有網路，提醒開啟網路功能
			CommonAlertDialog.showOKAlertDialog(this, "為方便實驗，請開啟無線網路功能");
		}
	}

	// 取得實驗列表
	private void loadExperimentList() {
		// REST
		String abc = String.format(ReadREST.WEBSERVICE_EXPLIST_URL, mDeviceOs,
				mDevice, mClicentDeviceID, mAuthCode);
		if (mIsDebug)
			Log.d(SettingString.TAG, "Load Experiment Url:" + abc);
		ReadREST readREST = new ReadREST();
		readREST.execute(String.format(ReadREST.WEBSERVICE_EXPLIST_URL,
				mDeviceOs, mDevice, mClicentDeviceID, mAuthCode));
		readREST.mAsyncDelegate = this;

	}

	// 將實驗列表以選單方式提供受測者選取
	private void ShowExpList(final ExpUrlToken[] expList) {
		AlertDialog.Builder builder = new Builder(this);

		builder.setTitle("Choose an experiment");

		String[] expNames = null;
		if (expList == null) {

		} else {
			expNames = new String[expList.length];
			for (int i = 0; i < expList.length; i++) {
				expNames[i] = expList[i].mName;
			}

		}

		builder.setItems(expNames, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				mExpChoice = expList[which];
				mExid = mExpChoice.mId;

				final String expScript = mExpChoice.mScript;
				if (mIsDebug)
					Log.d(SettingString.TAG, expScript);

				String descriptionString = ExpUrlToken
						.getExpDescription(expScript);

				String strDialogTitle = getString(R.string.str_alert_title);

				CommonAlertDialog.showAlertDialog(mContext, strDialogTitle,
						descriptionString, true,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) // 確認後開始感測
							{
								PreferenceHelper
										.setPreference(mContext,
												PreferenceHelper.PREF_SCRIPT,
												expScript);
								PreferenceHelper.setPreference(mContext,
										PreferenceHelper.ID_CHECK, mExid);

								Time time = new Time();
								time.setToNow();

								PreferenceHelper.setPreference(mContext,
										PreferenceHelper.UPLOADED_TIME,
										time.toMillis(false));

								startRecording();
								dialog.dismiss();

							}

						});

			}
		});

		builder.setNegativeButton(R.string.str_no,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						updateUI(false);
						dialog.dismiss();
					}
				});

		builder.show();
	}

	private void startRecording() {

		updateUI(true);

		Log.v(SettingString.TAG, "Start Recording");
		Intent intent = new Intent();
		intent.setClass(this, LogService.class); // 轉換至後端LogService
		startService(intent);// 好像沒有Start service
		finish();

	}

	// 停止後端記錄，後端LogService轉換至PLPA_CLAP
	private void StopService() {
		try {
			updateUI(false);

			Intent i = new Intent(this, LogService.class);
			stopService(i);
		} catch (Exception e) {
			Log.e(SettingString.TAG, "Stop Service Error", e);
		}
	}

	private boolean isMyServiceRunning(Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {

			if (serviceClass.getName().equals(service.service.getClassName())) {
				Log.d(SettingString.TAG, "service is still running:"
						+ serviceClass.getName());
				return true;
			}
		}

		Log.d(SettingString.TAG, "service is not running");
		return false;
	}

	@Override
	public void processFinish(String[] urls, String result) {
		// TODO Auto-generated method stub

		if (urls.length == 1) {
			Log.d(SettingString.TAG, "process finish, url=" + urls[0]);

			String url = urls[0];

			if (mIsDebug)
				Log.d(SettingString.TAG, url);

			try {
				JSONObject urlResult = new JSONObject(result);
				if (urlResult.length() > 0) {
					if (ReadREST.WEBSERVICE_ID_URI.equals(url)) {
						mClicentDeviceID = urlResult
								.getString(PreferenceHelper.CLIENT_DEVICE_ID);
						mAuthCode = urlResult
								.getString(PreferenceHelper.AUTH_CODE);
						// ClicentDeviceID = "262";
						// AuthCode = "52bceea3e55a6e2fdee85b30efc8fa71";

						Log.v(SettingString.TAG, "ClicentDeviceID"
								+ mClicentDeviceID + "AuthCode" + mAuthCode);
						PreferenceHelper.setPreference(this,
								PreferenceHelper.CLIENT_DEVICE_ID,
								mClicentDeviceID);
						PreferenceHelper.setPreference(this,
								PreferenceHelper.AUTH_CODE, mAuthCode);

					} else if (String.format(ReadREST.WEBSERVICE_EXPLIST_URL,
							mDeviceOs, mDevice, mClicentDeviceID, mAuthCode)
							.equals(url)) {
						JSONArray experiments = urlResult
								.getJSONArray("experiment");
						int count = experiments.length();

						ExpUrlToken[] expList = new ExpUrlToken[count];

						for (int i = 0; i < experiments.length(); i++) {
							JSONObject experiment = experiments
									.getJSONObject(i);
							ExpUrlToken expInfo = new ExpUrlToken();

							expInfo.mId = experiment.getString("experimentId");
							expInfo.mName = experiment.getString("name");
							expInfo.mDescription = experiment
									.getString("description");
							expInfo.mScript = experiment.getString("script");

							expList[i] = expInfo;
						}

						ShowExpList(expList);
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();

				CommonAlertDialog.showOKAlertDialog(this, "Server連結錯誤，請嘗試重新連線");
			}

		}

	}

}
