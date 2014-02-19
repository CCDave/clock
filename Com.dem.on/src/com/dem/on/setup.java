package com.dem.on;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.app.Activity;



public class setup extends Activity{
	
	private ListView list;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_activity);    
    }
	public void InitListView(){
		list = (ListView) findViewById(R.id.ListViewUserData);
ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		
		HashMap<String, Object> map = new HashMap<String, Object>();  
        map.put("ItemStarHeadImage", R.drawable.wenzhang);
        map.put("ItemClockName", "锟斤拷锟斤拷锟斤拷");  
        map.put("ItemClockTime", "07:30");  
        map.put("ItemClockData", "锟斤拷一锟斤拷锟斤拷锟斤拷"); 
        map.put("ItemClockLastTime", ""); 
        listItem.add(map);  
        //添加每个item的资源
        HashMap<String, Object> map2 = new HashMap<String, Object>();  
        map2.put("ItemStarHeadImage", R.drawable.gaoyuanyuan);
        map2.put("ItemClockName", "锟斤拷睡锟斤拷锟斤拷");  
        map2.put("ItemClockTime", "23:00");  
        map2.put("ItemClockData", "每锟斤拷"); 
        map2.put("ItemClockLastTime", "锟斤拷锟斤拷7小时30锟斤拷锟斤拷"); 
        listItem.add(map2);  
        
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,
	            R.layout.listitem,  //listView对应的资源ID
	            new String[] {"ItemStarHeadImage","ItemClockName", "ItemClockTime","ItemClockData","ItemClockLastTime"},   
	            //ImageItem锟斤拷XML锟侥硷拷锟斤拷锟斤拷锟揭伙拷锟絀mageView,锟斤拷锟斤拷TextView ID  
	            new int[] {R.id.ItemStarHeadImage,R.id.ItemClockName,R.id.ItemClockTime,R.id.ItemClockData, R.id.ItemClockLastTime}  
	        );
		
		//锟斤拷硬锟斤拷锟斤拷锟绞� 
        list.setAdapter(listItemAdapter);  
	}
}
