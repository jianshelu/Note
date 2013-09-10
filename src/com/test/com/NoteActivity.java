package com.test.com;



import java.util.ArrayList;
import java.util.HashMap;


import com.openhelpter.DBHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;


public class NoteActivity extends Activity {
    /** Called when the activity is first created. */
	
	String title;
	String content;
	
	Intent intent;
	private ListView list;
	String id,biaot,neirong,times;//查询到的字段
	Cursor cursor;
	String indexID;
	HashMap<String,Object> map;
	
	ArrayList<String> idList = new ArrayList<String>();
	
	//private TextView tv;
	
	final DBHelper helper = new DBHelper(this);
	
	private static final int ITEM1 = Menu.FIRST;
	
	//private int back = 0;//判断按几次back
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        list = (ListView)findViewById(R.id.ListViewAppend);//拿到ListView控件       
        list.setOnItemClickListener(new ListOnItem());//ListView点击监听器
		list.setOnItemLongClickListener(new ListOnItemLong());
		list.setOnCreateContextMenuListener(new ListOnCreate());//ListView长按监听器
    	cursor = helper.query();//查询数据	
    	
	ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>>();//创建ListView
		
		while(cursor.moveToNext()){  //判断下一个下标是否有内容
			id = cursor.getString(cursor.getColumnIndex("ID"));//ID
			biaot = cursor.getString(cursor.getColumnIndex("NAME"));//标题
			times = cursor.getString(cursor.getColumnIndex("TIME"));//时间

			idList.add(id);
			
			map = new HashMap<String,Object>();//创建listview
			map.put("ItemTitle", biaot);
			map.put("ItemText", times);
			listItem.add(map);
		}
		SimpleAdapter listAdapter = new SimpleAdapter(NoteActivity.this,listItem, R.layout.activity02_01,new String[]{"ItemTitle","ItemText"},new int[]{R.id.ItemTitle,R.id.ItemText});
		list.setAdapter(listAdapter);//添加到适配器并且显示
    }
  
    /*-------------覆盖该方法添加菜单项-------------*/
    public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, ITEM1, 0, "Add");
	
		return true;
	}
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
		case ITEM1:
			intent = new Intent(NoteActivity.this, Add.class);
			startActivity(intent);
			NoteActivity.this.finish();
			break;
		
		}
    	return true;
    }
    
    
    

    
    class ListOnItem implements OnItemClickListener{//ListView点击监听器
    	
		public void onItemClick(AdapterView<?> adapterView, View v, int position,long arg3) {//index是list中被选中元素的下标，从0开始
			indexID = idList.get(position);
			query();//点击打开
		}
	}
	class ListOnItemLong implements OnItemLongClickListener{//ListView长按监听器
		
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			indexID = idList.get(position);
			return false;
		}
	}
	class ListOnCreate implements OnCreateContextMenuListener{//ListView长按监听器弹出菜单
	
		public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
			menu.setHeaderTitle("操作");
			menu.add(0,0,0,"打开");
			menu.add(0,1,0,"删除");
			menu.add(0,2,0,"编辑");
			menu.add(0,3,0,"导出");
			menu.add(0,4,0,"设置闹铃");
			menu.add(0,5,0,"分享");
		}
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {//长按弹出菜单响应函数
		switch (item.getItemId()) {
		case 0:
			query();//长按菜单打开
			break;
		case 1://删除
			
			delete();
			break;
		case 2://编辑
			intent = new Intent(NoteActivity.this,Update.class);
			intent.putExtra("Index", indexID);
			startActivity(intent);
			NoteActivity.this.finish();
			break;
		case 3:
			export();
			break;
		case 4:
			Intent intent = new Intent(this,AlarmActivity.class);
	    	intent.putExtra("Index", indexID);
	    	startActivity(intent);
			NoteActivity.this.finish();
			break;
		case 5:
			shareNote();
			break;
		}
		return super.onContextItemSelected(item);
	}
	private void shareNote() {
		final CharSequence[] items = {
				getResources().getString(R.string.share_with_sms),
				getResources().getString(R.string.share_with_email) };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				// 当前便签的内容
				Cursor cursor = helper.queryy(indexID);
				
				if(cursor.moveToNext()){
				String strContent = cursor.getString(cursor.getColumnIndex("TEXT"));
				
				
				
				switch (item) {
				case 0:
					// 使用短信分享
					Uri smsToUri = Uri.parse("smsto:");// 联系人地址
					Intent mIntent = new Intent(
							android.content.Intent.ACTION_SENDTO, smsToUri);
					// 短信的内容
					mIntent.putExtra("sms_body", strContent);// 短信的内容
					startActivity(mIntent);
					Toast.makeText(NoteActivity.this,
							"启动" + items[item] + "程序中...", Toast.LENGTH_LONG)
							.show();
					break;
				case 1:
					// 使用邮件分享(模拟器无法测试,不知是否可用)
					Intent emailIntent = new Intent(
							android.content.Intent.ACTION_SEND);
					// 设置文本格式
					emailIntent.setType("text/plain");
					// 设置对方邮件地址
					emailIntent
							.putExtra(android.content.Intent.EXTRA_EMAIL, "");
					// 设置标题内容
					emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
							"通过大米便签分享信息");
					// 设置邮件文本内容
					emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
							strContent);
					startActivity(Intent.createChooser(emailIntent,
							"Choose Email Client"));
					Toast.makeText(NoteActivity.this,
							"启动" + items[item] + "程序中...", Toast.LENGTH_LONG)
							.show();
					break;
				default:
					break;
				}
				}
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
 
	//导出单个文件
	public void export(){
		
		Cursor cursor = helper.queryy(indexID);
		
		//查询数据
		if(cursor.moveToNext()){  //判断下一个下标是否有内容
			String title = cursor.getString(cursor.getColumnIndex("NAME"));
			String content = cursor.getString(cursor.getColumnIndex("TEXT"));
			
			FileService service = new FileService(getApplicationContext());
			try {
				//判断SDCard是否存在，并且可以读写
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					service.saveToSDCard(title, content);
			
					Toast.makeText(getApplicationContext(), "导出成功", 1).show();
				}else{
					Toast.makeText(getApplicationContext(), "导出fail", 1).show();
				}				
			} 
					
			catch (Exception e) {
				Toast.makeText(getApplicationContext(),"导出shibai", 1).show();
				e.printStackTrace();
			}
		}
		
		
	}
	//查看、打开
	public void query(){
		intent = new Intent(NoteActivity.this,Query.class);
		intent.putExtra("Index", indexID);
		startActivity(intent);
	}
	//删除函数
	public void delete(){
    	new AlertDialog.Builder(this)
    	//	.setTitle(R.string.warn)
    		.setMessage(R.string.confirm_delete)
    		.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener(){
				//@Override
				public void onClick(DialogInterface dialog, int which) {
				//	String id = ids.get(position);
			    	helper.deleteNote(indexID);
			    	Toast.makeText(NoteActivity.this, R.string.delete_success, Toast.LENGTH_LONG);
			    	
			    	intent = new Intent(NoteActivity.this, NoteActivity.class);
					startActivity(intent);
					NoteActivity.this.finish();		    	
				}   			
    		})
    		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
				//@Override
				public void onClick(DialogInterface dialog, int which) {
				}    			
    		}).show();
    }

	
	
	
	
}