package com.test.com;




import com.openhelpter.DBHelper;
import com.util.Logger;
import com.util.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

public class CallAlarm extends BroadcastReceiver {

	private Logger log;
	private DBHelper db;
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		db = new DBHelper(context);
		log = new Logger(CallAlarm.class);
		String id = intent.getStringExtra(DBHelper.CLOCK_ID);
		log.info("id==================="+id);
		Cursor cursor = db.getNote(id);
		Cursor cursor1 = db.getClock(id);
		
		
		if(!cursor.moveToFirst()||!cursor1.moveToFirst()) return;
		
		//String text = cursor.getString(cursor.getColumnIndex("TEXT"));
		String text = cursor.getString(cursor.getColumnIndex(DBHelper.FIELD_CONTENT));
		String uri = cursor1.getString(cursor1.getColumnIndex(DBHelper.CLOCK_URI));
		String isVabrate = cursor1.getString(cursor1.getColumnIndex(DBHelper.CLOCK_ISVABRATE));
		String date = cursor1.getString(cursor1.getColumnIndex(DBHelper.CLOCK_DATE));
		String time = cursor1.getString(cursor1.getColumnIndex(DBHelper.CLOCK_TIME));
		
		
		if(date!=null&&!date.equals("")){
			if(!date.equals(Util.getDateString()))return;
		}
		Intent i = new Intent(context,AlarmAlert.class);
		i.putExtra(DBHelper.CLOCK_ID, id);
		i.putExtra(DBHelper.FIELD_CONTENT, text);
		i.putExtra(DBHelper.CLOCK_ISVABRATE, isVabrate);
		i.putExtra(DBHelper.CLOCK_URI, uri);
		i.putExtra(DBHelper.CLOCK_TIME, time);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}

}
