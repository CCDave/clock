package com.dem.on;



import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;


import android.os.Bundle;
import android.util.Log;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.TextView;
import com.dem.on.MySQLiteWorker;


public class addclock extends Activity implements OnTouchListener , OnClickListener{
	
	String[] num={"只响一次","每天","法定工作日（智能跳过节假日）","周一至周五","自定义"};
	
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MIN_DISTANCE_CENT = 80;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;
	
	private static  int TIME_HOUR = 12;
	private static  int MAX_HOUR = 24;
	
	private static float flag_hour = 0;
	private static float flag_cent = 0;
	
	private static int flag_which_view = 0;
	
	private static  int MAX_CENT = 60;
	private static  int TIME_CENT = 30;
	
	MySQLiteWorker sql = null;
	
	LinearLayout fram_hour;
	LinearLayout fram_cent;
	
	
	RelativeLayout fram_chongfu;
	RelativeLayout fram_lingsheng;
	RelativeLayout fram_zhendong;
	RelativeLayout fram_beizhu;
	
	Button btquxiao;
	Button btqueding;
	Calendar calendar;
	
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.addclock_activity);   
        
        sql = new MySQLiteWorker(this);
        //sql.CreateDataTable(MySQLiteOpenHelper.TABLE_NAME);
        testDataBase();
        gestureDetector = new GestureDetector(new MyGestureDetector());
        
        fram_hour = (LinearLayout) findViewById(R.id.setclocltimehour);	
        fram_hour.setOnTouchListener(this);
        
        fram_cent = (LinearLayout) findViewById(R.id.setclocltimecent);	
        fram_cent.setOnTouchListener(this);
        
        fram_chongfu = (RelativeLayout) findViewById(R.id.fram_chongfu);	
        fram_chongfu.setOnClickListener( this);
        
        fram_lingsheng = (RelativeLayout) findViewById(R.id.fram_lingsheng);	
        fram_lingsheng.setOnClickListener(this);
        
        fram_zhendong = (RelativeLayout) findViewById(R.id.fram_zhendong);	
        fram_zhendong.setOnClickListener(this);
        
        fram_beizhu = (RelativeLayout) findViewById(R.id.fram_beizhu);	
        fram_beizhu.setOnClickListener(this);
        
        btquxiao = (Button) findViewById(R.id.ButtonAddClock_quxiao);	
        btquxiao.setOnClickListener(this);
        
        btqueding = (Button) findViewById(R.id.ButtonAddClock_queding);	
        btqueding.setOnClickListener(this);
        
        calendar = Calendar.getInstance();
        TIME_HOUR = calendar.get(Calendar.HOUR_OF_DAY); 
        TIME_CENT = calendar.get(Calendar.MINUTE);
        
        sethourtext();
        setcenttext();
        
		/*gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
		};*/
    }
	
	
	public void testDataBase(){
		if (sql == null){
			Log.i("DataBase", "sql ======null");
			return ;
		}
		
		//String ID = "id";
		String NAME = "name";
		String RECORD_DIR = "record_dir";
		String PICTURE_DIR = "picture_dir";
		String TIME = "time";
		String WAY = "way";
		
		/*for (int i =0; i < 10; i++)
		{
			ContentValues cv = new ContentValues();
			cv.put(NAME, i+"111");
			cv.put(RECORD_DIR, i+"222");
			cv.put(PICTURE_DIR, i+"333");
			cv.put(TIME, i+"444");
			cv.put(WAY, i+"555");
			sql.InsertData(cv);
		}*/
		
		sql.EnumDataBase(MySQLiteOpenHelper.TABLE_NAME);
		Log.i("DataBase", "*************************************************************");
		sql.DeleteData(MySQLiteOpenHelper.TABLE_NAME, 0);
		sql.EnumDataBase(MySQLiteOpenHelper.TABLE_NAME);
		
		Log.i("DataBase", "*************************************************************");
		sql.DeleteData(MySQLiteOpenHelper.TABLE_NAME, 10);
		sql.EnumDataBase(MySQLiteOpenHelper.TABLE_NAME);
		
		Log.i("DataBase", "*************************************************************");
		ContentValues cv = new ContentValues();
		cv.put(NAME, "fff");
		cv.put(RECORD_DIR, "fff");
		cv.put(PICTURE_DIR, "fff");
		cv.put(TIME, "fff");
		cv.put(WAY, "fff");
		sql.UpdateData(MySQLiteOpenHelper.TABLE_NAME, 7, cv);
		sql.EnumDataBase(MySQLiteOpenHelper.TABLE_NAME);
		
	}
	public void getClockTimeLong(){
		//先获取当前时间
		calendar.setTimeInMillis(System.currentTimeMillis());
		int mHour = calendar.get(Calendar.HOUR_OF_DAY);
		int mMinute = calendar.get(Calendar.MINUTE);
		
		//获取字体时间
		int tHour = 0;
		int tCent = 0;
		
		//算出时间长度
		if (mHour < TIME_HOUR)
			tHour = TIME_HOUR - mHour;
		else
			tHour =  24 - mHour + TIME_HOUR;
		
		if (mMinute > TIME_CENT)
		{
			tCent = 60 - mMinute + TIME_CENT;
			tHour--;
		}
		else
			tCent = TIME_CENT - mMinute;
		
		String str = "还有" + Integer.toString(tHour) + "小时" + Integer.toString(tCent)+ "分响铃";
		TextView text = (TextView) findViewById(R.id.addclocksettimelongtext);	
		text.setText(str);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
	        final int action = event.getAction();
	        switch (action) {    
	        case MotionEvent.ACTION_DOWN: 
	        	flag_hour = event.getY();
	        	flag_cent = event.getY();
	            break;    
	        case MotionEvent.ACTION_MOVE:  
	        	if (flag_which_view == R.id.setclocltimehour){
	        		
		        	try {
						if (Math.abs(event.getX() - event.getX()) > SWIPE_MAX_OFF_PATH)
							return false;
						// right to left swipe
						if (event.getY() - flag_hour > SWIPE_MIN_DISTANCE
								) {
							
							TIME_HOUR--;
							if (TIME_HOUR < 0) {
								TIME_HOUR = 23;
							} else {
								
							}	
							flag_hour = event.getY();
							sethourtext();
						} else if (flag_hour - event.getY() > SWIPE_MIN_DISTANCE
								) {
							
							TIME_HOUR++;
							if (TIME_HOUR == MAX_HOUR) {
								TIME_HOUR = 0;
							} else {
								
							}
							flag_hour = event.getY();
							sethourtext();
						}
		
					} catch (Exception e) {
					}
	        	}
	        	if (flag_which_view == R.id.setclocltimecent){
	        		
		        	try {
						if (Math.abs(event.getX() - event.getX()) > SWIPE_MAX_OFF_PATH)
							return false;
						// right to left swipe
						if (event.getY() - flag_cent > SWIPE_MIN_DISTANCE_CENT
								) {
							
							TIME_CENT--;
							if (TIME_CENT < 0) {
								TIME_CENT = 59;
							} else {
								
							}	
							flag_cent = event.getY();
							setcenttext();
						} else if (flag_cent - event.getY() > SWIPE_MIN_DISTANCE_CENT
								) {
							
							TIME_CENT++;
							if (TIME_CENT == MAX_CENT) {
								TIME_CENT = 0;
							} else {
								
							}
							flag_cent = event.getY();
							setcenttext();
						}
		
					} catch (Exception e) {
					}
	        	}
	           break;    
	                
	        case MotionEvent.ACTION_UP:     
	        	flag_which_view = 0;
	            break;      
	        }    
	            
	        return true;    
	}
	
	public void sethourtext(){
		int hour1 = 0;
		int hour2 = 0;
		int hour3 = 0;
		int hour4 = 0;
		int hour5 = 0;

		if(TIME_HOUR == 1)
		{
			hour1 = 23;
			hour2 = 0; 
			hour3 = TIME_HOUR ;
			hour4 = TIME_HOUR + 1;
			hour5 = TIME_HOUR + 2;
		}	
		else if(TIME_HOUR == 0)
		{
			hour1 = 22;
			hour2 = 23; 
			hour3 = TIME_HOUR ;
			hour4 =  1;
			hour5 =  2;
		}	
		else if(TIME_HOUR == 22)
		{
			hour1 = TIME_HOUR -2;
			hour2 = TIME_HOUR -1; 
			hour3 = TIME_HOUR ;
			hour4 =  23;
			hour5 =  0;
		}
		else if(TIME_HOUR == 23)
		{
			hour1 = TIME_HOUR -2;
			hour2 = TIME_HOUR -1; 
			hour3 = TIME_HOUR ;
			hour4 =  0;
			hour5 =  1;
		}	
		else{
				hour1 = TIME_HOUR -2;
				hour2 = TIME_HOUR -1; 
				hour3 = TIME_HOUR ;
				hour4 = TIME_HOUR + 1;
				hour5 = TIME_HOUR + 2;
			}
		
		TextView thour1 = (TextView) findViewById(R.id.texthour1);	
		thour1.setText( Integer.toString(hour1));
		TextView thour2 = (TextView) findViewById(R.id.texthour2);	
		thour2.setText( Integer.toString(hour2));
		TextView thour3 = (TextView) findViewById(R.id.texthour3);	
		thour3.setText( Integer.toString(hour3));
		TextView thour4 = (TextView) findViewById(R.id.texthour4);	
		thour4.setText( Integer.toString(hour4));
		TextView thour5 = (TextView) findViewById(R.id.texthour5);	
		thour5.setText( Integer.toString(hour5));
		
		getClockTimeLong();
		
	}
	
	public void setcenttext(){
		int cent1 = 0;
		int cent2 = 0;
		int cent3 = 0;
		int cent4 = 0;
		int cent5 = 0;

		if(TIME_CENT == 1)
		{
			cent1 = 59;
			cent2 = 0; 
			cent3 = TIME_CENT ;
			cent4 = TIME_CENT + 1;
			cent5 = TIME_CENT + 2;
		}	
		else if(TIME_CENT == 0)
		{
			cent1 = 58;
			cent2 = 59; 
			cent3 = TIME_CENT ;
			cent4 =  1;
			cent5 =  2;
		}	
		else if(TIME_CENT == 58)
		{
			cent1 = TIME_CENT -2;
			cent2 = TIME_CENT -1; 
			cent3 = TIME_CENT ;
			cent4 =  59;
			cent5 =  0;
		}
		else if(TIME_CENT == 59)
		{
			cent1 = TIME_CENT -2;
			cent2 = TIME_CENT -1; 
			cent3 = TIME_CENT ;
			cent4 =  0;
			cent5 =  1;
		}	
		else{
			cent1 = TIME_CENT -2;
			cent2 = TIME_CENT -1; 
			cent3 = TIME_CENT ;
			cent4 = TIME_CENT + 1;
			cent5 = TIME_CENT + 2;
			}
		
		TextView thour1 = (TextView) findViewById(R.id.textcent1);	
		thour1.setText( Integer.toString(cent1));
		TextView thour2 = (TextView) findViewById(R.id.textcent2);	
		thour2.setText( Integer.toString(cent2));
		TextView thour3 = (TextView) findViewById(R.id.textcent3);	
		thour3.setText( Integer.toString(cent3));
		TextView thour4 = (TextView) findViewById(R.id.textcent4);	
		thour4.setText( Integer.toString(cent4));
		TextView thour5 = (TextView) findViewById(R.id.textcent5);	
		thour5.setText( Integer.toString(cent5));
		getClockTimeLong();
		
	}
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,  
            float distanceY)  
  
    {  
  
        Log.i("MyGesture", "onScroll");  
        return true;  
  
    }  
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		flag_which_view = 0;
		if (v.getId() == R.id.setclocltimecent || v.getId() == R.id.setclocltimehour)
		{
			if(v.getId() == R.id.setclocltimecent){
				flag_which_view = R.id.setclocltimecent;
			}
			if(v.getId() == R.id.setclocltimehour){
				flag_which_view = R.id.setclocltimehour;
			}
			if (gestureDetector.onTouchEvent(event)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		
		if (gestureDetector.onTouchEvent(event)) {
			event.setAction(MotionEvent.ACTION_CANCEL);
		}
		return super.dispatchTouchEvent(event);
	}
	
	class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			
			try {
				if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH)
					return false;
				
				 // right to left swipe
				if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
					Log.i("test", "right");
					if (TIME_HOUR == MAX_HOUR) {
						
					} else {
						//TIME_HOUR++;
					}
				} else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
					Log.i("test", "left");
					if (TIME_HOUR == 0) {
						
					} else {
						//TIME_HOUR--;
					}
				}
				//TextView hour3 = (TextView) findViewById(R.id.texthour3);	
				//hour3.setText( Integer.toString(TIME_HOUR));
						
			} catch (Exception e) {
			}
			return false;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		
		switch(id)
		{
		case R.id.fram_chongfu:
			Log.i("test", "22222222222222");
			
			LayoutInflater layoutInflater = LayoutInflater.from(addclock.this);   
            View myLoginView = layoutInflater.inflate(R.layout.popdlg, null);   
            new AlertDialog.Builder(addclock.this)  
            .setTitle("选择")  
            .setItems(num, new DialogInterface.OnClickListener() {  
                public void onClick(DialogInterface dialog, int which) {    
                    Toast info =Toast.makeText(addclock.this, num[which],Toast.LENGTH_LONG);  
                    info.setMargin(0.0f, 0.3f);  
                    info.show(); 
                    TextView chongfu = (TextView) findViewById(R.id.chongfu_item);	
                    chongfu.setText(num[which]);
                }  
            }  
            )  
            .setView(myLoginView).create().show();
			break;
		case R.id.fram_lingsheng:
			Log.i("test", "1111111111111");
			break;
		case R.id.fram_zhendong:
			Log.i("test", "3333333333333");
			break;
		case R.id.fram_beizhu:
			Log.i("test", "44444444444444");
			break;
			
		case R.id.ButtonAddClock_quxiao:
			Log.i("test", "55555555555555");
			Intent it = new Intent(addclock.this, MainActivity.class);
			startActivity(it);
			break;
			
		case R.id.ButtonAddClock_queding:
			Log.i("test", "6666666666666");
			//1.开启闹钟
			startclock();
			//2.写入数据库
			AddNewClockToDataBase();
			//3.返回主界面		
			returnToMain();
			break;
		default:
			break;
			
		}
		
		}
		
	public void startclock(){

		calendar.setTimeInMillis(System
				.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, TIME_HOUR);
		calendar.set(Calendar.MINUTE, TIME_CENT);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		/* 建立Intent和PendingIntent，来调用目标组件 */
		Intent intent = new Intent(addclock.this,
				AlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent
				.getBroadcast(addclock.this, 0,
						intent, 0);
		AlarmManager am;
		/* 获取闹钟管理的实例 */
		am = (AlarmManager) getSystemService(ALARM_SERVICE);
		/* 设置闹钟 */
		am.set(AlarmManager.RTC_WAKEUP, calendar
				.getTimeInMillis(), pendingIntent);
		/* 设置周期闹 */
		am.setRepeating(AlarmManager.RTC_WAKEUP, System
				.currentTimeMillis()
				+ (10 * 1000), (24 * 60 * 60 * 1000),
				pendingIntent);
	}
	public void AddNewClockToDataBase(){
		
	}
	public void returnToMain(){
		//跳回主界面并显示
				Intent OkPage = new Intent();
				OkPage.setClass(addclock.this, MainActivity.class);
			    Bundle bundle = new Bundle();
			    bundle.putString("name", "shuju");
			    bundle.putDouble("height", 123.123);
			    OkPage.putExtras(bundle);
			    startActivity(OkPage);
	}
}