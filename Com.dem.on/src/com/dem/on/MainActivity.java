package com.dem.on;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;



import com.dem.on.EmptyActivity;

import com.dem.on.Clock;
import com.dem.on.R;


@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

	
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;

	
	int currentView = 0;
	private static int maxTabIndex = 3;
	
	TabHost tabHost = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//IfFristLoad();
		createDataDir();
        setContentView(R.layout.activity_main);
        
        Intent OkPage = new Intent();
		OkPage.setClass(MainActivity.this, ViewPagerActivity.class);
	    Bundle bundle = new Bundle();
	    bundle.putInt("flag", 1);
	    bundle.putInt("updataid", 0);
	    OkPage.putExtras(bundle);
	    startActivityForResult(OkPage, 1);
	    //finish();
        
        
		/*tabHost = getTabHost();
		setTabs();  
		gestureDetector = new GestureDetector(new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
		};*/
    }
    
    private void IfFristLoad(){
    	SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
    	boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
    	Editor editor = sharedPreferences.edit();
    	if (isFirstRun)
    	{
    	Log.i("debug", "第一次运行");
    		//firstload();
    		MySQLiteWorker sql = new MySQLiteWorker(this);
    		sql.AddNewClockToDataBase();
    		sql.AddNewClockToDataBase();
    		//创建存储路径
    		createDataDir();
    		editor.putBoolean("isFirstRun", false);
    		editor.commit();
    	} else
    	{
    	Log.i("debug", "不是第一次运行");
    	}
    }
    private void createDataDir(){
    	File f = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + config.PICTURE_DIR);
    	File f1 = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + config.RECORD_DIR);
    	 if(f.exists() == false) {
    	        f.mkdirs();
    	 System.out.println("路径不存在,但是已经成功创建了" + Environment.getExternalStorageDirectory()
 				.getAbsolutePath() + config.PICTURE_DIR);
    	 }else{
    	  System.out.println("文件路径存在" + Environment.getExternalStorageDirectory()
  				.getAbsolutePath() + config.PICTURE_DIR);
    	 }
    	 if(f1.exists() == false) {
 	        f1.mkdirs();
 	        System.out.println("路径不存在,但是已经成功创建了" + Environment.getExternalStorageDirectory()
				.getAbsolutePath() + config.RECORD_DIR);
    	 }else{
    		 System.out.println("文件路径存在" + Environment.getExternalStorageDirectory()
				.getAbsolutePath() + config.RECORD_DIR);
 	 }
    }
    
    private void firstload(){
    	Intent it = new Intent(MainActivity.this, TestWeiXinWhatsNewActivity.class);
		startActivity(it);
    }
    
    private void setTabs()
	{
    	addTab("闹钟", R.drawable.tab_clock, Clock.class);
    	addTab("明星", R.drawable.tab_stars, stars.class);
    	addTab("录音", R.drawable.tab_record, record.class);
    	addTab("我", R.drawable.tab_setup, me.class);
	}

	private void addTab(String labelId, int drawableId, Class<?> c)
	{
		Intent intent = new Intent(this, c);
		
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);	
		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);
		title.setTextSize(18);
		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
	}
	
	class MyGestureDetector extends SimpleOnGestureListener {
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				TabHost tabHost = getTabHost();
				try {
					if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
						return false;
					// right to left swipe
					if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
							&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
						Log.i("test", "right");
						if (currentView == maxTabIndex) {
							currentView = 0;
						} else {
							currentView++;
						}
						tabHost.setCurrentTab(currentView);
					} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
							&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
						Log.i("test", "left");
						if (currentView == 0) {
							currentView = maxTabIndex;
						} else {
							currentView--;
						}
						tabHost.setCurrentTab(currentView);
					}
				} catch (Exception e) {
				}
				return false;
			}
		}
		
		@Override
		public boolean dispatchTouchEvent(MotionEvent event) {
			if (gestureDetector.onTouchEvent(event)) {
				event.setAction(MotionEvent.ACTION_CANCEL);
			}
			return super.dispatchTouchEvent(event);
		}
		
}
