package com.example.plpa.utils;

import static android.provider.BaseColumns._ID;
//import static com.example.plpa.utils.DbConstants.UUID;
import static com.example.plpa.utils.DbConstants.ATTR;
import static com.example.plpa.utils.DbConstants.ATTRVAL;
import static com.example.plpa.utils.DbConstants.DATETIME;
//import static com.example.plpa.utils.DbConstants.DEVICEID;
//import static com.example.plpa.utils.DbConstants.EXID;
import static com.example.plpa.utils.DbConstants.ITEM;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	private final static String DATABASE_NAME = "clap.db";
	private static final String TABLE_NAME = "clap";
	
	private final static int DATABASE_VERSION = 1;
	private static final String mTag = SettingString.TAG;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		final String INIT_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + _ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ ITEM + " TEXT, " + ATTR + " TEXT, " + ATTRVAL + " TEXT, "
				+ DATETIME + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";
		db.execSQL(INIT_TABLE);

		Log.v(mTag, INIT_TABLE);

	}

	public void truncateDB(SQLiteDatabase db) {
		final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
		db.execSQL(DROP_TABLE);
		onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
		db.execSQL(DROP_TABLE);
		// onCreate(db);
	}

	public void deleteTable() {
		SQLiteDatabase db = this.getWritableDatabase();
		
		final String DEL_TABLE = "DELETE FROM " + TABLE_NAME;
		db.execSQL(DEL_TABLE);
		// TODO Auto-generated method stub

	}

	// Insert record into the database
    public void addRecord(DbConstants record) {
        // Open database connection
        SQLiteDatabase db = this.getWritableDatabase();
        // Define values for each field
        ContentValues values = new ContentValues();
        values.put(ITEM, record.getItem()); 
        values.put(ATTR, record.getAttr());
        values.put(ATTRVAL, record.getAttrval());
        if(record.getDateTime() != null) values.put(DATETIME, record.getDateTime());
        
        db.insertOrThrow(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }
    
    public int getRecordCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        //jim
        db.close();
        // return count
        return cursor.getCount();
    }
    
    public List<DbConstants> getAllTodoItems() {
        List<DbConstants> records = new ArrayList<DbConstants>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	DbConstants item = new DbConstants();
                item.setId(cursor.getInt(cursor.getColumnIndex(BaseColumns._ID)));
//                item.setUuid(cursor.getString(cursor.getColumnIndex(UUID)));
                item.setAttr(cursor.getString(cursor.getColumnIndex(ATTR)));
                item.setAttrval(cursor.getString(cursor.getColumnIndex(ATTRVAL)));
                item.setDateTime(cursor.getString(cursor.getColumnIndex(DATETIME)));
//                item.setExId(cursor.getString(cursor.getColumnIndex(EXID)));
//                item.setDeviceId(cursor.getString(cursor.getColumnIndex(DEVICEID)));
                item.setItem(cursor.getString(cursor.getColumnIndex(ITEM)));
                // Adding todo item to list
                records.add(item);
            } while (cursor.moveToNext());
        }

        //jim
        cursor.close();
        db.close();
        
        // return todo list
        return records;
    }
}
