package com.dem.on;


import java.util.ArrayList;  
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.dem.on.R;


import android.widget.ListView;
import android.widget.SimpleAdapter;
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
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock_activity);   
        
        btAddClock = (Button)findViewById(R.id.ButtonAddClock);
        btAddClock.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
            //装换视图切换到新的视图
            Intent it = new Intent(Clock.this, addclock.class);

			startActivity(it);
        	}
        });
        
        this.initMidListView();
    }
    
   
    
private void initMidListView() {
		
		list = (ListView) findViewById(R.id.ListViewClock);
		
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		
		HashMap<String, Object> map = new HashMap<String, Object>();  
        map.put("ItemStarHeadImage", R.drawable.wenzhang);//图锟斤拷锟斤拷源锟斤拷ID  
        map.put("ItemClockName", "锟斤拷锟斤拷锟斤拷");  
        map.put("ItemClockTime", "07:30");  
        map.put("ItemClockData", "锟斤拷一锟斤拷锟斤拷锟斤拷"); 
        map.put("ItemClockLastTime", ""); 
        listItem.add(map);  
        
        HashMap<String, Object> map2 = new HashMap<String, Object>();  
        map2.put("ItemStarHeadImage", R.drawable.gaoyuanyuan);//图锟斤拷锟斤拷源锟斤拷ID  
        map2.put("ItemClockName", "锟斤拷睡锟斤拷锟斤拷");  
        map2.put("ItemClockTime", "23:00");  
        map2.put("ItemClockData", "每锟斤拷"); 
        map2.put("ItemClockLastTime", "锟斤拷锟斤拷7小时30锟斤拷锟斤拷"); 
        listItem.add(map2);  
        
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//锟斤拷锟皆�  
	            R.layout.listitem,//ListItem锟斤拷XML实锟斤拷  
	            //锟斤拷态锟斤拷锟斤拷锟斤拷ImageItem锟斤拷应锟斤拷锟斤拷锟斤拷          
	            new String[] {"ItemStarHeadImage","ItemClockName", "ItemClockTime","ItemClockData","ItemClockLastTime"},   
	            //ImageItem锟斤拷XML锟侥硷拷锟斤拷锟斤拷锟揭伙拷锟絀mageView,锟斤拷锟斤拷TextView ID  
	            new int[] {R.id.ItemStarHeadImage,R.id.ItemClockName,R.id.ItemClockTime,R.id.ItemClockData, R.id.ItemClockLastTime}  
	        );
		
		//锟斤拷硬锟斤拷锟斤拷锟绞� 
        list.setAdapter(listItemAdapter);  
          
        //锟斤拷拥锟斤拷  
        list.setOnItemClickListener(new OnItemClickListener() {  
  
            @Override  
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
                    long arg3) {  
               //进入个人主页
            }  
        });
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
}
