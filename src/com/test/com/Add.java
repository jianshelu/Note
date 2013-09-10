package com.test.com;

import java.util.Calendar;

import com.openhelpter.DBHelper;




import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;




public class Add extends Activity {
	private EditText biaoti,neirong;
	private Button baocun,quxiao;//ȡ���ͱ��水ť
	Calendar calendar;
	String time;
	Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity02_tianjia);
		biaoti = (EditText)findViewById(R.id.editbiaoti);
		neirong = (EditText)findViewById(R.id.edit02_01);
		baocun = (Button)findViewById(R.id.button02_01);
		
		baocun.setOnClickListener(new MyButton());
		quxiao = (Button)findViewById(R.id.button02_02);
		
		quxiao.setOnClickListener(new MyButton());
	}
	
	class MyButton implements OnClickListener{//�����ȡ������
	
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.button02_01://����---��д�����ݵ�SQLiteȻ�󷵻ز�ѯҳ�棩
				
				shijian();//��ȡʱ��
				String BiaoT = biaoti.getText().toString();//�õ�--����
				String Neirong = neirong.getText().toString();//�õ�--����
				
				
				
				ContentValues content = new ContentValues();//д��SQLite
				content.put("NAME",BiaoT);
				content.put("TEXT",Neirong);
				content.put("TIME", time);
				
				DBHelper helper = new DBHelper(getApplicationContext());
				helper.insert(content);
				
				intent = new Intent(Add.this, NoteActivity.class);//���ص���ѯҳ��(ShardActivity02.java)
				startActivity(intent);
				Add.this.finish();
				break;
			case R.id.button02_02://ȡ��---��ֱ�ӷ��ص���ѯҳ�棩
				intent = new Intent(Add.this, NoteActivity.class);//���ص���ѯҳ��(ShardActivity02.java)
				startActivity(intent);
				Add.this.finish();
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
