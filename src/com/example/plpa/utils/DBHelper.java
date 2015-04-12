package com.example.plpa.utils;


import static android.provider.BaseColumns._ID;
import static com.example.plpa.utils.DbConstants.*;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
//import static com.example.plpa.project.LogService.delgroup;

public class DBHelper extends SQLiteOpenHelper {
	
	private final static String DATABASE_NAME = "clap.db";
	private final static int DATABASE_VERSION = 1;
	private static final String TAG = "Mytest";
	//private String ITEM;
	//private String ATTR;
	//private String ATTRVAL;
	
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		//context.deleteDatabase(DATABASE_NAME);
		
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		final String INIT_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
				  _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +								  
				  EXID + " TEXT, " +
				  ITEM  + " TEXT, " +
				  ATTR  + " TEXT, " +
				  ATTRVAL + " TEXT, " +
			//	  GROUP + " TEXT, " +
				  DEVICEID +" TEXT, " +
				  DATETIME + " CURRENT_TIME);"; 
		 db.execSQL(INIT_TABLE);
		 Log.v(TAG,INIT_TABLE);
		/*
		 *final String INIT_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
								  _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
								  ACCX + " TEXT, " +
								  ACCY + " TEXT, " +
								  ACCZ + " TEXT, " +
								  ORIX + " TEXT, " +
								  ORIY + " TEXT, " +
								  ORIZ + " TEXT, " +
								  MAGNX + " TEXT, " +
								  MAGNY + " TEXT, " +
								  MAGNZ + " TEXT, " +
								  PROX + " TEXT, " +
								  PRES + " TEXT, " +
								  LI + " TEXT, " +
								  TEMP + " TEXT, " +
								  RSSI + " TEXT, " +
								  SSID + " TEXT, " +
								  SCREEN + " TEXT, " +
								  EXTMEDIA + " TEXT, " +
								  LANGUAGE + " TEXT, " +
								  COUNTRY + " TEXT, " +
								  MSGFROM + " TEXT, " +
								  MSGBODY + " TEXT, " +
								  PKGNAME + " TEXT, " +
								  APKACT + " TEXT, " +
								  POWER + " TEXT, " +
								  CALLSTATE + " TEXT, " +
								  CONTACT + " TEXT, " +
								  NUMBER + " TEXT, " +
								  BROWSERTITLE + " TEXT, " +
								  BROWSERURL + " TEXT, " +								  								  
								  GPSLA + " TEXT, " +
								  GPSLO + " TEXT, " +
								  SPEED + " TEXT, " +
								  EXPID + " TEXT, " +
								  DATTIME + " CURRENT_TIME);"; 
 
		 * 
		 */
	}
	
	
	public void truncateDB(SQLiteDatabase db){
		final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
		db.execSQL(DROP_TABLE);
		onCreate(db);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
		db.execSQL(DROP_TABLE);
		//onCreate(db);
	}

	public void deleteTable(SQLiteDatabase db) {
		final String DEL_TABLE = "DELETE FROM " + TABLE_NAME;
		db.execSQL(DEL_TABLE);
		// TODO Auto-generated method stub
		
	}
	
	public void deleteGroup(SQLiteDatabase db, int delgroup) {
		final String DEL_TABLE = "DELETE FROM " + TABLE_NAME + " WHERE fgroup = " +delgroup + ";";
		db.execSQL(DEL_TABLE);
		Log.v(TAG,"deleteGroup"+String.valueOf(delgroup));
		// TODO Auto-generated method stub
		
	}

}


//origin
/*
_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
ACCX + " TEXT, " +
ACCY + " TEXT, " +
ACCZ + " TEXT, " +
ORIX + " TEXT, " +
ORIY + " TEXT, " +
ORIZ + " TEXT, " +
MAGNX + " TEXT, " +
MAGNY + " TEXT, " +
MAGNZ + " TEXT, " +
PROX + " TEXT, " +
PRES + " TEXT, " +
LI + " TEXT, " +
TEMP + " TEXT, " +
RSSI + " TEXT, " +
SSID + " TEXT, " +
SCREEN + " TEXT, " +
EXTMEDIA + " TEXT, " +
LANGUAGE + " TEXT, " +
COUNTRY + " TEXT, " +
MSGFROM + " TEXT, " +
MSGBODY + " TEXT, " +
PKGNAME + " TEXT, " +
APKACT + " TEXT, " +
POWER + " TEXT, " +
CALLSTATE + " TEXT, " +
CONTACT + " TEXT, " +
NUMBER + " TEXT, " +
BROWSERTITLE + " TEXT, " +
BROWSERURL + " TEXT, " +								  								  
GPSLA + " TEXT, " +
GPSLO + " TEXT, " +
SPEED + " TEXT, " +
EXPID + " TEXT, " +
DATTIME + " CURRENT_TIME);"      */