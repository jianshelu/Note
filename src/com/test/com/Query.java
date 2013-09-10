package com.test.com;

import com.openhelpter.DBHelper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;



public class Query extends Activity {
	private TextView CX_biaoti,CX_neirong;
	String biaot,neirong;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query);
		
		CX_biaoti = (TextView)findViewById(R.id.CX_biaoti);
		CX_neirong = (TextView)findViewById(R.id.CX_neirong);
		Intent intent = this.getIntent();
		
		DBHelper helper = new DBHelper(this);
		
		String name1 = intent.getStringExtra("Index");	
		Cursor cursor = helper.queryy(name1);
		//查询数据
		while(cursor.moveToNext()){  //判断下一个下标是否有内容
			biaot = cursor.getString(cursor.getColumnIndex("NAME"));//标题
			neirong = cursor.getString(cursor.getColumnIndex("TEXT"));
		}
		CX_biaoti.setText(biaot);
		CX_neirong.setText(neirong);
	}

}
