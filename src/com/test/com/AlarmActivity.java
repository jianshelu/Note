package com.test.com;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



import com.adapter.AlarmSettingAdapter;
import com.openhelpter.DBHelper;
import com.util.Logger;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmActivity extends Activity {

	private ListView lv;
	private List<String> items;
	private List<String> values;
	private Calendar cal = Calendar.getInstance();
	private String id = "";
	private String isOpen = "";
	private String date = "";
	private String time = "";
	private String isRepeat = "";
	private String isVabrate = "";
	private String rings = "";
	private String rings_uri = "";
	private String[] yesOrNo;
	private DBHelper db;
	private AlarmManager am;
	private Logger log;
	private static String[] CURSOR_COLS = new String[]{
		MediaStore.Audio.Media._ID,
		MediaStore.Audio.Media.TITLE,
		MediaStore.Audio.Media.DISPLAY_NAME,
		MediaStore.Audio.Media.DATA,
		MediaStore.Audio.Media.ALBUM,
		MediaStore.Audio.Media.ARTIST,
		MediaStore.Audio.Media.ARTIST_ID,
		MediaStore.Audio.Media.DURATION,
		MediaStore.Audio.Media.TRACK
	};
	
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		setContentView(R.layout.alarm_layout);
		am = (AlarmManager)getSystemService(ALARM_SERVICE);
		log = new Logger(AlarmActivity.class);
		
		yesOrNo = new String[]{
			getResources().getString(R.string.yes),
			getResources().getString(R.string.no)
		};
		
		 db = new DBHelper(this);
		lv = (ListView)findViewById(R.id.alarm_listview);
		
		
		id = this.getIntent().getStringExtra("Index");
		
		
		Cursor cursor = db.getClock(id);
		if(cursor.moveToFirst()){
			isOpen = cursor.getString(cursor.getColumnIndex(DBHelper.CLOCK_ISOPEN));
			date = cursor.getString(cursor.getColumnIndex(DBHelper.CLOCK_DATE));
			time = cursor.getString(cursor.getColumnIndex(DBHelper.CLOCK_TIME));
			isRepeat = cursor.getString(cursor.getColumnIndex(DBHelper.CLOCK_ISREPEAT));
			isVabrate = cursor.getString(cursor.getColumnIndex(DBHelper.CLOCK_ISVABRATE));
			rings = cursor.getString(cursor.getColumnIndex(DBHelper.CLOCK_RINGS));
			rings_uri = cursor.getString(cursor.getColumnIndex(DBHelper.CLOCK_URI));
		}else{
			isOpen = getResources().getString(R.string.no);
			isRepeat = getResources().getString(R.string.no);
			isVabrate = getResources().getString(R.string.no);
		}
		setListItem();
		
		lv.setOnItemClickListener(new ListView.OnItemClickListener(){
			//@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				switch(position){
				case 0:
					setAlarmState();
					break;
				case 1:
					setDate();
					break;
				case 2:
					setTime();
					break;
				case 3:
					setRepeat();
					break;
				case 4:
					setVabrate();
					break;
				case 5:
					setRings();
					break;
				}
			}
			
		});
		
	}
	
	public void setListItem(){
		items = new ArrayList<String>();
		values = new ArrayList<String>();
		
		
		items.add(getResources().getString(R.string.start_alarm));
		values.add(isOpen);
		items.add(getResources().getString(R.string.alarm_date_setting));
		values.add(date);
		items.add(getResources().getString(R.string.alarm_time_setting));
		values.add(time);
		items.add(getResources().getString(R.string.alarm_is_repeat));
		values.add(isRepeat);
		items.add(getResources().getString(R.string.alarm_is_vibrate));
		values.add(isVabrate);
		items.add(getResources().getString(R.string.alarm_rings));
		values.add(rings);
		lv.setAdapter(new AlarmSettingAdapter(this,items,values));
		
	}
	
	private void setAlarmState(){
		OnClickListener listener = new DialogInterface.OnClickListener(){
			//@Override
			public void onClick(DialogInterface dialog, int which) {
				switch(which){
					case 0:
						openAlarm();
						break;
					case 1:
						closeAlarm();
						break;
				}
				saveChange();
			}
			
		};
		new AlertDialog.Builder(AlarmActivity.this)
			.setItems(yesOrNo, listener)
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
				//@Override
				public void onClick(DialogInterface dialog, int which) {
				}
				
			}).show();
	}
	
	private void openAlarm(){
		cal.setTimeInMillis(System.currentTimeMillis());
		if(!time.equals("")){
			String[] temp = time.split(":");
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(temp[0]));
			cal.set(Calendar.MINUTE, Integer.parseInt(temp[1]));
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			
			Intent intent = new Intent(AlarmActivity.this,CallAlarm.class);
			intent.putExtra(DBHelper.CLOCK_ID, id);
			PendingIntent pi = PendingIntent.getBroadcast(AlarmActivity.this, Integer.parseInt(id), intent, 0);
			am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
		}
		isOpen = yesOrNo[0];
		setListItem();
		
	}
	
	private void closeAlarm(){
		Intent intent = new Intent(AlarmActivity.this,CallAlarm.class);
		intent.putExtra(DBHelper.CLOCK_ID, id);
		PendingIntent pi = PendingIntent.getBroadcast(AlarmActivity.this, Integer.parseInt(id), intent, 0);
		am.cancel(pi);
		isOpen = yesOrNo[1];
		setListItem();
	}
	
	private void setDate(){
		cal.setTimeInMillis(System.currentTimeMillis());
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		
		new DatePickerDialog(AlarmActivity.this,new DatePickerDialog.OnDateSetListener(){
			//@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				date = year+"-"+format(monthOfYear+1)+"-"+format(dayOfMonth);
				saveChange();
				setListItem();
			}
			
		},year,month,day).show();
	}
	
	
	private void setTime(){
		cal.setTimeInMillis(System.currentTimeMillis());
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		new TimePickerDialog(AlarmActivity.this,new TimePickerDialog.OnTimeSetListener(){
			//@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				time = format(hourOfDay)+":"+format(minute);
				saveChange();
				if(isOpen.equals(yesOrNo[0])){
					openAlarm();
				}
				setListItem();
			}
			
		},hour,minute,true).show();
	}
	
	public void setRepeat(){
		OnClickListener listener = new DialogInterface.OnClickListener(){
			//@Override
			public void onClick(DialogInterface dialog, int which) {
				isRepeat = yesOrNo[which];
				if(isOpen.equals(yesOrNo[0])){
					openAlarm();
				}
				saveChange();
			}
			
		};
		new AlertDialog.Builder(AlarmActivity.this)
			.setItems(yesOrNo, listener)
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
				//@Override
				public void onClick(DialogInterface dialog, int which) {
				}
				
			}).show();
	}
	
	public void setVabrate(){
		OnClickListener listener = new DialogInterface.OnClickListener(){
			//@Override
			public void onClick(DialogInterface dialog, int which) {
				isVabrate = yesOrNo[which];
				saveChange();
			}
			
		};
		new AlertDialog.Builder(AlarmActivity.this)
			.setItems(yesOrNo, listener)
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
				//@Override
				public void onClick(DialogInterface dialog, int which) {
				}
				
			}).show();
	}
	
	public void setRings(){
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.setType("audio/*");
		this.startActivityForResult(intent, 0);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Uri uri = data!=null?data.getData():null;
		if(uri==null)return;
		String path = uri.toString();
		rings_uri = path;
		Cursor cursor = doQuery(uri);
		cursor.moveToFirst();
		rings = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
		saveChange();
		setListItem();
	}
	
	public String format(int i){
		String s = ""+i;
		if(s.length()==1) s = "0"+s;
		return s;
	}
	
	public void saveChange(){
		ContentValues cv = new ContentValues();
		cv.put(DBHelper.CLOCK_ISOPEN, isOpen);
		cv.put(DBHelper.CLOCK_DATE, date);
		cv.put(DBHelper.CLOCK_TIME, time);
		cv.put(DBHelper.CLOCK_ISREPEAT, isRepeat);
		cv.put(DBHelper.CLOCK_ISVABRATE, isVabrate);
		cv.put(DBHelper.CLOCK_RINGS, rings);
		cv.put(DBHelper.CLOCK_URI, rings_uri);
		if(!db.getClock(id).moveToFirst()){
			cv.put(DBHelper.CLOCK_ID, id);
			db.insertClock(cv);
		}else{
			db.updateClock(id, cv);
		}
	}
	
	public Cursor doQuery(Uri uri){
		return this.getContentResolver().query(uri, CURSOR_COLS, null, null, null);
	}
}
