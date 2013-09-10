package com.openhelpter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "coll.db";
	private static final String TBL_NAME = "CollTbl";
	
	
	
	
	
	public final static String CLOCK_TABLE_NAME = "clock";
	public final static String CLOCK_ID = "_id";
	public final static String CLOCK_ISOPEN = "isopen";
	public final static String CLOCK_DATE = "date";
	public final static String CLOCK_TIME = "time";
	public final static String CLOCK_ISREPEAT = "isrepeat";
	public final static String CLOCK_ISVABRATE = "isvabrate";
	public final static String CLOCK_RINGS = "rings";
	public final static String CLOCK_URI = "uri";
	
	
	private static final String ID = "id";
	public static final String FIELD_CONTENT = "TEXT";
	public static final String NOTETIME="TIME";
	public static final String NOTENAME="NAME";
	
	
	
	private static final String CREATE_TBL = " create table "
			+ " CollTbl(ID INTEGER PRIMARY KEY,"
			+""+NOTENAME+" text," 
			+""+FIELD_CONTENT+" text," 
			+""+NOTETIME+" text)";
	
	
	
	
	private static final String CREATE_CLOCK=
	 "create table "+CLOCK_TABLE_NAME+" ("
			+CLOCK_ID+" integer primary key, "
			+CLOCK_ISOPEN+" text, "
			+CLOCK_DATE+" text, "
			+CLOCK_TIME+" text, "
			+CLOCK_ISREPEAT+" text, "
			+CLOCK_ISVABRATE+" text, "
			+CLOCK_RINGS+" text, " 
			+CLOCK_URI+" text )";
	;
		
	private SQLiteDatabase db;

	public DBHelper(Context c) {
		super(c, DB_NAME, null, 2);
	}

	@Override
	/*--------创建表----------------------*/
	public void onCreate(SQLiteDatabase db) {
		this.db = db;
		
		
		
		db.execSQL(CREATE_TBL);
		db.execSQL(CREATE_CLOCK);
	}
	/*---------------记事本方法---------------*/
	//插入
	public void insert(ContentValues values) {
		SQLiteDatabase db = getWritableDatabase();
		db.insert(TBL_NAME, null, values);
		db.close();
	}
	//查询全部
	public Cursor query() {
		if (db == null)
			db = getWritableDatabase();
		
		Cursor c= db.query(TBL_NAME, new String[]{"ID","name","text","time"}, null, null, null, null, null);//查询数据	
		return c;
	}
	/*-----------查询单页--------------*/
	public Cursor queryy(String id) {
		SQLiteDatabase db =this. getWritableDatabase();
		String where = ID+"=?";
		String[] whereValues = {id};
		Cursor c= db.query(TBL_NAME, new String[]{"ID","NAME","TEXT","TIME"},  where, whereValues, null, null, null);
		return c;
	}
	//删除
	public void deleteNote(String id){
		if (db == null)
			db = getWritableDatabase();
		String where = ID+"=?";
		String[] whereValues = {id};
		db.delete(TBL_NAME, where, whereValues);
	}
	//更新
	public void updateNote(ContentValues values,String id){
		if (db == null)
			db = getWritableDatabase();
		String whereClause = ID+"=?";
		String[] whereargs = {id};
		db.update(TBL_NAME, values,whereClause,whereargs);		
	}
	public Cursor getNote(String id){
		SQLiteDatabase db = this.getReadableDatabase();
		String where = ID+"=?";
		String[] whereValues = {id};
		Cursor cursor = db.query(TBL_NAME, new String[]{"ID","NAME","TEXT","TIME"}, where, whereValues, null, null, null);
		return cursor;
	}
	
	
	
	
	/*-------闹听方法----------------*/
	public long insertClock(ContentValues cv){
		if (db == null)
			db = getWritableDatabase();
		return db.insert(CLOCK_TABLE_NAME, null, cv);
	}
	
	public void deleteClock(String id){
		if (db == null)
			db = getWritableDatabase();
		String where = CLOCK_ID+"=?";
		String[] whereValues = {id};
		db.delete(CLOCK_TABLE_NAME, where, whereValues);
	}
	
	public int updateClock(String id,ContentValues cv){
		if (db == null)
			db = getWritableDatabase();
		String where = CLOCK_ID+"=?";
		String[] whereValues = {id};
		return db.update(CLOCK_TABLE_NAME, cv, where, whereValues);
	}
	public Cursor getClock(String id){
		if (db == null)
			db = getWritableDatabase();
		String where = CLOCK_ID+"=?";
		String[] whereValues = {id};
		Cursor cursor = db.query(CLOCK_TABLE_NAME, null, where, whereValues, null, null, null);
		return cursor;
	}
	//关闭数据库
	public void close() {
		if (db != null)
			db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "drop table if exists "+TBL_NAME;
		db.execSQL(sql);
		
		 sql = "drop table if exists "+ CLOCK_TABLE_NAME;
		db.execSQL(sql);
	}
}
