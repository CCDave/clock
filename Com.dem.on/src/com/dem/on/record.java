package com.dem.on;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.MediaStore;
import android.widget.AdapterView;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;


import com.dem.on.R;
import com.dem.on.RecordHelper;
import com.dem.on.MySQLiteOpenHelper;
import com.dem.on.RecordButton;
import com.dem.on.RecordButton.OnFinishedRecordListener;
import com.dem.on.setup.MyAdapter;
import com.dem.on.setup.ViewHolder;
import com.dem.on.setup.ViewHolderHead;
import com.dem.on.config;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.content.ContentValues;


public  class record extends Activity implements OnTouchListener, OnClickListener,
RecordHelper.OnStateChangedListener, OnCompletionListener, OnErrorListener{
	
	String[] way={"相机","图库"};
	Activity a = this;
	
	WakeLock mWakeLock;//用WakeLock请求休眠锁，让其不会休眠
	private RecordHelper mRecorder;//录音类，实现录音功能

	private MySQLiteWorker sql = null;
	private Button ButtonStartRecord;
	
	private ListView list = null;
	private int listItemPosition = 0;
	private RelativeLayout tupianbuju = null;
	private ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
	private List<String> myClockId = new ArrayList<String>();
	private SoundControl  sc = null;
	private int nPlayMusic = -1;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recordhome_activity);
        sql = new MySQLiteWorker(this);
        sql.CreateDataTable(MySQLiteOpenHelper.MYRECORD_TABLE_NAME);
        sql.EnumDataBase(MySQLiteOpenHelper.MYRECORD_TABLE_NAME);
        InitView();
        
        InitListView();
    }
	private void InitView(){
		ButtonStartRecord = (Button)findViewById(R.id.ButtonStartRecord);
		ButtonStartRecord.setOnClickListener(this);
		list = (ListView) findViewById(R.id.ListViewUserData);
		list.setVerticalScrollBarEnabled(false);
		
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
		//弹出菜单  
        list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {  
            @Override  
            public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
                menu.add(0, 0, 0, "删除");  
            }  
        });  
		tupianbuju = (RelativeLayout)findViewById(R.id.tishitupian);
		
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
			
			default:
				break;
			}
		}catch(Exception e){}
		
		return super.onContextItemSelected(menu);
	}
	
	private void DeleteClock(int index){
		String ID = myClockId.get(index);
		sql.DeleteData(MySQLiteOpenHelper.MYRECORD_TABLE_NAME, Integer.valueOf(ID).intValue());
		InitListView();
	}
	
	private void InitListView(){
		
		if (TestBase() == 0){
			list.setVisibility(View.GONE);
			tupianbuju.setVisibility(View.VISIBLE);
			return;
		}else{
			list.setVisibility(View.VISIBLE);
			tupianbuju.setVisibility(View.GONE);
		}
		
		listItem.clear();
		myClockId.clear();
        sql.EnumDataBase(MySQLiteOpenHelper.MYRECORD_TABLE_NAME);
        Cursor cur = sql.GetTableCursor(MySQLiteOpenHelper.MYRECORD_TABLE_NAME);
        
		if (cur.getCount() != 0) {
			while (cur.moveToNext()) {//直到返回false说明表中到了数据末尾
				myClockId.add(cur.getString(0));
				HashMap<String, Object> maptmp = new HashMap<String, Object>(); 
				Log.i("lllllllllllllllllllllll", cur.getString(3));
				maptmp.put("ItemBigPicture", cur.getString(3));
				maptmp.put("Itemleijijiaoxing_shu", "56312728");
				maptmp.put("Itemjiaoxinglv_shu", "91%");
				maptmp.put("ItemButtonYuyin", "语音");
				maptmp.put("ItemButtonDaoju", "道具");
				maptmp.put("ItemRecordTime", cur.getString(4));
		        listItem.add(maptmp);
			}
		}
        
        
        MyAdapter listItemAdapter = new MyAdapter(this);

        list.setAdapter(listItemAdapter);  
	}
	
	
	private int TestBase(){
		Cursor cur = sql.GetTableCursor(MySQLiteOpenHelper.MYRECORD_TABLE_NAME);
		int count = 0;
		while (cur.moveToNext()) {//直到返回false说明表中到了数据末尾
			Log.i("youwuyuansu", "11111111111111111");
			
			count++;
		}
		return count;
		
	}
	public void startPlayback(String strfilname)
	{
		mRecorder.startPlayback(strfilname);
	}



	@Override
	public void onClick(View v) {
		
		// TODO Auto-generated method stub
		int ID = v.getId();
		if(ID == R.id.ButtonStartRecord){
			Bundle bundle = new Bundle();
		    bundle.putString("id", "4");
		    Intent intent = new Intent(this, newrecord.class);
		    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        intent.putExtras(bundle);
	        startActivityForResult(intent,1);
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
                    holder = new ViewHolder();    
                    //可以理解为从vlist获取view  之后把view返回给ListView  
                    convertView = mInflater.inflate(R.layout.record_one, null);  
                   
                    holder.ItemBigPicture = (ImageView)convertView.findViewById(R.id.ItemBigPicture);  
                    holder.Itemleijijiaoxing_shu = (TextView)convertView.findViewById(R.id.Itemleijijiaoxing_shu);
                    holder.Itemjiaoxinglv_shu = (TextView)convertView.findViewById(R.id.Itemjiaoxinglv_shu);
                    holder.ItemButtonYuyin = (Button)convertView.findViewById(R.id.ItemButtonYuyin);
                    holder.ItemButtonDaoju = (Button)convertView.findViewById(R.id.ItemButtonDaoju);
                    holder.ItemRecordTime = (TextView)convertView.findViewById(R.id.ItemRecordTime);
                    
                    holder.ItemButtonListen = (Button)convertView.findViewById(R.id.ItemButtonListen);
                    holder.ItemButtonSetClock = (Button)convertView.findViewById(R.id.ItemButtonSetClock);
                    holder.ItemButtonPlayRecord = (Button)convertView.findViewById(R.id.ItemButtonPlayRecord);
                    holder.ItemButtonDeleteData = (Button)convertView.findViewById(R.id.ItemButtonDeleteData);
                    convertView.setTag(holder);               
                }else {               
                    holder = (ViewHolder)convertView.getTag();  
                }         

                File file = new File((String)listItem.get(position).get("ItemBigPicture"));
                if (file.exists()){
                	
                	Bitmap bm = BitmapFactory.decodeFile((String)listItem.get(position).get("ItemBigPicture"));
                	 // 获得图片的宽高
                    int width = bm.getWidth();
                    int height = bm.getHeight();
                    // 设置想要的大小
                    int newWidth = 800;
                    int newHeight = 400;
                    // 计算缩放比例
                    float scaleWidth = ((float) newWidth) / width;
                    float scaleHeight = ((float) newHeight) / height;
                    // 取得想要缩放的matrix参数
                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleWidth, scaleHeight);
                    // 得到新的图片
                    Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                      true);
                    if (bm.isRecycled()) {
                        bm.recycle();
                    }
                    
                	holder.ItemBigPicture.setImageBitmap(newbm);
                }else{
                	holder.ItemBigPicture.setBackgroundResource(R.drawable.queshengtupian);
                }
                
                
                
                holder.Itemleijijiaoxing_shu.setText((String)listItem.get(position).get("Itemleijijiaoxing_shu"));
                holder.Itemjiaoxinglv_shu.setText((String)listItem.get(position).get("Itemjiaoxinglv_shu"));
                holder.ItemButtonYuyin.setText((String)listItem.get(position).get("ItemButtonYuyin"));
                holder.ItemButtonDaoju.setText((String)listItem.get(position).get("ItemButtonDaoju"));
                holder.ItemRecordTime.setText((String)listItem.get(position).get("ItemRecordTime"));
                
                //删除
                
                holder.ItemButtonDeleteData.setOnClickListener(new View.OnClickListener() {  
                    @Override  
                    public void onClick(View v) {  
                    	DeleteClock(position);
                    }  
                });  
                //预览
                holder.ItemButtonListen.setOnClickListener(new View.OnClickListener() {  
                    @Override  
                    public void onClick(View v) {  
                    	yulan(position);
                    }  
                });  
                //设置为闹钟
                holder.ItemButtonSetClock.setOnClickListener(new View.OnClickListener() {  
                    @Override  
                    public void onClick(View v) {  
                    	addtoclock(position);
                    }  
                });  
                //播放音乐
                holder.ItemButtonPlayRecord.setOnClickListener(new View.OnClickListener() {  
                    @Override  
                    public void onClick(View v) {  
                    	
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
    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
		case (1) :{
			if (resultCode == Activity.RESULT_OK)
			{Log.i("result", "11111111111111111");
				InitListView();
			}
			break;
		}
		case (2) :{
			if (resultCode == Activity.RESULT_OK)
			{Log.i("result", "11111111111111111");
				InitListView();
			}
			break;
		}
		}
	}
    
    void yulan(int index){
    	String ID = myClockId.get(index);
    	Intent OkPage = new Intent();
		OkPage.setClass(record.this, notice.class);
	    Bundle bundle = new Bundle();
	    bundle.putString("flag", "2");
	    bundle.putString("id", ID);
	    OkPage.putExtras(bundle);
	    startActivityForResult(OkPage, 1);
    }
    void addtoclock(int index){
    	String ID = myClockId.get(index);
    	Intent OkPage = new Intent();
		OkPage.setClass(record.this, addclock.class);
	    Bundle bundle = new Bundle();
	    bundle.putInt("flag", 3);
	    bundle.putInt("updataid", Integer.valueOf(ID).intValue());
	    OkPage.putExtras(bundle);
	    startActivityForResult(OkPage, 1);
    
    }
    void playmusic(int index){
		String ID = myClockId.get(index);
		Cursor cur = sql.FindData(MySQLiteOpenHelper.MYRECORD_TABLE_NAME, Integer.valueOf(ID).intValue());
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
    
	public final class ViewHolder {   

        public ImageView ItemBigPicture;
        
        public TextView ItemRecordTime;
        public TextView Itemleijijiaoxing_shu;
        public TextView Itemjiaoxinglv_shu;
        public Button ItemButtonYuyin;
        public Button ItemButtonDaoju;
        public Button ItemButtonListen;
        public Button ItemButtonSetClock;
        public Button ItemButtonPlayRecord;
        public Button ItemButtonDeleteData;
        
    }
	@Override
	public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStateChanged(int state) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onError(int error) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	protected void onResume() {
		Log.i("重新激活了列表", "1111111111111111111111111111111111");
		 InitListView();
		 super.onResume();
	 }
}
