package com.dem.on;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "你设置的闹钟时间到了", Toast.LENGTH_LONG).show();
		//闹钟来了弹窗并响铃
	}
}
