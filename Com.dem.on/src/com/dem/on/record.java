package com.dem.on;



import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import com.dem.on.R;
import com.dem.on.RecordHelper;
import com.dem.on.MySQLiteOpenHelper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;


public  class record extends Activity implements OnTouchListener,
RecordHelper.OnStateChangedListener{
	
	WakeLock mWakeLock;//用WakeLock请求休眠锁，让其不会休眠
	private Button btnStart;//开始录音按钮
	private RecordHelper mRecorder;//录音类，实现录音功能
	private String strSampleFilePath;
	private MySQLiteOpenHelper sqlHelpRecord; //录音数据
	private SQLiteDatabase sqlRecord ; 	//录音数据库
	
	private ListView list; //列表框
	
	private ArrayList<HashMap<String, Object>> listItem;
	private SimpleAdapter listItemAdapter ;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_activity); 
        setupDataBase();
        setupViews();
    }
	
	//初始化
	@SuppressWarnings("deprecation")
	public void setupViews() {

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
				"SoundRecorder");//请求休眠锁
		mRecorder = new RecordHelper();
		mRecorder.setOnStateChangedListener(this);
		//根据ID找到相应控件
		btnStart = (Button) findViewById(R.id.ButtonRecord);	
		btnStart.setOnTouchListener(this);
		
		listItem = new ArrayList<HashMap<String, Object>>();
		list = (ListView) findViewById(R.id.ListViewRecord);
		listItemAdapter = new SimpleAdapter(this,listItem,//锟斤拷锟皆�  
	            R.layout.listitem,//ListItem锟斤拷XML实锟斤拷  
	            //锟斤拷态锟斤拷锟斤拷锟斤拷ImageItem锟斤拷应锟斤拷锟斤拷锟斤拷          
	            new String[] {"ItemStarHeadImage","ItemClockName", "ItemClockTime","ItemClockData","ItemClockLastTime"},   
	            //ImageItem锟斤拷XML锟侥硷拷锟斤拷锟斤拷锟揭伙拷锟絀mageView,锟斤拷锟斤拷TextView ID  
	            new int[] {R.id.ItemStarHeadImage,R.id.ItemClockName,R.id.ItemClockTime,R.id.ItemClockData, R.id.ItemClockLastTime}  
	        );
		list.setAdapter(listItemAdapter);  
		InitDataBase();
		refreshList();
	}

	public void refreshList(){
		listItemAdapter = new SimpleAdapter(this,listItem,//锟斤拷锟皆�  
	            R.layout.listitem,//ListItem锟斤拷XML实锟斤拷  
	            //锟斤拷态锟斤拷锟斤拷锟斤拷ImageItem锟斤拷应锟斤拷锟斤拷锟斤拷          
	            new String[] {"ItemStarHeadImage","ItemClockName", "ItemClockTime","ItemClockData","ItemClockLastTime"},   
	            //ImageItem锟斤拷XML锟侥硷拷锟斤拷锟斤拷锟揭伙拷锟絀mageView,锟斤拷锟斤拷TextView ID  
	            new int[] {R.id.ItemStarHeadImage,R.id.ItemClockName,R.id.ItemClockTime,R.id.ItemClockData, R.id.ItemClockLastTime}  
	        );
		listItemAdapter.notifyDataSetChanged();
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
	public boolean onTouch(View v, MotionEvent event) {
		if (v.getId() == R.id.ButtonRecord)
		{
			if (event.getAction() == MotionEvent.ACTION_DOWN)
			{
				btnStart.setBackgroundResource(R.color.black);
				//按下按钮开始录音
				mRecorder.startRecording(MediaRecorder.OutputFormat.DEFAULT,
	   					".mp3", this);
				
				//这里还可以进行其他操作
				
				//开启录音动画并计时
			}
			if (event.getAction() == MotionEvent.ACTION_UP)
			{
				btnStart.setBackgroundResource(R.color.green);
				mRecorder.stopRecording();
				strSampleFilePath = mRecorder.getSampleFile();
				//获取文件路径
				InsertData(strSampleFilePath, "1111");
				refreshList();
				Log.i("filadirectory",strSampleFilePath+"123123123123"); 
			}
		}
		return false;
	}
	
	public void setupDataBase() {
		sqlHelpRecord = new MySQLiteOpenHelper(this);// 实例一个数据库辅助器
		sqlRecord = sqlHelpRecord.getWritableDatabase();
	}
	
	//遍历数据库中的数据-并刷新列表
	public void InitDataBase() {
		Cursor cur = sqlRecord.rawQuery("SELECT * FROM "
				+ MySQLiteOpenHelper.TABLE_NAME, null);
		listItem.clear();
		
		
		if (cur != null) {
			String name = "";
			String time = "";
			
			while (cur.moveToNext()) {//直到返回false说明表中到了数据末尾
				name = cur.getString(0); 
				// 参数0 指的是列的下标,这里的0指的是id列
				time = cur.getString(1);
				// 这里的0相对于当前应该是咱们的text列了
				HashMap<String, Object> map = new HashMap<String, Object>();  
				map.put("ItemClockName", name);
				map.put("ItemClockTime", time);
				listItem.add(map);  
			}
		}
	}
	
	public void InsertData(String name, String time){
		ContentValues cv = new ContentValues();
		cv.put(MySQLiteOpenHelper.NAME, name);
		cv.put(MySQLiteOpenHelper.TIME, time);
		sqlRecord.insert(MySQLiteOpenHelper.TABLE_NAME, null, cv);
	}
	
	public void DeleteData(){
		sqlRecord.delete("himi", MySQLiteOpenHelper.ID + "=1", null);
	}
	
	public void UpdateData(String name, String time){
		ContentValues cv = new ContentValues();
		cv.put(MySQLiteOpenHelper.NAME, name);
		cv.put(MySQLiteOpenHelper.TIME, time);
		sqlRecord.update("himi", cv, "id " + "=" + Integer.toString(3), null);
	}
	
	public void CreateTable(){
		String TABLE_NAME = "himi";
		String ID = "id";
		String TEXT = "text";
		String str_sql2 = "CREATE TABLE " + TABLE_NAME + "(" + ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + TEXT
				+ " text );";
		sqlRecord.execSQL(str_sql2);
	}
	
	public void DropTable(){
		sqlRecord.execSQL("DROP TABLE himi");
	}
}
