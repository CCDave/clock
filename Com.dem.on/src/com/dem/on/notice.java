package com.dem.on;
import java.io.File;

import android.os.Bundle;
import android.util.Log;
import android.app.Activity;
import android.database.Cursor;

public class notice extends Activity{
	SoundControl sc = null;
	MySQLiteWorker sql = null;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.setup_activity); 
        if (sc == null){
        	//参数为1 无线播放
        	sc = new SoundControl(1);
        }
        if (sql == null){
        	sql = new MySQLiteWorker(this);
        }
        Bundle bundle = this.getIntent().getExtras();
        
        String strID = bundle.getString("id");
        
        Cursor cur = sql.FindData(MySQLiteOpenHelper.TABLE_NAME, Integer.valueOf(strID).intValue());
        
        if (cur.getCount() != 0){

        	String strdir = cur.getString(8);
			File file = new File(strdir);
			if (file.exists()){
				if (sc == null){
					sc = new SoundControl(1);
				}
				sc.playMusic(strdir);
			}
        }
        Log.i("=============================", "11111111111111111111111111111");
    }
}
