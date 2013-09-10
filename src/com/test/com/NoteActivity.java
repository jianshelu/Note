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
	String id,biaot,neirong,times;//��ѯ�����ֶ�
	Cursor cursor;
	String indexID;
	HashMap<String,Object> map;
	
	ArrayList<String> idList = new ArrayList<String>();
	
	//private TextView tv;
	
	final DBHelper helper = new DBHelper(this);
	
	private static final int ITEM1 = Menu.FIRST;
	
	//private int back = 0;//�жϰ�����back
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        list = (ListView)findViewById(R.id.ListViewAppend);//�õ�ListView�ؼ�       
        list.setOnItemClickListener(new ListOnItem());//ListView���������
		list.setOnItemLongClickListener(new ListOnItemLong());
		list.setOnCreateContextMenuListener(new ListOnCreate());//ListView����������
    	cursor = helper.query();//��ѯ����	
    	
	ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>>();//����ListView
		
		while(cursor.moveToNext()){  //�ж���һ���±��Ƿ�������
			id = cursor.getString(cursor.getColumnIndex("ID"));//ID
			biaot = cursor.getString(cursor.getColumnIndex("NAME"));//����
			times = cursor.getString(cursor.getColumnIndex("TIME"));//ʱ��

			idList.add(id);
			
			map = new HashMap<String,Object>();//����listview
			map.put("ItemTitle", biaot);
			map.put("ItemText", times);
			listItem.add(map);
		}
		SimpleAdapter listAdapter = new SimpleAdapter(NoteActivity.this,listItem, R.layout.activity02_01,new String[]{"ItemTitle","ItemText"},new int[]{R.id.ItemTitle,R.id.ItemText});
		list.setAdapter(listAdapter);//��ӵ�������������ʾ
    }
  
    /*-------------���Ǹ÷�����Ӳ˵���-------------*/
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
    
    
    

    
    class ListOnItem implements OnItemClickListener{//ListView���������
    	
		public void onItemClick(AdapterView<?> adapterView, View v, int position,long arg3) {//index��list�б�ѡ��Ԫ�ص��±꣬��0��ʼ
			indexID = idList.get(position);
			query();//�����
		}
	}
	class ListOnItemLong implements OnItemLongClickListener{//ListView����������
		
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			indexID = idList.get(position);
			return false;
		}
	}
	class ListOnCreate implements OnCreateContextMenuListener{//ListView���������������˵�
	
		public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
			menu.setHeaderTitle("����");
			menu.add(0,0,0,"��");
			menu.add(0,1,0,"ɾ��");
			menu.add(0,2,0,"�༭");
			menu.add(0,3,0,"����");
			menu.add(0,4,0,"��������");
			menu.add(0,5,0,"����");
		}
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {//���������˵���Ӧ����
		switch (item.getItemId()) {
		case 0:
			query();//�����˵���
			break;
		case 1://ɾ��
			
			delete();
			break;
		case 2://�༭
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
				// ��ǰ��ǩ������
				Cursor cursor = helper.queryy(indexID);
				
				if(cursor.moveToNext()){
				String strContent = cursor.getString(cursor.getColumnIndex("TEXT"));
				
				
				
				switch (item) {
				case 0:
					// ʹ�ö��ŷ���
					Uri smsToUri = Uri.parse("smsto:");// ��ϵ�˵�ַ
					Intent mIntent = new Intent(
							android.content.Intent.ACTION_SENDTO, smsToUri);
					// ���ŵ�����
					mIntent.putExtra("sms_body", strContent);// ���ŵ�����
					startActivity(mIntent);
					Toast.makeText(NoteActivity.this,
							"����" + items[item] + "������...", Toast.LENGTH_LONG)
							.show();
					break;
				case 1:
					// ʹ���ʼ�����(ģ�����޷�����,��֪�Ƿ����)
					Intent emailIntent = new Intent(
							android.content.Intent.ACTION_SEND);
					// �����ı���ʽ
					emailIntent.setType("text/plain");
					// ���öԷ��ʼ���ַ
					emailIntent
							.putExtra(android.content.Intent.EXTRA_EMAIL, "");
					// ���ñ�������
					emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
							"ͨ�����ױ�ǩ������Ϣ");
					// �����ʼ��ı�����
					emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
							strContent);
					startActivity(Intent.createChooser(emailIntent,
							"Choose Email Client"));
					Toast.makeText(NoteActivity.this,
							"����" + items[item] + "������...", Toast.LENGTH_LONG)
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
 
	//���������ļ�
	public void export(){
		
		Cursor cursor = helper.queryy(indexID);
		
		//��ѯ����
		if(cursor.moveToNext()){  //�ж���һ���±��Ƿ�������
			String title = cursor.getString(cursor.getColumnIndex("NAME"));
			String content = cursor.getString(cursor.getColumnIndex("TEXT"));
			
			FileService service = new FileService(getApplicationContext());
			try {
				//�ж�SDCard�Ƿ���ڣ����ҿ��Զ�д
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					service.saveToSDCard(title, content);
			
					Toast.makeText(getApplicationContext(), "�����ɹ�", 1).show();
				}else{
					Toast.makeText(getApplicationContext(), "����fail", 1).show();
				}				
			} 
					
			catch (Exception e) {
				Toast.makeText(getApplicationContext(),"����shibai", 1).show();
				e.printStackTrace();
			}
		}
		
		
	}
	//�鿴����
	public void query(){
		intent = new Intent(NoteActivity.this,Query.class);
		intent.putExtra("Index", indexID);
		startActivity(intent);
	}
	//ɾ������
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