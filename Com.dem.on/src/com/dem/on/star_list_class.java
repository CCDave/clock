package com.dem.on;



import java.util.ArrayList;  
import java.util.HashMap;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dem.on.R;
import com.dem.on.Clock.MyAdapter;
import com.dem.on.Clock.ViewHolder;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import android.view.ContextMenu;
import android.widget.AdapterView;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;


public class star_list_class extends Activity {

	private ListView list;
	ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.starts_list);    
        
        this.initMidListView();
    }
	public final class ViewHolder {   
        public TextView ItemPeopleCount;  
        public TextView ItemStarName; 
        public ImageView ItemStarHeadImage;
        public Button ButtonControl_onoff;
    }
	private void initMidListView() {
		
		list = (ListView) findViewById(R.id.ListStars);
		listItem.clear();

		HashMap<String, Object> map = new HashMap<String, Object>();  
        map.put("ItemStarHeadImage", R.drawable.fenlei);//图像资源的ID  
        map.put("ItemStarName","明星");
        map.put("ItemPeopleCount", "2568091");
        listItem.add(map);  
        
        HashMap<String, Object> map2 = new HashMap<String, Object>();  
        map2.put("ItemStarHeadImage", R.drawable.fenlei);//图像资源的ID  
        map2.put("ItemStarName", "自媒体");
        map2.put("ItemPeopleCount", "2147483647");
        listItem.add(map2);  
        
        HashMap<String, Object> map3 = new HashMap<String, Object>();  
        map3.put("ItemStarHeadImage", R.drawable.fenlei);//图像资源的ID  
        map3.put("ItemStarName","资讯");
        map3.put("ItemPeopleCount", "2568091");
        listItem.add(map3);  
        
        HashMap<String, Object> map4 = new HashMap<String, Object>();  
        map4.put("ItemStarHeadImage", R.drawable.fenlei);//图像资源的ID  
        map4.put("ItemStarName","娱乐");
        map4.put("ItemPeopleCount", "2568091");
        listItem.add(map4);  
        
        HashMap<String, Object> map5 = new HashMap<String, Object>();  
        map5.put("ItemStarHeadImage", R.drawable.fenlei);//图像资源的ID  
        map5.put("ItemStarName","其他");
        map5.put("ItemPeopleCount", "2568091");
        listItem.add(map5);  
      
        
        MyAdapter listItemAdapter = new MyAdapter(this);

        list.setAdapter(listItemAdapter);  
          
        //添加点击  
       list.setOnItemClickListener(new OnItemClickListener() {  
  
            @Override  
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
                    long arg3) {  
            	String strName = (String)listItem.get(arg2).get("ItemStarName");
            	Toast.makeText(star_list_class.this, "转向"+strName+"的主页", Toast.LENGTH_SHORT).show();
            }  
        });  
        
        
	}
	public class MyAdapter extends BaseAdapter {  
		  
        private LayoutInflater mInflater;  
  
        public MyAdapter(Context context) {  
            this.mInflater = LayoutInflater.from(context);  
        }  
  
        @Override  
        public int getCount() {  
            // TODO Auto-generated method stub  
            return listItem.size();  
        }  
  
        @Override  
        public Object getItem(int position) {  
            // TODO Auto-generated method stub  
            return null;  
        }  
  
        @Override  
        public long getItemId(int position) {  
            // TODO Auto-generated method stub  
            return 0;  
        }  
        //****************************************final方法  
        //注意原本getView方法中的int position变量是非final的，现在改为final  
        @Override  
        public View getView(final int position, View convertView, ViewGroup parent) {  
             ViewHolder holder = null;  
            if (convertView == null) {  
                  
                holder=new ViewHolder();    
                //可以理解为从vlist获取view  之后把view返回给ListView  
                convertView = mInflater.inflate(R.layout.stars_list_class, null);  
                
                holder.ItemPeopleCount = (TextView)convertView.findViewById(R.id.ItemPeopleCount);  
                holder.ItemStarName = (TextView)convertView.findViewById(R.id.ItemStarName);  
                holder.ItemStarHeadImage = (ImageView)convertView.findViewById(R.id.ItemStarHeadImage); 
                holder.ButtonControl_onoff = (Button)convertView.findViewById(R.id.ButtonControl_onoff);  
                
                convertView.setTag(holder);               
            }else {               
                holder = (ViewHolder)convertView.getTag();  
            }         
              
            holder.ItemPeopleCount.setText((String)listItem.get(position).get("ItemPeopleCount"));  
            holder.ItemStarName.setText((String)listItem.get(position).get("ItemStarName"));  
            
            holder.ItemStarHeadImage.setBackgroundResource((Integer)listItem.get(position).get("ItemStarHeadImage"));
            
            holder.ButtonControl_onoff.setOnClickListener(new OnClickListener() {
            	
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(star_list_class.this, "已添加到提醒人列表！", Toast.LENGTH_SHORT).show();
				}
			});
           
           
            return convertView;  
        }  
    }  
	
}
