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
	private Button baocun,quxiao;//取消和保存按钮
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
	
	class MyButton implements OnClickListener{//保存和取消监听
	
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.button02_01://保存---（写入数据到SQLite然后返回查询页面）
				
				shijian();//获取时间
				String BiaoT = biaoti.getText().toString();//得到--标题
				String Neirong = neirong.getText().toString();//得到--内容
				
				
				
				ContentValues content = new ContentValues();//写入SQLite
				content.put("NAME",BiaoT);
				content.put("TEXT",Neirong);
				content.put("TIME", time);
				
				DBHelper helper = new DBHelper(getApplicationContext());
				helper.insert(content);
				
				intent = new Intent(Add.this, NoteActivity.class);//返回到查询页面(ShardActivity02.java)
				startActivity(intent);
				Add.this.finish();
				break;
			case R.id.button02_02://取消---（直接返回到查询页面）
				intent = new Intent(Add.this, NoteActivity.class);//返回到查询页面(ShardActivity02.java)
				startActivity(intent);
				Add.this.finish();
				break;
			}
		}
	}
	
	/*
	 * 时间
	 */
	public void shijian(){
		calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);//年
		int month = calendar.get(Calendar.MONTH)+1;//月
		int day = calendar.get(Calendar.DAY_OF_MONTH);//日
		int hour = calendar.get(Calendar.HOUR_OF_DAY);//时
		int minute = calendar.get(Calendar.MINUTE);//分
		time = ""+year+"-"+month+"-"+day+" / "+hour+":"+minute;
	}
}
