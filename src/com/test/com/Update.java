package com.test.com;

import java.util.Calendar;

import com.openhelpter.DBHelper;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;




public class Update extends Activity {
	private EditText biaoti,neirong;
	
	Calendar calendar;
	String time;
	String name1,biaotiE,neirongE;
	Intent intent;
	
	private Button baocun,quxiao;//ȡ���ͱ��水ť
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity02_tianjia);
		DBHelper helper = new DBHelper(getApplicationContext());
		
		biaoti = (EditText)findViewById(R.id.editbiaoti);
		neirong = (EditText)findViewById(R.id.edit02_01);
		
		baocun = (Button)findViewById(R.id.button02_01);		
		baocun.setOnClickListener(new MyButton());
		
		quxiao = (Button)findViewById(R.id.button02_02);	
		quxiao.setOnClickListener(new MyButton());
		
		Intent intent = this.getIntent();	
		 name1 = intent.getStringExtra("Index");	
		Cursor cursor = helper.queryy(name1);
			
		//��ѯ����
		while(cursor.moveToNext()){  //�ж���һ���±��Ƿ�������
			biaotiE = cursor.getString(cursor.getColumnIndex("NAME"));
			neirongE = cursor.getString(cursor.getColumnIndex("TEXT"));
		}
		biaoti.setText(biaotiE);
		neirong.setText(neirongE);
	}
	class MyButton implements OnClickListener{//�����ȡ������
	
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.button02_01:
				DBHelper helper = new DBHelper(getApplicationContext());
				String BiaoT = biaoti.getText().toString();//�õ�--����
				String Neirong = neirong.getText().toString();//�õ�--����
				
				
				
				ContentValues values = new ContentValues();
				values.put("name", BiaoT);
				values.put("text", Neirong);
						
				helper.updateNote(values, name1);			
				intent = new Intent(Update.this, NoteActivity.class);//���ص���ѯҳ��(ShardActivity02.java)
				startActivity(intent);
				Update.this.finish();
				break;
			case R.id.button02_02://ȡ��---��ֱ�ӷ��ص���ѯҳ�棩
				intent = new Intent(Update.this, NoteActivity.class);//���ص���ѯҳ��(ShardActivity02.java)
				startActivity(intent);
				Update.this.finish();
				break;
			}
		}
	}
	
	/*
	 * ʱ��
	 */
	public void shijian(){
		calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);//��
		int month = calendar.get(Calendar.MONTH)+1;//��
		int day = calendar.get(Calendar.DAY_OF_MONTH);//��
		int hour = calendar.get(Calendar.HOUR_OF_DAY);//ʱ
		int minute = calendar.get(Calendar.MINUTE);//��
		time = ""+year+"-"+month+"-"+day+" / "+hour+":"+minute;
	}
}
