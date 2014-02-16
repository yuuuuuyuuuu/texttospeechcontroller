package com.texttospeechcontroller;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class SentenceDbHelper extends ContentProvider {

	private SentenceDatabaseOpenHelper mDbOpenHelper = null;
	
	// SQL
	private static final String DB_NAME = "sentencelistdb";
	private static final String TABLE_NAME = "sentencelisttable2";
	private static final String CREATE_TABLE_SQL = "create table if not exists " + TABLE_NAME + " ( _id integer primary key autoincrement, sentence text , selected integer default 0);";
	private static final String DROP_TABLE_SQL = "drop table " + TABLE_NAME + ";";
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		
		SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
		int result = db.delete(TABLE_NAME, selection, selectionArgs);
		
		getContext().getContentResolver().notifyChange(uri, null);
		
		return result;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		
		SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        long result = db.insert(TABLE_NAME, null, values);
		
        if(-1 == result) return null;
        
        getContext().getContentResolver().notifyChange(uri, null);
        
		return uri;
	}

	@Override
	public boolean onCreate() {
		
		mDbOpenHelper = new SentenceDatabaseOpenHelper(getContext(), DB_NAME, null, 1);
		
		return false;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        
        db.execSQL(CREATE_TABLE_SQL);
        qb.setTables(TABLE_NAME);
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, null);
        
        c.setNotificationUri(getContext().getContentResolver(), uri);
        
		return c;
	}

	
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		
		SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
		int result = db.update(TABLE_NAME, values, selection, null);
		
		if(0 != result)
		{
			getContext().getContentResolver().notifyChange(uri, null);
		}
		
		return result;
	}
	
	
	private static class SentenceDatabaseOpenHelper extends SQLiteOpenHelper
	{
		
		public SentenceDatabaseOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(CREATE_TABLE_SQL);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL(DROP_TABLE_SQL);
			onCreate(db);
		}
		
	}
}
