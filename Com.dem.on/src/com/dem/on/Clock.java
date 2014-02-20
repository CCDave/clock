package com.dem.on;


import java.util.ArrayList;  
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.dem.on.R;


import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.view.ContextMenu;
import android.widget.AdapterView;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.Button;
import android.view.View.OnClickListener;

public class Clock extends Activity {

	private ListView list;
	private Button btAddClock;
	private ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
	MySQLiteWorker sql = null;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock_activity);   
        
        sql = new MySQLiteWorker(this);
        
        btAddClock = (Button)findViewById(R.id.ButtonAddClock);
        btAddClock.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
            	Intent OkPage = new Intent();
				OkPage.setClass(Clock.this, addclock.class);
			    Bundle bundle = new Bundle();
			    bundle.putInt("flag", 1);
			    bundle.putInt("updataid", 0);
			    OkPage.putExtras(bundle);
			    startActivityForResult(OkPage, 1);
            //装换视图切换到新的视图
            //Intent it = new Intent(Clock.this, addclock.class);
			//startActivity(it);
        	}
        });
        
        list = (ListView) findViewById(R.id.ListViewClock);
        list.setOnItemClickListener(new OnItemClickListener(){
        	@Override
        	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
        	long arg3) {
        		Log.i("$$$$$$$$$$$$$$$", "$$$$$$$$$$$$$$$$$");
        			}
        	});
        this.initMidListView();
    }
    
   
    
private void initMidListView() {
		
		
		
		listItem.clear();
		HashMap<String, Object> map = new HashMap<String, Object>();  
        map.put("ItemStarHeadImage", R.drawable.wenzhang);//图锟斤拷锟斤拷源锟斤拷ID  
        map.put("ItemClockName", "起床闹钟");  
        map.put("ItemClockTime", "07:30");  
        map.put("ItemClockData", "周一到周五"); 
        map.put("ItemClockLastTime", ""); 
        listItem.add(map);  
        
        HashMap<String, Object> map2 = new HashMap<String, Object>();  
        map2.put("ItemStarHeadImage", R.drawable.gaoyuanyuan);//图锟斤拷锟斤拷源锟斤拷ID  
        map2.put("ItemClockName", "早睡闹钟");  
        map2.put("ItemClockTime", "23:00");  
        map2.put("ItemClockData", "周日"); 
        map2.put("ItemClockLastTime", "还有七小时24分"); 
        listItem.add(map2);  
        
        //枚举数据库并添加消息
        SQLiteDatabase sqlbase =  sql.getDataBase();
		sql.EnumDataBase(MySQLiteOpenHelper.TABLE_NAME);
        Cursor cur = sqlbase.rawQuery("SELECT * FROM "
				+ MySQLiteOpenHelper.TABLE_NAME, null);
		if (cur != null) {
			//String name;
			//String time;
			Log.i("shujukushifougengxin", "****************************");
			while (cur.moveToNext()) {//直到返回false说明表中到了数据末尾
				
				HashMap<String, Object> maptmp = new HashMap<String, Object>(); 
				Log.i("shujukushifougengxin", cur.getString(5));
				//获取头像图标
				//cur.getString(12);
				maptmp.put("ItemStarHeadImage", R.drawable.gaoyuanyuan);//图锟斤拷锟斤拷源锟斤拷ID  
		        //获取闹铃名字
				Log.i("11111111111", cur.getString(5));
				maptmp.put("ItemClockName", cur.getString(5));  
		        //获取闹铃时间
				Log.i("2222222222", cur.getString(9));
				maptmp.put("ItemClockTime", cur.getString(9));  
		        //获取规律
				Log.i("33333333333333", cur.getString(6));
				maptmp.put("ItemClockData", cur.getString(6)); 
		        //计算剩余时间
				maptmp.put("ItemClockLastTime", "还有七小时24分"); 
				listItem.add(maptmp); 
			}
		}
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//锟斤拷锟皆�  
	            R.layout.listitem,//ListItem锟斤拷XML实锟斤拷  
	            //锟斤拷态锟斤拷锟斤拷锟斤拷ImageItem锟斤拷应锟斤拷锟斤拷锟斤拷          
	            new String[] {"ItemStarHeadImage","ItemClockName", "ItemClockTime","ItemClockData","ItemClockLastTime"},   
	            //ImageItem锟斤拷XML锟侥硷拷锟斤拷锟斤拷锟揭伙拷锟絀mageView,锟斤拷锟斤拷TextView ID  
	            new int[] {R.id.ItemStarHeadImage,R.id.ItemClockName,R.id.ItemClockTime,R.id.ItemClockData, R.id.ItemClockLastTime}  
	        );
		
		
        list.setAdapter(listItemAdapter);  
          
       
        
        
        
        
        //弹出菜单  
        list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {  
            @Override  
            public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
                menu.setHeaderTitle("锟斤拷锟斤拷锟剿碉拷-ContextMenu");     
                menu.add(0, 0, 0, "锟斤拷锟斤拷锟斤拷锟斤拷锟剿碉拷0");  
                menu.add(0, 1, 0, "锟斤拷锟斤拷锟斤拷锟斤拷锟剿碉拷1");     
            }  
        });    
       
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode)
		{
		case (1) :
		{
			if (resultCode == Activity.RESULT_OK)
			{
				Log.i("gengxinliebiao", "11111111111111111111");
				initMidListView();
			}
			break;
		}
	}
}
}
