package com.dem.on;

import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;


public class ClockManage extends Activity{
	
	
	Calendar calendar;
	
	ClockManage(){
		calendar = Calendar.getInstance();
	}
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
	}
	public void OpenClock(){
		
		calendar.setTimeInMillis(System
				.currentTimeMillis());
		//calendar.set(Calendar.HOUR_OF_DAY, TIME_HOUR);
		//calendar.set(Calendar.MINUTE, TIME_CENT);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		/* 建立Intent和PendingIntent，来调用目标组件 */
		Intent intent = new Intent(ClockManage.this,
				AlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent
				.getBroadcast(ClockManage.this, 0,
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
	
	public void CloseClock(){
		
	}
	
	public String GetClockName(){
		return "";
	}
}
