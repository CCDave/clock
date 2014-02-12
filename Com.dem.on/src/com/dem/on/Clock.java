package com.dem.on;


import java.util.ArrayList;  
import java.util.HashMap;

import android.app.Activity;
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




public class Clock extends Activity {

	private ListView list;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock_activity);    
        
        this.initMidListView();
    }
    
   
    
private void initMidListView() {
		
		list = (ListView) findViewById(R.id.ListViewClock);
		
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		
		HashMap<String, Object> map = new HashMap<String, Object>();  
        map.put("ItemStarHeadImage", R.drawable.wenzhang);//图像资源的ID  
        map.put("ItemClockName", "起床闹钟");  
        map.put("ItemClockTime", "07:30");  
        map.put("ItemClockData", "周一到周五"); 
        map.put("ItemClockLastTime", ""); 
        listItem.add(map);  
        
        HashMap<String, Object> map2 = new HashMap<String, Object>();  
        map2.put("ItemStarHeadImage", R.drawable.gaoyuanyuan);//图像资源的ID  
        map2.put("ItemClockName", "早睡闹钟");  
        map2.put("ItemClockTime", "23:00");  
        map2.put("ItemClockData", "每天"); 
        map2.put("ItemClockLastTime", "还有7小时30分钟"); 
        listItem.add(map2);  
        
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//数据源   
	            R.layout.listitem,//ListItem的XML实现  
	            //动态数组与ImageItem对应的子项          
	            new String[] {"ItemStarHeadImage","ItemClockName", "ItemClockTime","ItemClockData","ItemClockLastTime"},   
	            //ImageItem的XML文件里面的一个ImageView,两个TextView ID  
	            new int[] {R.id.ItemStarHeadImage,R.id.ItemClockName,R.id.ItemClockTime,R.id.ItemClockData, R.id.ItemClockLastTime}  
	        );
		
		//添加并且显示  
        list.setAdapter(listItemAdapter);  
          
        //添加点击  
        list.setOnItemClickListener(new OnItemClickListener() {  
  
            @Override  
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
                    long arg3) {  
                setTitle("点击第"+arg2+"个项目");  
            }  
        });  
        //添加长按点击  
        list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {  
            @Override  
            public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
                menu.setHeaderTitle("长按菜单-ContextMenu");     
                menu.add(0, 0, 0, "弹出长按菜单0");  
                menu.add(0, 1, 0, "弹出长按菜单1");     
            }  
        });    
       
	}
}
