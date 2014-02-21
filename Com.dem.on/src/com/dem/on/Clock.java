package com.dem.on;


import java.io.File;
import java.util.ArrayList;  
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.dem.on.R;
import com.dem.on.SoundControl;

import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import android.widget.AdapterView.OnItemClickListener;
import android.view.ContextMenu;
import android.widget.AdapterView;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View.OnClickListener;

public class Clock extends Activity {

	private ListView list;
	private Button btAddClock;
	private int nPlayMusic = -1;
	
	private Calendar calendar;
	
	private List<String> myClockId = new ArrayList<String>();
	private int listItemPosition = 0;

	private ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
	private MySQLiteWorker sql = null;
	
	private SoundControl  sc = null;
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock_activity);   
        
        sql = new MySQLiteWorker(this);
        calendar = Calendar.getInstance();
        btAddClock = (Button)findViewById(R.id.ButtonAddClock);
        btAddClock.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
            	//装换视图切换到新的视图
            	Intent OkPage = new Intent();
				OkPage.setClass(Clock.this, addclock.class);
			    Bundle bundle = new Bundle();
			    bundle.putInt("flag", 1);
			    bundle.putInt("updataid", 0);
			    OkPage.putExtras(bundle);
			    startActivityForResult(OkPage, 1);
        	}
        });
            
        list = (ListView) findViewById(R.id.ListViewClock);
       
        this.initMidListView();
    }
    
   
    
private void initMidListView() {
		
		myClockId.clear();
		listItem.clear();
		HashMap<String, Object> map = new HashMap<String, Object>();  
        map.put("ItemStarHeadImage", R.drawable.wenzhang);
        map.put("ItemClockName", "起床闹钟");  
        map.put("ItemClockTime", "07:30");  
        map.put("ItemClockData", "周一到周五"); 
        map.put("ItemClockLastTime", ""); 
        map.put("ButtonControl_onoff", "0"); 
        listItem.add(map);  
        
        HashMap<String, Object> map2 = new HashMap<String, Object>();  
        map2.put("ItemStarHeadImage", R.drawable.gaoyuanyuan);
        map2.put("ItemClockName", "早睡闹钟");  
        map2.put("ItemClockTime", "23:00");  
        map2.put("ItemClockData", "周日"); 
        map2.put("ItemClockLastTime", "还有七小时24分"); 
        map2.put("ButtonControl_onoff", "0"); 
        listItem.add(map2);  
        myClockId.add("1");
        myClockId.add("2");
        
        //枚举数据库并添加消息
        SQLiteDatabase sqlbase =  sql.getDataBase();
		sql.EnumDataBase(MySQLiteOpenHelper.TABLE_NAME);
        Cursor cur = sqlbase.rawQuery("SELECT * FROM "
				+ MySQLiteOpenHelper.TABLE_NAME, null);
		if (cur != null) {
			
			Log.i("shujukushifougengxin", "****************************");
			while (cur.moveToNext()) {//直到返回false说明表中到了数据末尾
				
				HashMap<String, Object> maptmp = new HashMap<String, Object>(); 
				Log.i("shujukushifougengxin", cur.getString(5));
				//获取头像图标
				//cur.getString(12);
				myClockId.add(cur.getString(0));
				maptmp.put("ItemStarHeadImage", R.drawable.default_clock);
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
				Log.i("444444444444444444", cur.getString(2));
				maptmp.put("ButtonControl_onoff", cur.getString(2));
				
				listItem.add(maptmp); 
			}
		}
		/*SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//锟斤拷锟皆�  
	            R.layout.listitem,      
	            new String[] {"ItemStarHeadImage","ItemClockName", "ItemClockTime","ItemClockData","ItemClockLastTime"},    
	            new int[] {R.id.ItemStarHeadImage,R.id.ItemClockName,R.id.ItemClockTime,R.id.ItemClockData, R.id.ItemClockLastTime}  
	        );*/
		MyAdapter listItemAdapter = new MyAdapter(this);

        list.setAdapter(listItemAdapter);  
        
        list.setOnItemLongClickListener(new OnItemLongClickListener() {
        	@Override
        	public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
        			int arg2, long arg3) {
        		listItemPosition = arg2;
        		Log.i("$$$$$$$$$$$$$$$", ""+arg2);
        		Log.i("$$$$$$$$$$$$$$$", ""+arg3);
        			return false;
        			}
        });
        
        list.setOnItemClickListener(new OnItemClickListener(){
        	@Override
        	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
        	long arg3) {
        		Log.i("$$$$$$$$$$$$$$$", ""+arg2);
        		listItemPosition = arg2;
        		ChangeClock(listItemPosition);
        		}
        	});
            
        //弹出菜单  
        list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {  
            @Override  
            public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
                menu.add(0, 0, 0, "删除");  
                menu.add(0, 1, 0, "编辑");     
            }  
        });    
	}

	@Override
	public boolean onContextItemSelected(MenuItem menu){
		
		Log.i("$$$$$$$$$$$$$$$", "$$$$$$$$$$$$$$$$$");
		try{
			switch(menu.getItemId()){
			case 0:
				//删除这一项
				DeleteClock(listItemPosition);
				break;
			case 1:
				//编辑这一项
				ChangeClock(listItemPosition);
				break;
			default:
				break;
			}
		}catch(Exception e){}
		
		return super.onContextItemSelected(menu);
	}
	
	public void DeleteClock(int index)
	{
		String ID = myClockId.get(index);
		sql.DeleteData(MySQLiteOpenHelper.TABLE_NAME, Integer.valueOf(ID).intValue());
		initMidListView();
	}
	
	public void ChangeClock(int index){
		String ID = myClockId.get(index);
		//装换视图切换到新的视图
    	Intent OkPage = new Intent();
		OkPage.setClass(Clock.this, addclock.class);
	    Bundle bundle = new Bundle();
	    bundle.putInt("flag", 2);
	    bundle.putInt("updataid", Integer.valueOf(ID).intValue());
	    OkPage.putExtras(bundle);
	    startActivityForResult(OkPage, 2);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
		case (1) :{
			if (resultCode == Activity.RESULT_OK)
			{
				initMidListView();
			}
			break;
		}
		case (2) :{
			if (resultCode == Activity.RESULT_OK)
			{
				initMidListView();
			}
			break;
		}
		}
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
                convertView = mInflater.inflate(R.layout.listitem, null);  
                
                holder.ItemClockName = (TextView)convertView.findViewById(R.id.ItemClockName);  
                holder.ItemClockTime = (TextView)convertView.findViewById(R.id.ItemClockTime);  
                holder.ItemClockData = (TextView)convertView.findViewById(R.id.ItemClockData);  
                holder.ItemClockLastTime = (TextView)convertView.findViewById(R.id.ItemClockLastTime);  
                holder.ItemStarHeadImage = (ImageView)convertView.findViewById(R.id.ItemStarHeadImage); 
                holder.ButtonControl_onoff = (ToggleButton)convertView.findViewById(R.id.ButtonControl_onoff);  
                
                convertView.setTag(holder);               
            }else {               
                holder = (ViewHolder)convertView.getTag();  
            }         
              
            holder.ItemClockName.setText((String)listItem.get(position).get("ItemClockName"));  
            holder.ItemClockTime.setText((String)listItem.get(position).get("ItemClockTime"));  
            holder.ItemClockData.setText((String)listItem.get(position).get("ItemClockData"));  
            holder.ItemClockLastTime.setText((String)listItem.get(position).get("ItemClockLastTime"));  
            holder.ItemStarHeadImage.setBackgroundResource((Integer)listItem.get(position).get("ItemStarHeadImage"));
            
            holder.ButtonControl_onoff.setTag(position);
            String strflag = (String)listItem.get(position).get("ButtonControl_onoff");
            
            if( 0 == Integer.valueOf(strflag).intValue()){
            	
            	holder.ButtonControl_onoff.setChecked(false);
            }else if (1 == Integer.valueOf(strflag).intValue()){
            	
            	holder.ButtonControl_onoff.setChecked(true);
            }
            
            //给Button添加单击事件  添加Button之后ListView将失去焦点  需要的直接把Button的焦点去掉  
            holder.ButtonControl_onoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            	if (isChecked) {
            	
            		Log.i("11111111111", "触发了点击事件");
            		//开启闹钟
            		//获取ID
            		String ID = myClockId.get(position);
            		OpenClock(Integer.valueOf(ID).intValue());
            		updataopenclock(Integer.valueOf(ID).intValue(), 1);
            		
            	} else {
            	
            		Log.i("2222222222222222", "触发了点击事件");
            		//关闭闹钟
            		String ID = myClockId.get(position);
            		deleteClock(Integer.valueOf(ID).intValue());
            		updataopenclock(Integer.valueOf(ID).intValue(), 0);
            	}
            	}
            	});       
            
            holder.ItemStarHeadImage.
            setOnClickListener(new View.OnClickListener() {  
                @Override  
                public void onClick(View v) {  
                	Log.i("anniudianjichenggong", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"); 
                	if (nPlayMusic == -1){
                		//播放音乐
                    	playmusic(position);
                    	nPlayMusic = position;
                	}else if(position == nPlayMusic){
                		sc.pause();
                		nPlayMusic = -1;
                	}else{
                		sc.pause();
                		playmusic(position);
                    	nPlayMusic = position;
                	}
                }  
            });  
            return convertView;  
        }  
    }  
	public void updataopenclock(int Id, int flag){
		Cursor cur = sql.FindData(MySQLiteOpenHelper.TABLE_NAME, Integer.valueOf(Id).intValue());
		if (cur.getCount() != 0){
			String RECORD_DIR = "record_dir";
			cur.getString(2);
			ContentValues cv = new ContentValues();
			cv.put(RECORD_DIR, ""+flag);
			sql.UpdateData(MySQLiteOpenHelper.TABLE_NAME, Id, cv);
		}
	}
	public void OpenClock(int ID){
		
		Cursor cur = sql.FindData(MySQLiteOpenHelper.TABLE_NAME, ID);
		if (cur.getCount() != 0){
			String strtime = cur.getString(9);
			String[] str = strtime.split(":");
			int TIME_HOUR = Integer.valueOf(str[0]).intValue();
			int TIME_CENT = Integer.valueOf(str[1]).intValue();
			
			Log.i("设置闹铃的时间", ""+TIME_HOUR);
			Log.i("设置闹铃的时间", ""+TIME_CENT);
			
			
			calendar.setTimeInMillis(System
					.currentTimeMillis());
			calendar.set(Calendar.HOUR_OF_DAY, TIME_HOUR);
			calendar.set(Calendar.MINUTE, TIME_CENT);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			/* 建立Intent和PendingIntent，来调用目标组件 */
			Intent intent = new Intent(Clock.this,
					AlarmReceiver.class);
			intent.putExtra("id", ""+ID);
			
			PendingIntent pendingIntent = PendingIntent
					.getBroadcast(Clock.this, ID,
							intent, 0);
			AlarmManager am;
			/* 获取闹钟管理的实例 */
			am = (AlarmManager) getSystemService(ALARM_SERVICE);
			
			/* 设置闹钟 */
			
			long time = calendar.getTimeInMillis();
			if (time < System.currentTimeMillis()){
				time = (System.currentTimeMillis() + 24 * 60 * 60 * 1000) - (System.currentTimeMillis() - time);
			}
			Log.i("ssssssssssssss", ""+calendar.getTimeInMillis());
			Log.i("ssssssssssssss", ""+System.currentTimeMillis());
			Log.i("ssssssssssssss", ""+time);
			am.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
			
			/* 设置周期闹 
			am.setRepeating(AlarmManager.RTC_WAKEUP, System
					.currentTimeMillis()
					+ (10 * 1000), (24 * 60 * 60 * 1000),
					pendingIntent);*/
		}
	}
	
	public void deleteClock(int ID){
		Intent i=new Intent(Clock.this,AlarmReceiver.class); 
		PendingIntent pi = PendingIntent.getBroadcast(Clock.this, ID , i, 0);  
		AlarmManager am;
		/* 获取闹钟管理的实例 */
		am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.cancel(pi);
	}
	
	public final class ViewHolder {   
        public TextView ItemClockName;  
        public TextView ItemClockTime;  
        public TextView ItemClockData;  
        public TextView ItemClockLastTime; 
        public ImageView ItemStarHeadImage;
        public ToggleButton ButtonControl_onoff;
       
    }
	
	void playmusic(int index){
		String ID = myClockId.get(index);
		Cursor cur = sql.FindData(MySQLiteOpenHelper.TABLE_NAME, Integer.valueOf(ID).intValue());
		if (cur.getCount() != 0){
			String strDir = cur.getString(8);
			File file = new File(strDir);
			if (file.exists()){
				if (sc == null){
					sc = new SoundControl(0);
				}
				sc.playMusic(strDir);
			}
		}
	}
}


