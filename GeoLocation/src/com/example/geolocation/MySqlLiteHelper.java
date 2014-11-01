package com.example.geolocation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqlLiteHelper extends SQLiteOpenHelper{
	
	private static final String DATABASE_NAME = "AddressData.db";
	private static final String TABLE_NAME_ADDRESS = "LocationData";
	private static final int DATABASE_VERSION = 1;
	
	private static final String ADDRESS = "AddressLine";
	private static final String LATITUDE = "Latitude";
	private static final String LONGITUDE = "Longitude";
	
	

	public MySqlLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String CREATE_ADDRESS_TABLE = "CREATE TABLE " +
				TABLE_NAME_ADDRESS + "("
	             + ADDRESS + " TEXT NOT NULL, " +  LATITUDE + " TEXT NOT NULL, " + LONGITUDE + " TEXT NOT NULL, " +  "PRIMARY KEY (" +  LATITUDE + "," + LONGITUDE +  ")" +  ")";
		System.out.println(CREATE_ADDRESS_TABLE);
	      db.execSQL(CREATE_ADDRESS_TABLE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ADDRESS);
	    onCreate(db);
		
	}
	
	public boolean existsAddress(String latitute, String longitude){
			
		
			String isExistQuery = "SELECT * FROM " + TABLE_NAME_ADDRESS + " WHERE " + LATITUDE + " = '" + latitute + "' AND " + LONGITUDE + " = '" + longitude + "'";
			System.out.println(isExistQuery);
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(isExistQuery, null);
			System.out.println(cursor.getCount());
			boolean isExist = true;
			if(cursor.getCount() == 0)
				isExist = false;
			return isExist;
			
		}


	public void addAddress(String AddressInfo, String latitude, String longitude){
		System.out.println("add address");
		
			ContentValues values = new ContentValues();
			values.put(ADDRESS, AddressInfo);
			values.put(LATITUDE, latitude);
			values.put(LONGITUDE, longitude);
			
			SQLiteDatabase db = this.getWritableDatabase();
			db.insert(TABLE_NAME_ADDRESS, null, values);
		    db.close();
			
		
	}
	
	
	
}
