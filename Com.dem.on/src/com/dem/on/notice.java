package com.dem.on;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.DialogInterface;
import android.content.Intent;


public class notice extends Activity{
	private SoundControl sc = null;
	private MySQLiteWorker sql = null;
	
	
	private ImageView ItemBigPicture_head = null;
	private ImageButton Itemtouxiang = null;
	private ImageButton huangguan = null;
	private TextView Itemusername = null;
	private TextView Itemchenghao = null;
	private TextView TextNoticeWord = null;
	private Button ItemButtonClicktoClose = null;
	private SoundPool sp = null;
	private int spPause = 0;
	private int nCount = 0;
	int music = 0;
	int rplay = -1;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noticepage_activity);
        
        if (sql == null){
        	sql = new MySQLiteWorker(this);
        }
       
		sp= new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
		music = sp.load(this, R.raw.default_music, 1);
		SystemClock.sleep(1000);
		
		
        ItemBigPicture_head = (ImageView)findViewById(R.id.ItemBigPicture_head);
        Itemtouxiang = (ImageButton) findViewById(R.id.Itemtouxiang);
        huangguan = (ImageButton) findViewById(R.id.huangguan);
        ItemButtonClicktoClose = (Button) findViewById(R.id.ItemButtonClicktoClose);
        
        Itemusername = (TextView) findViewById(R.id.Itemusername);
        Itemchenghao = (TextView) findViewById(R.id.Itemchenghao);
        TextNoticeWord = (TextView) findViewById(R.id.TextNoticeWord);
        
        InitView();
    }
	
	public void InitView(){
		
		Bundle bundle = this.getIntent().getExtras();
        String strID = bundle.getString("id");
        String strFlag = bundle.getString("flag");
        String tablename;
        
        
        if(Integer.valueOf(strFlag).intValue() == 2){
        	tablename = MySQLiteOpenHelper.MYRECORD_TABLE_NAME;
        	rplay = 0;
        }
        else{
        	tablename = MySQLiteOpenHelper.TABLE_NAME;
        }
        Cursor cur = sql.FindData(tablename, Integer.valueOf(strID).intValue());
        Log.i(tablename, ""+strID + "dsadasd" + strFlag);
        if (cur.getCount() != 0){

        	String musicdir = cur.getString(8);
        	String picturedir = cur.getString(3);
        	
			File file = new File(musicdir);
			File filepic = new File(picturedir);
			
  		  	if (filepic.exists()){
  		  		Uri imageUri = Uri.fromFile(filepic);
  		  		Bitmap bitmap = null;
  		  		bitmap = decodeUriAsBitmap(imageUri);
  		  		ItemBigPicture_head.setImageBitmap(bitmap);
			}else{
				ItemBigPicture_head.setBackgroundResource(R.drawable.queshengtupian);
			}
  		  	Log.i(musicdir, "bofang");
			if (file.exists()){
				if (sc == null){
					if(Integer.valueOf(strFlag).intValue() == 2){
						sc = new SoundControl(0);
					}else{
						sc = new SoundControl(1);
					}
					Log.i(musicdir, "bofang");
					
					sc.playMusic(musicdir);
				}
			}else{
				//声明一个SoundPool  
				spPause = sp.play(music, 1, 1, 0, 0, 1);
			}
			
			
			ItemButtonClicktoClose.setOnClickListener(new OnClickListener() {	
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					nCount++;
					if (nCount == 30){
						if (sc != null)
							sc.pause();
						if (sp != null)
							sp.pause(spPause);
						
						finish();
					}
				}
			});
        }
	}
	
	
	public void chroseDialog(){
		AlertDialog.Builder dialog=new AlertDialog.Builder(notice.this);

		dialog.setTitle("Dialog")
		.setMessage("弹出框")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
		 @Override
		 public void onClick(DialogInterface dialog, int which) {
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
	
	private Bitmap decodeUriAsBitmap(Uri uri){
   	 Bitmap bitmap = null;
   	 try {
   	  bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
   	 } catch (FileNotFoundException e) {
   	  e.printStackTrace();
   	  return null;
   	 }
   	 return bitmap;
   	}
}
