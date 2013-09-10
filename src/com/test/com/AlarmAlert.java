package com.test.com;

import java.io.File;
import java.io.IOException;

import com.openhelpter.DBHelper;
import com.util.Logger;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.widget.Toast;

public class AlarmAlert extends Activity {

	private MediaPlayer mp;
	private DBHelper db;
	private Logger log;
	private Vibrator v;
	
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		db = new DBHelper(this);
		log = new Logger(AlarmAlert.class);
		
		Intent intent = getIntent();
		String id = intent.getStringExtra(DBHelper.CLOCK_ID);
		String text = intent.getStringExtra(DBHelper.FIELD_CONTENT);
		String isVabrate = intent.getStringExtra(DBHelper.CLOCK_ISVABRATE);
		String rings = intent.getStringExtra(DBHelper.CLOCK_URI);
		String time = intent.getStringExtra(DBHelper.CLOCK_TIME);
		//ContentValues cv = new ContentValues();
		if(mp!=null)return;
		mp = new MediaPlayer();
		File file = new File(text);
		try {
			if(rings.equals("")&&!file.exists()){
				mp.setDataSource(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
			}else{
				/*cv.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
				cv.put(MediaStore.MediaColumns.TITLE, file.getName());
				cv.put(MediaStore.MediaColumns.SIZE, file.length());
				cv.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
				cv.put(MediaStore.Audio.Media.IS_RINGTONE, false);  
				cv.put(MediaStore.Audio.Media.IS_NOTIFICATION, false); 
				cv.put(MediaStore.Audio.Media.IS_ALARM, true);   
				cv.put(MediaStore.Audio.Media.IS_MUSIC, false);  
				Uri uri = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());
				Uri newUri = this.getContentResolver().insert(uri, cv);
				RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM, newUri);*/
				mp.setDataSource(AlarmAlert.this, Uri.parse(rings));
			}
			
			AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
			new AlertDialog.Builder(this)
			.setTitle(time)
			.setMessage(text)
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
				//@Override
				public void onClick(DialogInterface dialog, int which) {
					mp.stop();
					v.cancel();
					finish();
				}
				
			}).show();
			if(isVabrate.equals(getResources().getString(R.string.yes))){
				v.vibrate(new long[]{1000,3000,500,2000}, 2);
			}
			if(am.getStreamVolume(AudioManager.STREAM_ALARM)!=0){	
				mp.setAudioStreamType(AudioManager.STREAM_ALARM);
				mp.setLooping(true);
				mp.prepare();
				mp.start();
			}
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
			mp.stop();
		}
		super.onConfigurationChanged(newConfig);
	}
	
	
}
