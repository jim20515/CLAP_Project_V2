package com.example.plpa.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpException;

import android.os.AsyncTask;
import android.util.Log;

public class ReadREST extends AsyncTask<String, Void, String> {

//	public static final String WEB_URI = "http://192.168.43.85";
	public static final String WEB_URI = "http://140.119.163.68";
	public static final String WEBSERVICE_ID_URI = WEB_URI + "/admin/DeviceInfoes/PostDeviceInfo";
	public static final String PARAMETER_UUID = "UUID";
	
	public static final String WEBSERVICE_EXPLIST_URL = WEB_URI + "/admin/ExperimentApply/GetExperimentList";
//	public static final String WEBSERVICE_EXPDETAIL_URL = WEB_URI + "/admin/ExperimentApply/GetExperimentDetail";
	
	public static final String WEBSERVICE_EXPPOST_URL = WEB_URI + "/admin/Record/Post";
	public static final String PARAMETER_EXPPOST_VALUE = "value";
	
	public static final String PARAMETER_EXPID = "expId";
	
	public static final String JSON_EXPLIST_NAME = "Experiment";
	public static final String JSON_EXP_ID = "Id";
	public static final String JSON_EXP_TITLE = "Title";
	public static final String JSON_EXP_DESCRIPTION = "Description";
	public static final String JSON_EXP_DETAIL = "Detail";
	public static final String JSON_EXP_DATA = "Data";
//	public static final String JSON_EXP_MODIFYTIME = "ModifyTime";
	public static final String JSON_EXP_ITEMS = "Items";
	public static final String JSON_EXP_POLICIES = "Policies";
	
	private String[] mReadURL;
	
	public AsyncResponse mAsyncDelegate = null;
	public interface AsyncResponse {
		void processFinish(String[] urls, String result);
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPreExecute() {
		// before doing doInBackground, it will call this method
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(String result) {
		// after finishing doInBackground it will call this method
		
		mAsyncDelegate.processFinish(mReadURL, result);
		
		super.onPostExecute(result);
	}

	// String... strURLFile means the paramter can be empty or more as a array
	protected String doInBackground(String... strUrlFile) {

		URL urlRESTLocation = null;
		HttpURLConnection connREST = null;
		String response = "";
		
		try {
			mReadURL = strUrlFile;
			
			urlRESTLocation = new URL(strUrlFile[0]);
			connREST = (HttpURLConnection) urlRESTLocation.openConnection();
			connREST.setDoInput(true);
			connREST.setRequestMethod("POST");
			
			if(strUrlFile.length > 1) {
				OutputStream os = connREST.getOutputStream();
	            BufferedWriter writer = new BufferedWriter(
	                    new OutputStreamWriter(os, "UTF-8"));
	            writer.write(strUrlFile[1]);

	            writer.flush();
	            writer.close();
	            os.close();
			}
			
            int responseCode = connREST.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(connREST.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

                throw new HttpException(responseCode+"");
            }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if(SettingString.mIsDebug)Log.d(SettingString.TAG, "Get response:" + response);
		
		return response;
	}
	
	public static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
