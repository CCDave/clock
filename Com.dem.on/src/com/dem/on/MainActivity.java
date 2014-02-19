package com.dem.on;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TextView;
import android.util.Log;
import com.dem.on.Clock;
import com.dem.on.R;


@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

	TabHost tabHost;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;
	private static boolean bfistflag = false;
	
	int currentView = 0;
	private static int maxTabIndex = 3;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (!bfistflag){
			firstload();
			bfistflag = true;
		}
		
        setContentView(R.layout.activity_main);
		tabHost = getTabHost();
		//UtilVar.activities.add(MainActivity.this);
		setTabs();  
		
		gestureDetector = new GestureDetector(new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
		};
    }
    private void firstload(){
    	Intent it = new Intent(MainActivity.this, TestWeiXinWhatsNewActivity.class);

		startActivity(it);
    }
    private void setTabs()
	{
    	addTab("clock", R.drawable.tab_clock, Clock.class);
    	addTab("stars", R.drawable.tab_stars, stars.class);
    	addTab("record", R.drawable.tab_record, record.class);
    	addTab("setup", R.drawable.tab_record, setup.class);
	}

	private void addTab(String labelId, int drawableId, Class<?> c)
	{
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);	
		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);
		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
	}
	
	// ���һ����պ�ҳ��Ҳ�л���Ч��
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

		/*@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			getMenuInflater().inflate(R.menu.activity_main, menu);
			return true;
		}*/

}
