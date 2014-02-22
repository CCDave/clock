package com.dem.on;

import java.util.ArrayList;
import java.util.HashMap;

import com.dem.on.Clock.MyAdapter;
import com.dem.on.Clock.ViewHolder;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.app.Activity;
import android.content.Context;



public class setup extends Activity{
	
	private ListView list = null;
	private ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_activity);   
        InitListView();
    }
	public void InitListView(){
		
		list = (ListView) findViewById(R.id.ListViewUserData);
		list.setVerticalScrollBarEnabled(false);
		
		HashMap<String, Object> map = new HashMap<String, Object>();  
        map.put("ItemBigPicture_head", R.drawable.wenzhang);
        listItem.add(map);
        
        
        HashMap<String, Object> map1 = new HashMap<String, Object>();  
        map1.put("Itemtouxiang", R.drawable.gaoyuanyuan);
        map1.put("huangguan", R.drawable.wenzhang);
        map1.put("ItemBigPicture", R.drawable.gaoyuanyuan);
        map1.put("Itemusername", "谢娜");
        map1.put("Itemchenghao", "叫醒宗师");
        map1.put("Itemshijian", "2/22 13:50");
        
        map1.put("Itemleijijiaoxing_shu", "56312728");
        map1.put("Itemjiaoxinglv_shu", "91%");
        map1.put("ItemButtonYuyin", "语音");
        map1.put("ItemButtonDaoju", "道具");
       
        listItem.add(map1);
        
        
        MyAdapter listItemAdapter = new MyAdapter(this);

        list.setAdapter(listItemAdapter);  
        
        /*SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,
	            R.layout.userdatalistitem,  //listView对应的资源ID
	            new String[] {"ItemBigPicture","Itemwhenupload", "ItemUserName","UserPeople","ItemTimeLong","ItemTextContent"},     
	            new int[] {R.id.ItemBigPicture,R.id.Itemwhenupload,R.id.ItemUserName,R.id.UserPeople, R.id.ItemTimeLong, R.id.ItemTextContent}  
	        );
        
        list.setAdapter(listItemAdapter);*/  
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
             
        	if(position == 0){
        		ViewHolderHead holder = null;  
                if (convertView == null) { 
                    holder = new ViewHolderHead();    
                    //可以理解为从vlist获取view  之后把view返回给ListView  
                    convertView = mInflater.inflate(R.layout.userdatahead, null);  
                    
                    holder.ItemBigPicture_head = (ImageView)convertView.findViewById(R.id.ItemBigPicture_head);  
                    holder.ItemButtonPlayRecord_head = (Button)convertView.findViewById(R.id.ItemButtonPlayRecord_head);  
                    holder.ItemButtonLike_head = (Button)convertView.findViewById(R.id.ItemButtonLike_head);  
                    holder.ItemButtonListen_head = (Button)convertView.findViewById(R.id.ItemButtonListen_head);  
                    holder.ItemButtonTalk_head = (Button)convertView.findViewById(R.id.ItemButtonTalk_head); 
                    
                    convertView.setTag(holder);               
                }else {               
                    holder = (ViewHolderHead)convertView.getTag();  
                }         
                  
                holder.ItemBigPicture_head.setBackgroundResource((Integer)listItem.get(position).get("ItemBigPicture_head"));  
                
                return convertView;
        	}else{
        		
        		ViewHolder holder = null;  
                if (convertView == null) { 
                    holder = new ViewHolder();    
                    //可以理解为从vlist获取view  之后把view返回给ListView  
                    convertView = mInflater.inflate(R.layout.userdatalistitem, null);  
                    
                    holder.Itemtouxiang = (ImageButton)convertView.findViewById(R.id.Itemtouxiang);  
                    holder.huangguan = (ImageButton)convertView.findViewById(R.id.huangguan);  
                    holder.ItemBigPicture = (ImageView)convertView.findViewById(R.id.ItemBigPicture);  
                    
                    holder.Itemusername = (TextView)convertView.findViewById(R.id.Itemusername);  
                    holder.Itemchenghao = (TextView)convertView.findViewById(R.id.Itemchenghao); 
                    holder.Itemshijian = (TextView)convertView.findViewById(R.id.Itemshijian); 
                    
                    
                    holder.Itemleijijiaoxing_shu = (TextView)convertView.findViewById(R.id.Itemleijijiaoxing_shu);
                    holder.Itemjiaoxinglv_shu = (TextView)convertView.findViewById(R.id.Itemjiaoxinglv_shu);
                    holder.ItemButtonYuyin = (Button)convertView.findViewById(R.id.ItemButtonYuyin);
                    holder.ItemButtonDaoju = (Button)convertView.findViewById(R.id.ItemButtonDaoju);
                   
                    
                    convertView.setTag(holder);               
                }else {               
                    holder = (ViewHolder)convertView.getTag();  
                }         
                
               /**/ holder.Itemtouxiang.setBackgroundResource((Integer)listItem.get(position).get("Itemtouxiang"));
                holder.huangguan.setBackgroundResource((Integer)listItem.get(position).get("huangguan"));
                holder.ItemBigPicture.setBackgroundResource((Integer)listItem.get(position).get("ItemBigPicture"));
                
                holder.Itemusername.setText((String)listItem.get(position).get("Itemusername"));
                holder.Itemchenghao.setText((String)listItem.get(position).get("Itemchenghao"));
                holder.Itemshijian.setText((String)listItem.get(position).get("Itemshijian"));

                holder.Itemleijijiaoxing_shu.setText((String)listItem.get(position).get("Itemleijijiaoxing_shu"));
                holder.Itemjiaoxinglv_shu.setText((String)listItem.get(position).get("Itemjiaoxinglv_shu"));
                holder.ItemButtonYuyin.setText((String)listItem.get(position).get("ItemButtonYuyin"));
                holder.ItemButtonDaoju.setText((String)listItem.get(position).get("ItemButtonDaoju"));
                
                return convertView;
        	}
        }  
    }  
	
	public final class ViewHolder {   
        public ImageButton Itemtouxiang;  
        public ImageButton huangguan;  
        public ImageView ItemBigPicture;
        
        public TextView Itemusername;  
        public TextView Itemchenghao; 
        public TextView Itemshijian;
        
        public TextView Itemleijijiaoxing_shu;
        public TextView Itemjiaoxinglv_shu;
        public Button ItemButtonYuyin;
        public Button ItemButtonDaoju;
        
    }
	
	
	public final class ViewHolderHead {   
        public ImageView ItemBigPicture_head;  
        public Button ItemButtonPlayRecord_head;  
        public Button ItemButtonLike_head;  
        public Button ItemButtonListen_head; 
        public Button ItemButtonTalk_head;
    }
	
}
