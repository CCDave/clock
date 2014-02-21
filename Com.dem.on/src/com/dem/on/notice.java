package com.dem.on;

import java.io.File;
import android.os.Bundle;
import android.util.Log;
import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.content.DialogInterface;
import android.content.Intent;


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
				
				AlertDialog.Builder dialog=new AlertDialog.Builder(notice.this);

				dialog.setTitle("Dialog")
				.setMessage("弹出框")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				 @Override
				 public void onClick(DialogInterface dialog, int which) {
				 //转跳到另外一个Activity
				 // TODO Auto-generated method stub
					 //Intent intent=new Intent();
					 //intent.setClass(getApplicationContext(), list.class);
					 //startActivity(intent);
					 sc.pause();
					 finish();
				 }
				 }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

				 public void onClick(DialogInterface dialog, int which) {
				 // TODO Auto-generated method stub
					 dialog.cancel();//取消弹出框
					 sc.pause();
					 finish();
				 }
				 }).create().show();
			}
        }
    }
}
