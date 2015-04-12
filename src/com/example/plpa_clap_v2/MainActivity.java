package com.example.plpa_clap_v2;

import java.io.File;
import java.util.ArrayList;

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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pipa.item.ExpUrlToken;
import com.example.pipa.item.ExpItemAttribute;
import com.example.plpa.utils.CommonAlertDialog;
import com.example.plpa.utils.Connectivity;
import com.example.plpa.utils.DBHelper;
import com.example.plpa.utils.ReadREST;
import com.example.plpa.utils.ReadREST.AsyncResponse;
import com.example.plpa.utils.SettingString;

public class MainActivity extends Activity implements AsyncResponse {

	public static final String mTag = "Jim";
	public static final boolean mIsDebug = true;
	
	private Button mBtnLoadDSL;
	private Button mBtnStart;
	private Button mBtnEnd;
	private TextView mDSLTxtView;
	private Context mContext = this;

	private DBHelper mDbHelper = null;
	private ExpUrlToken mExpChoice;

	private String mClicentDeviceID;
	private String mAuthCode;
	private String mDeviceOs;
	private String mDevice;
	private String mTestDescript = "";
	private String mExid = "";

//	private ExpItemBase[] mExpAllItems;
//	private ArrayList<ExpItemBase> mExpImplimentItems;
//	
//	private void initExpItem() {
//		mExpAllItems = new ExpItemBase[] { new PhoneCallInfo() };
//		mExpImplimentItems = new ArrayList<ExpItemBase>();
//	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		loadClientId();
//		initExpItem();

		// ���o�ϥΪ̤�������MOS����
		mDeviceOs = android.os.Build.VERSION.RELEASE;
		mDevice = android.os.Build.MODEL.replace(' ', '-');

		// // ����]�w�ɧ�s
		// Bundle mBundle01 = this.getIntent().getExtras();
		// if (mBundle01 != null) {
		// mExid = mBundle01.getString(SettingString.ORIGIN_TEST_ID);
		// mContents = mBundle01.getString(SettingString.NEW_TEST_SCRIPT);
		// mTestDescript = mBundle01
		// .getString(SettingString.NEW_TEST_DESCRIPT);
		//
		// if (mExid != null) {
		// StopService();
		// Log.v(mTag, "refind TestId" + mExid);
		// reFindTest(mContents); // ���s�N�s����]�w�ɧ@���R
		// Log.v(mTag, "retrun PLPA_CLAP sucessful");
		// mDSLTxtView.setText(mTestDescript);
		// new AlertDialog.Builder(mContext)
		// .setMessage("���禳��s")
		// .setPositiveButton(R.string.update,
		// new DialogInterface.OnClickListener() { // �T�{��s
		// public void onClick(DialogInterface dialog,
		// int whichButton) {
		// testfileCheck(); // �R���¹���ѼơA���ͷs����Ѽ���
		// mDSLTxtView.setText(mTestDescript);
		// startRecording();
		// Log.v(mTag, "update sucessful");
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
		mDSLTxtView = (TextView) findViewById(R.id.DSLContents);

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

	// ���o������ID�M���v�X
	private void loadClientId() {
		// TODO Auto-generated method stub

		// ���ӥi�H��SharedPreferences���ˬd
		File f = new File(SettingString.XML_PATH);
		if (f.exists()) {
			Log.v(mTag, "SharedPreferences checkid : exist");
			SharedPreferences ckdevice = getSharedPreferences(
					SettingString.ID_CHECK, Context.MODE_PRIVATE);
			mClicentDeviceID = ckdevice.getString(
					SettingString.CLIENT_DEVICE_ID, "");
			mAuthCode = ckdevice.getString(SettingString.AUTH_CODE, "");

		} else {
			Log.v(mTag, "New checkId url:" + ReadREST.WEBSERVICE_ID_URI);
			ReadREST readREST = new ReadREST();
			readREST.execute(ReadREST.WEBSERVICE_ID_URI); // ������ID�M���v�XRESTFUL
			readREST.mAsyncDelegate = this;

		}

	}

	// �U���sĲ�o�ƥ�
	public void onClick(View v) {
		
		if(mClicentDeviceID == null || mClicentDeviceID.equals("")) {
			CommonAlertDialog.showOKAlertDialog(this, "Server�s�����~�A�й��խ��s�s�u"); 
			return;
		}
		
		switch (v.getId()) {
		case R.id.button_load:
			loadDSL(); // ���o����C��
			break;
		case R.id.button_start:
			// startRecording(); // ���s��_�_��
			updateUI(true);
			break;
		case R.id.button_end:
			StopService(); // ����O��
			updateUI(false);
			break;
		default:
			break;
		}
	}

	// �����ݰO���A���LogService�ഫ��PLPA_CLAP
	private void StopService() {
		// mChecktest = false;
		//
		// SharedPreferences settings = getSharedPreferences(
		// SettingString.PREF_FILENAME, Context.MODE_PRIVATE);
		// SharedPreferences.Editor editor = settings.edit();
		// editor.putBoolean(SettingString.CHECK_TEXT, mChecktest);

		try {
			// Intent i = new Intent(this, LogService.class);
			// stopService(i);
		} catch (Exception e) {
			Log.e(mTag, "Stop Service Error", e);
		}
	}

	// ���o����C��
	private void loadDSL() {
		if (Connectivity.isConnected(this)) { // �������L�������A
			openDatabase();
			loadExperimentList();
		} else { // �����S�������A�����}�Һ����\��
			CommonAlertDialog.showOKAlertDialog(this, "����K����A�ж}�ҵL�u�����\��");
		}
	}

	// �إ�SQLite��ƪ�
	private void openDatabase() {
		mDbHelper = new DBHelper(this);
		Log.v(mTag, "open db");
	}

	// ���o����C��
	private void loadExperimentList() {
		// REST
		String abc = String.format(ReadREST.WEBSERVICE_EXPLIST_URL, mDeviceOs,
				mDevice, mClicentDeviceID, mAuthCode);
		if(mIsDebug) Log.d(mTag, "Load Experiment Url:" + abc);
		ReadREST readREST = new ReadREST();
		readREST.execute(String.format(
				ReadREST.WEBSERVICE_EXPLIST_URL, mDeviceOs, mDevice,
				mClicentDeviceID, mAuthCode));
		readREST.mAsyncDelegate = this;
		
	}

	// �N����C��H���覡���Ѩ����̿��
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
				String descriptionString = "";
				final String expScript = mExpChoice.mScript;

				System.out.println("clicking DSL number = " + which);

				String[] temps2 = expScript.split(ExpUrlToken.TOKEN_UPLOAD_POLICY);
				String[] temps1 = temps2[0].split(ExpUrlToken.TOKEN_WHERE);

				String itemString = ((temps1.length > 0 ? temps1[0] : "")
						.replace("log ", "")).trim();
				String whereConditionString = (temps1.length > 1 ? temps1[1]
						: "").trim();
				String uploadPolicyString = (temps2.length > 1 ? temps2[1] : "")
						.trim();

				ArrayList<ExpItemAttribute> items = new ArrayList<ExpItemAttribute>();

				for (String item : itemString.split(",")) {
					String[] token = item.split(" ");
					if (token.length == 2) {
						ExpItemAttribute itemInfo = new ExpItemAttribute();
						itemInfo.mName = token[0];
						descriptionString += itemInfo.mName + " ";
						try {
							itemInfo.mNumber = Integer.valueOf(token[1]);

						} catch (Exception e) {

							// don't need interval
							itemInfo.mNumber = -1;
						}

						items.add(itemInfo);
					}

				}

				for (String where : whereConditionString.split(",")) {
					String[] token = where.split(" ");
					if (token.length == 3) {
						for (ExpItemAttribute name : items) {
							if (name.mName.equals(token[0])) {
								name.mWhereExpress = token[1];
								name.mWhereValue = token[2];
							}
						}
					}
				}

				final String[] uploadLimit = uploadPolicyString.split(",");
				final ArrayList<ExpItemAttribute> finalItems = (ArrayList<ExpItemAttribute>) items.clone();
				// String normalText = mContents; // �����ϥΪ̦�����O���ݩ�
				// Log.v(TAG,contents);
				String strDialogTitle = getString(R.string.str_alert_title);

				CommonAlertDialog.showAlertDialog(mContext, strDialogTitle, descriptionString, true,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) // �T�{��}�l�P��
							{
								startRecording(finalItems, uploadLimit);
								updateUI(true);
								dialog.dismiss();
								
								SharedPreferences.Editor editor = getSharedPreferences(SettingString.PREF_FILENAME, 
										Context.MODE_PRIVATE).edit();
								editor.putString(SettingString.PREF_SCRIPT, expScript);
								editor.commit();
							}

						});

				// String DSLString = mContents.replace(",", "").replace(" ",
				// "");

			}
		})
				.setNegativeButton(R.string.str_no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								updateUI(false);
								dialog.dismiss();
							}
						}).show();
	}

	private void startRecording(ArrayList<ExpItemAttribute> items, String[] uploadLimit) {

		Log.v(mTag, "startRecording");
		Intent intent = new Intent();

		intent.putExtra(LogService.INTENTKEY_EXP_ITEMS, (Parcelable)items);
		intent.putExtra(LogService.INTENTKEY_EXP_UPLOADTIME, uploadLimit);
		
		intent.setClass(this, LogService.class); // �ഫ�ܫ��LogService
		startService(intent);
		finish();
	}
	
	private boolean isMyServiceRunning(Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void processFinish(String[] urls, String result){
		// TODO Auto-generated method stub

		if (urls.length == 1) {
			Log.d(mTag, "process finish, url=" + urls[0]);
			
			String url = urls[0];
			
			if(mIsDebug) Log.d(mTag, url);
			
			try {
				JSONObject urlResult = new JSONObject(result);
				if (urlResult.length() > 0) {
					if (ReadREST.WEBSERVICE_ID_URI.equals(url)) {
						mClicentDeviceID = urlResult
								.getString(SettingString.CLIENT_DEVICE_ID);
						mAuthCode = urlResult.getString(SettingString.AUTH_CODE);
						// ClicentDeviceID = "262";
						// AuthCode = "52bceea3e55a6e2fdee85b30efc8fa71";

						Log.v(mTag, "ClicentDeviceID" + mClicentDeviceID
								+ "AuthCode" + mAuthCode);
						SharedPreferences.Editor editor = getSharedPreferences(
								SettingString.ID_CHECK, Context.MODE_PRIVATE)
								.edit(); // �N���o������ID�M���v�X�g�JXML��

						editor.putString(SettingString.CLIENT_DEVICE_ID,
								mClicentDeviceID);
						editor.putString(SettingString.AUTH_CODE, mAuthCode);
						editor.commit();
						
					} else if (String.format(ReadREST.WEBSERVICE_EXPLIST_URL, mDeviceOs,
							mDevice, mClicentDeviceID, mAuthCode).equals(url)) {
						JSONArray experiments = urlResult.getJSONArray("experiment");
						int count = experiments.length();

						ExpUrlToken[] expList = new ExpUrlToken[count];

						for (int i = 0; i < experiments.length(); i++) {
							JSONObject experiment = experiments.getJSONObject(i);
							ExpUrlToken expInfo = new ExpUrlToken();

							expInfo.mId = experiment.getString("experimentId");
							expInfo.mName = experiment.getString("name");
							expInfo.mDescription = experiment.getString("description");
							expInfo.mScript = experiment.getString("script");

							expList[i] = expInfo;
						}

						ShowExpList(expList);
					}
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
				
				CommonAlertDialog.showOKAlertDialog(this, "Server�s�����~�A�й��խ��s�s�u");
			}

		}

	}
}
