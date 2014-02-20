package com.dem.on;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.app.Activity;



public class setup extends Activity{
	
	private ListView list = null;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_activity);   
        InitListView();
    }
	public void InitListView(){
		
		list = (ListView) findViewById(R.id.ListViewUserData);
		list.setVerticalScrollBarEnabled(false);
ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		
		HashMap<String, Object> map = new HashMap<String, Object>();  
        map.put("ItemBigPicture", R.drawable.wenzhang);
        map.put("Itemwhenupload", "1小时前");  
        map.put("ItemUserName", "高富帅");  
        map.put("UserPeople", "3049587次使用"); 
        map.put("ItemTimeLong", "01：00"); 
        map.put("ItemTextContent", "这是一段测试的代码,这是一段测试的代码,这是一段测试的代码,这是一段测试的代码,这是一段测试的代码,这是一段测试的代码,这是一段测试的代码,这是一段测试的代码,这是一段测试的代码,这是一段测试的代码,"); 
        listItem.add(map);
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,
	            R.layout.userdatalistitem,  //listView对应的资源ID
	            new String[] {"ItemBigPicture","Itemwhenupload", "ItemUserName","UserPeople","ItemTimeLong","ItemTextContent"},     
	            new int[] {R.id.ItemBigPicture,R.id.Itemwhenupload,R.id.ItemUserName,R.id.UserPeople, R.id.ItemTimeLong, R.id.ItemTextContent}  
	        );
		//锟斤拷硬锟斤拷锟斤拷锟绞� 
        list.setAdapter(listItemAdapter);  
	}
}
