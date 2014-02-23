package com.dem.on;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
	
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "你设置的闹钟时间到了", Toast.LENGTH_LONG).show();
		//闹钟来了弹窗并响铃
		//根据ID从数据库中获取路径突然响铃
		String msg=intent.getStringExtra("id");
		//触发闹钟创空
		Log.i("shujukushifougengxin", msg);

	    Bundle bundle = new Bundle();
	    bundle.putString("id", msg);
	    bundle.putString("flag", "1");

	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, notice.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
	}
}
