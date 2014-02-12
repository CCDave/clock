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
        map.put("ItemStarHeadImage", R.drawable.wenzhang);//ͼ����Դ��ID  
        map.put("ItemClockName", "������");  
        map.put("ItemClockTime", "07:30");  
        map.put("ItemClockData", "��һ������"); 
        map.put("ItemClockLastTime", ""); 
        listItem.add(map);  
        
        HashMap<String, Object> map2 = new HashMap<String, Object>();  
        map2.put("ItemStarHeadImage", R.drawable.gaoyuanyuan);//ͼ����Դ��ID  
        map2.put("ItemClockName", "��˯����");  
        map2.put("ItemClockTime", "23:00");  
        map2.put("ItemClockData", "ÿ��"); 
        map2.put("ItemClockLastTime", "����7Сʱ30����"); 
        listItem.add(map2);  
        
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//����Դ   
	            R.layout.listitem,//ListItem��XMLʵ��  
	            //��̬������ImageItem��Ӧ������          
	            new String[] {"ItemStarHeadImage","ItemClockName", "ItemClockTime","ItemClockData","ItemClockLastTime"},   
	            //ImageItem��XML�ļ������һ��ImageView,����TextView ID  
	            new int[] {R.id.ItemStarHeadImage,R.id.ItemClockName,R.id.ItemClockTime,R.id.ItemClockData, R.id.ItemClockLastTime}  
	        );
		
		//��Ӳ�����ʾ  
        list.setAdapter(listItemAdapter);  
          
        //��ӵ��  
        list.setOnItemClickListener(new OnItemClickListener() {  
  
            @Override  
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
                    long arg3) {  
                setTitle("�����"+arg2+"����Ŀ");  
            }  
        });  
        //��ӳ������  
        list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {  
            @Override  
            public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
                menu.setHeaderTitle("�����˵�-ContextMenu");     
                menu.add(0, 0, 0, "���������˵�0");  
                menu.add(0, 1, 0, "���������˵�1");     
            }  
        });    
       
	}
}
