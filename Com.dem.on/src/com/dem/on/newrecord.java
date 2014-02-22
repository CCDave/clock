package com.dem.on;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.MediaStore;
import android.widget.AdapterView;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnTouchListener;


import com.dem.on.R;
import com.dem.on.RecordHelper;
import com.dem.on.MySQLiteOpenHelper;
import com.dem.on.RecordButton;
import com.dem.on.RecordButton.OnFinishedRecordListener;
import com.dem.on.config;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.content.ContentValues;


public  class newrecord extends Activity implements OnTouchListener, OnClickListener,
RecordHelper.OnStateChangedListener, OnCompletionListener, OnErrorListener{
	
	String[] way={"相机","图库"};
	private static final int CHOOSE_BIG_PICTURE = 0;
	private static final int TAKE_BIG_PICTURE = 1;
	
	private Uri imageUri = null;
	
	private final String RECORD_FILE_NAME = "/myTempClock.mp3";//"/mmmm.amr"
	private final String IMAGE_FILE_NAME = "/myTempImge.png";
	private final int MIN_INTERVAL_TIME = 2000;
	private long startTime = 0;	//记录时间判断是否是有效录音
	WakeLock mWakeLock;//用WakeLock请求休眠锁，让其不会休眠
	private RecordHelper mRecorder;//录音类，实现录音功能

	private MySQLiteWorker sql = null;
	private RecordButton btnRecord = null;//录音按钮
	private Button btnUpload;//上传
	private Button btnAddPicture;//添加图片
	private Button btnPlayRecord;
	private Button btnNextPage;
	private Button btnrPage;
	private int nPage = 0;
	
	private ImageView iv_image = null;
	private RelativeLayout recordpage = null;
	
	
	
	private String strSampleFilePath;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_activity);
        
        recordpage = (RelativeLayout)findViewById(R.id.recordpageid);
        recordpage.setBackgroundResource(R.drawable.page1);
        
        imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
        sql = new MySQLiteWorker(this);
        sql.CreateDataTable(MySQLiteOpenHelper.MYRECORD_TABLE_NAME);
        sql.EnumDataBase(MySQLiteOpenHelper.MYRECORD_TABLE_NAME);
        setupViews();
        Initpage1();
    }
	
	public void Initpage1(){
		nPage = 1;
		btnPlayRecord.setVisibility(View.GONE);
		btnUpload.setVisibility(View.GONE);
		btnRecord.setVisibility(View.GONE);
		btnNextPage.setVisibility(View.VISIBLE);
		btnAddPicture.setVisibility(View.VISIBLE);
		recordpage.setBackgroundResource(R.drawable.page1);
	}
	
	public void Initpage2(){
		nPage = 2;
		btnPlayRecord.setVisibility(View.GONE);
		btnUpload.setVisibility(View.GONE);
		btnRecord.setVisibility(View.VISIBLE);
		btnNextPage.setVisibility(View.GONE);
		btnAddPicture.setVisibility(View.GONE);
		recordpage.setBackgroundResource(R.drawable.page2);
	}
	
	public void Initpage3(){
		nPage = 3;
		btnPlayRecord.setVisibility(View.VISIBLE);
		btnUpload.setVisibility(View.GONE);
		btnRecord.setVisibility(View.VISIBLE);
		btnNextPage.setVisibility(View.VISIBLE);
		btnAddPicture.setVisibility(View.GONE);
		recordpage.setBackgroundResource(R.drawable.page3);
	}
	
	public void Initpage4(){
		nPage = 4;
		btnPlayRecord.setVisibility(View.GONE);
		btnUpload.setVisibility(View.VISIBLE);
		btnRecord.setVisibility(View.GONE);
		btnNextPage.setVisibility(View.GONE);
		btnAddPicture.setVisibility(View.GONE);
		recordpage.setBackgroundResource(R.drawable.page4);
	}
	
	public void startPlayback(String strfilname)
	{
		mRecorder.startPlayback(strfilname);
	}

	//初始化
	@SuppressWarnings("deprecation")
	public void setupViews() {
		
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
				"SoundRecorder");//请求休眠锁
		
		//初始化播放控件
		mRecorder = new RecordHelper();
		mRecorder.setOnStateChangedListener(this);
		
		//根据ID找到相应控件
		btnRecord = (RecordButton) findViewById(R.id.ButtonRecord_btn);
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		path += RECORD_FILE_NAME;//"/mmmm.amr"
		btnRecord.setSavePath(path);
		btnRecord.setOnFinishedRecordListener(new OnFinishedRecordListener() {
					@Override
					public void onFinishedRecord(String audioPath) {
						//录音完成
						Initpage3();
						Log.i("RECORD!!!", "finished!!!!!!!!!! save to "
								+ audioPath);
					}
				});
		btnRecord.setOnClickListener(this);
		btnPlayRecord = (Button) findViewById(R.id.ButtonPlayRecord);
		btnPlayRecord.setOnClickListener(this);
		
		btnAddPicture = (Button) findViewById(R.id.ButtonAddPicture);
		btnAddPicture.setOnClickListener(this);
		
		btnUpload = (Button) findViewById(R.id.ButtonUpload);
		btnUpload.setOnClickListener(this);
		
		btnNextPage = (Button)findViewById(R.id.ButtonNextPage);
		btnNextPage.setOnClickListener(this);
       
		btnrPage = (Button)findViewById(R.id.IdButtonRePage);
		btnrPage.setOnClickListener(this);
		
		iv_image = (ImageView) findViewById(R.id.ViewPictureInRecord);

	}

	@Override 
	public boolean onContextItemSelected(MenuItem item) {  
		  
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item  
                .getMenuInfo();  
        int MID = (int) info.id; 
        
        switch (item.getItemId()) {  
            case 0:  
                Toast.makeText(this,  
                        "menuItem1",  
                        Toast.LENGTH_SHORT).show();  
                //启动设置窗口
                break;  
  
            case 1:  
                Toast.makeText(this,  
                        String.valueOf(MID),  
                        Toast.LENGTH_SHORT).show();  
                //删除数据，并刷新listview
               
               
                break;  
            default:  
                break;  
        }  
  
        return super.onContextItemSelected(item);  
  
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
		if (v.getId() == R.id.ButtonRecord_btn)
		{
			if (event.getAction() == MotionEvent.ACTION_DOWN)
			{
				mRecorder.startRecording(MediaRecorder.OutputFormat.DEFAULT,
	   					".mp3", this);
				startTime = System.currentTimeMillis();
				//这里还可以进行其他操作
				//开启录音动画并计时
			}
			if (event.getAction() == MotionEvent.ACTION_UP)
			{
				//btnStart.setBackgroundResource(R.color.green);
				mRecorder.stopRecording();
				strSampleFilePath = mRecorder.getSampleFile();
				long intervalTime = System.currentTimeMillis() - startTime;
				if (intervalTime < MIN_INTERVAL_TIME) {
					Toast.makeText(getBaseContext(), "时间太短！", Toast.LENGTH_SHORT).show();
					File file = new File(strSampleFilePath);
					file.delete();
					return false;
				}
				//获取文件路径
				Initpage3();
			}
		}
		return false;
	}

	@Override
	public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		
		// TODO Auto-generated method stub
		int ID = v.getId();
		if (ID == R.id.ButtonRecord_btn){
		}
		if (ID == R.id.ButtonPlayRecord){
			startPlayback(Environment.getExternalStorageDirectory()
					.getAbsolutePath() +  RECORD_FILE_NAME);
		}
		if (ID == R.id.ButtonAddPicture){
			
			LayoutInflater layoutInflater = LayoutInflater.from(newrecord.this);   
            View myLoginView = layoutInflater.inflate(R.layout.popdlg, null);   
            new AlertDialog.Builder(newrecord.this)  
            .setTitle("选择")  
            .setItems(way, new DialogInterface.OnClickListener() {  
                public void onClick(DialogInterface dialog, int which) {  
                   switch(which){
                   		case 0:
                   			OpenCamera();
                	   		break;
                   		case 1:
                   			OpenAlbum();
                	   		break;
                   		default:
                   			break;
                   }
                }  
            }  
            ).setView(myLoginView).create().show();
			
		}		
		if (ID == R.id.ButtonUpload){
			
			String recordpath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/" + DateFormat.format("yyyyMMdd_hhmmss",
					Calendar.getInstance(Locale.CHINA))+"_record.mp3";
			String imagepath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/" + DateFormat.format("yyyyMMdd_hhmmss",
					Calendar.getInstance(Locale.CHINA))+"_image.png";
			
			//拷贝声音和图片文件到响应的文件夹
			SavePicAndRecord(recordpath, imagepath);
			//在数据库中加入数据
			InsertDataBase(recordpath, imagepath);
			//refreshDataBase();
			//刷新数据库、
			//退出
			finish();
		}
		
		if (ID == R.id.ButtonNextPage){
			if (nPage == 1){
				Initpage2();
			}else if (nPage == 2){
				Initpage3();
			}else if (nPage == 3){
				Initpage4();
			}else if (nPage == 4){
				//完成加载
			}
		}
		if (ID == R.id.IdButtonRePage){
			if (nPage == 1){
				//退出
				finish();
			}else if (nPage == 2){
				Initpage1();
			}else if (nPage == 3){
				Initpage2();
			}else if (nPage == 4){
				Initpage3();
				//完成加载
			}
		}
	}
	
	private void InsertDataBase(String recordpath, String imagepath){
		
		String NAME = "name";
		String RECORD_DIR = "record_dir";
		String PICTURE_DIR = "picture_dir";
		String TIME = "time";
		String WAY = "way";
		String BEIZHU = "beizhu";
		String ZHENDONG = "zhendong";
		String CHONGFU = "chongfu";
		String LINGSHEN_DIR = "lingshen_dir";
		String CLOCKTIME = "clocktime";
		String USERNAME = "username";
		String UPLOADTIME = "uploadtime";
		String HEAD_DIR = "head_dir";
		String USE_TIMES = "use_time";
		
		ContentValues cv = new ContentValues();
		String lingsheng_item = recordpath;
		//闹钟时间
		String clockTime = "0" +":"+ "0"; 
		cv.put(NAME, "0");
		cv.put(RECORD_DIR, "0");
		cv.put(PICTURE_DIR, imagepath);
		cv.put(TIME, "0");
		cv.put(WAY, "0");
		cv.put(BEIZHU, "备注");
		cv.put(ZHENDONG, "震动");
		cv.put(CHONGFU, "重复");
		cv.put(LINGSHEN_DIR, lingsheng_item);
		cv.put(CLOCKTIME, clockTime);
		cv.put(USERNAME, "0");
		cv.put(UPLOADTIME, "0");
		cv.put(HEAD_DIR, "0");
		cv.put(USE_TIMES, "0");
		sql.InsertData(MySQLiteOpenHelper.MYRECORD_TABLE_NAME, cv);
		
	}
	
	private void SavePicAndRecord(String recordpath, String imagepath){
		copyFile(Environment.getExternalStorageDirectory()
				.getAbsolutePath() +  RECORD_FILE_NAME, recordpath);
		copyFile(Environment.getExternalStorageDirectory()
				.getAbsolutePath() +  IMAGE_FILE_NAME, imagepath);
	}
	
	public void copyFile(String oldPath, String newPath) {   
		Log.i("11111111111111", "111111111111111111111");
		Log.i(oldPath, newPath);
	       try {   
	           int bytesum = 0;   
	           int byteread = 0;   
	           File oldfile = new File(oldPath);   
	           if (oldfile.exists()) { //文件存在时   
	               InputStream inStream = new FileInputStream(oldPath); //读入原文件   
	               FileOutputStream fs = new FileOutputStream(newPath);   
	               byte[] buffer = new byte[1024];     
	               while ( (byteread = inStream.read(buffer)) != -1) {   
	                   bytesum += byteread; //字节数 文件大小   
	                   fs.write(buffer, 0, byteread);   
	               }   
	               inStream.close();   
	           }
	       }   
	       catch (Exception e) {   
	           System.out.println("复制单个文件操作出错");   
	           e.printStackTrace();   
	       }   
	  
	   }   
	
	private void OpenCamera(){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, TAKE_BIG_PICTURE);
	
	}
	
	private void OpenAlbum(){
		GetBigPicture();
	}


   
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
        	Bitmap bitmap = null;
            switch (requestCode) {

            case CHOOSE_BIG_PICTURE:
            	
            	 if(imageUri != null){
            		  bitmap = decodeUriAsBitmap(imageUri);//decode bitmap
            		  iv_image.setImageBitmap(bitmap);
            		  //存入文件系统
            		  try {
						savePhotoToSDCard(Environment.getExternalStorageDirectory()
						  		.getAbsolutePath(), IMAGE_FILE_NAME, bitmap);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            		  //
            		 }
            	break;
            case TAKE_BIG_PICTURE:
            		if(imageUri != null){
            			 cropImageUri(imageUri, 700, 800, CHOOSE_BIG_PICTURE);
           		 	}
            	break;
            default:
                break;
            }
        }
    }
	public void GetBigPicture(){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 800);
		intent.putExtra("outputY", 650);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, CHOOSE_BIG_PICTURE);
	}
	
	private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode){
		 Intent intent = new Intent("com.android.camera.action.CROP");
		 intent.setDataAndType(uri, "image/*");
		 intent.putExtra("crop", "true");
		 intent.putExtra("aspectX", 1);
		 intent.putExtra("aspectY", 1);
		 intent.putExtra("outputX", outputX);
		 intent.putExtra("outputY", outputY);
		 intent.putExtra("scale", true);
		 intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		 intent.putExtra("return-data", false);
		 intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		 intent.putExtra("noFaceDetection", true); // no face detection
		 startActivityForResult(intent, requestCode);
		}

	/**Save image to the SD card**/
    public void savePhotoToSDCard (String path, String photoName, Bitmap photoBitmap)throws IOException {

    	File f = new File(path + photoName);  
    	if (f.exists()){
    		f.delete();
    	}
        f.createNewFile();  
        FileOutputStream fOut = null;  
        try {  
                fOut = new FileOutputStream(f);  
        } catch (FileNotFoundException e) {  
                e.printStackTrace();  
        }  
        photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);  
        try {  
                fOut.flush();  
        } catch (IOException e) {  
                e.printStackTrace();  
        }  
        try {  
                fOut.close();  
        } catch (IOException e) {  
                e.printStackTrace();  
        }  
        
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
