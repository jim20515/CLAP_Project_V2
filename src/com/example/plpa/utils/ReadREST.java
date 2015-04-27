package com.example.plpa.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;

public class ReadREST extends AsyncTask<String, Void, String> {

	public static final String WEB_URI = "http://140.119.221.34";
	public static final String WEBSERVICE_ID_URI = WEB_URI + "/ws/rc/getid";
//	public static final String WEBSERVICE_ID_URI = "https://maps.googleapis.com/maps/api/js?v=3.exp&signed_in=true&libraries=places";
	public static final String WEBSERVICE_EXPLIST_URL = WEB_URI
			+ "/ws/le/getall/Android/%s/%s/%s/%s";

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
		StringBuffer out = new StringBuffer();
		try {
			int iUrlCount = strUrlFile.length;
			mReadURL = strUrlFile;
			
			for (int i = 0; i < iUrlCount; i++) {
				urlRESTLocation = new URL(strUrlFile[i]);
				connREST = (HttpURLConnection) urlRESTLocation.openConnection();
				connREST.setDoInput(true);

				InputStream in = connREST.getInputStream();
				out = new StringBuffer();

				byte[] buffer = new byte[4096];
				int n = 1;

				while ((n = in.read(buffer)) != -1) {
					out.append(new String(buffer, 0, n));
				}
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

		return out.toString();
	}
}
