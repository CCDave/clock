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

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnTouchListener;


import com.dem.on.R;
import com.dem.on.RecordHelper;
import com.dem.on.MySQLiteOpenHelper;
import com.dem.on.RecordButton;
import com.dem.on.RecordButton.OnFinishedRecordListener;
import com.dem.on.setup.MyAdapter;
import com.dem.on.setup.ViewHolder;
import com.dem.on.setup.ViewHolderHead;
import com.dem.on.config;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.content.ContentValues;


public  class record extends Activity implements OnTouchListener, OnClickListener,
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
	private ImageView iv_image = null;
	
	private String strSampleFilePath;
	
	private ListView list = null;
	private ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recordhome_activity);
        imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
        sql = new MySQLiteWorker(this);
        sql.CreateDataTable(MySQLiteOpenHelper.MYRECORD_TABLE_NAME);
        sql.EnumDataBase(MySQLiteOpenHelper.MYRECORD_TABLE_NAME);
        //setupViews();
        InitListView();
    }
	
	private void InitListView(){
		list = (ListView) findViewById(R.id.ListViewUserData);
		list.setVerticalScrollBarEnabled(false);
		
		HashMap<String, Object> map = new HashMap<String, Object>();  
        map.put("ItemBigPicture_head", R.drawable.wenzhang);
        listItem.add(map);
        
        
        HashMap<String, Object> map1 = new HashMap<String, Object>();  
        map1.put("Itemtouxiang", R.drawable.gaoyuanyuan);
        map1.put("huangguan", R.drawable.wenzhang);
        map1.put("ItemBigPicture", R.drawable.gaoyuanyuan);
        map1.put("Itemusername", "谢娜");
        map1.put("Itemchenghao", "叫醒宗师");
        map1.put("Itemshijian", "2/22 13:50");
        
        map1.put("Itemleijijiaoxing_shu", "56312728");
        map1.put("Itemjiaoxinglv_shu", "91%");
        map1.put("ItemButtonYuyin", "语音");
        map1.put("ItemButtonDaoju", "道具");
       
        listItem.add(map1);

        MyAdapter listItemAdapter = new MyAdapter(this);

        list.setAdapter(listItemAdapter);  
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
			
			LayoutInflater layoutInflater = LayoutInflater.from(record.this);   
            View myLoginView = layoutInflater.inflate(R.layout.popdlg, null);   
            new AlertDialog.Builder(record.this)  
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
		intent.putExtra("outputX", 700);
		intent.putExtra("outputY", 800);
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
    
    public class MyAdapter extends BaseAdapter {  
		  
        private LayoutInflater mInflater;  
  
        public MyAdapter(Context context) {  
            this.mInflater = LayoutInflater.from(context);  
        }  
  
        @Override  
        public int getCount() {  
            // TODO Auto-generated method stub  
            return listItem.size();  
        }  
  
        @Override  
        public Object getItem(int position) {  
            // TODO Auto-generated method stub  
            return null;  
        }  
  
        @Override  
        public long getItemId(int position) {  
            // TODO Auto-generated method stub  
            return 0;  
        }  
        //****************************************final方法  
        //注意原本getView方法中的int position变量是非final的，现在改为final  
        @Override  
        public View getView(final int position, View convertView, ViewGroup parent) {  
             
        	if(position == 0){
        		ViewHolderHead holder = null;  
                if (convertView == null) { 
                    holder = new ViewHolderHead();    
                    //可以理解为从vlist获取view  之后把view返回给ListView  
                    convertView = mInflater.inflate(R.layout.userdatahead, null);  
                    
                    holder.ItemBigPicture_head = (ImageView)convertView.findViewById(R.id.ItemBigPicture_head);  
                    holder.ItemButtonPlayRecord_head = (Button)convertView.findViewById(R.id.ItemButtonPlayRecord_head);  
                    holder.ItemButtonLike_head = (Button)convertView.findViewById(R.id.ItemButtonLike_head);  
                    holder.ItemButtonListen_head = (Button)convertView.findViewById(R.id.ItemButtonListen_head);  
                    holder.ItemButtonTalk_head = (Button)convertView.findViewById(R.id.ItemButtonTalk_head); 
                    
                    convertView.setTag(holder);               
                }else {               
                    holder = (ViewHolderHead)convertView.getTag();  
                }         
                  
                holder.ItemBigPicture_head.setBackgroundResource((Integer)listItem.get(position).get("ItemBigPicture_head"));  
                
                return convertView;
        	}else{
        		
        		ViewHolder holder = null;  
                if (convertView == null) { 
                    holder = new ViewHolder();    
                    //可以理解为从vlist获取view  之后把view返回给ListView  
                    convertView = mInflater.inflate(R.layout.userdatalistitem, null);  
                    
                    holder.Itemtouxiang = (ImageButton)convertView.findViewById(R.id.Itemtouxiang);  
                    holder.huangguan = (ImageButton)convertView.findViewById(R.id.huangguan);  
                    holder.ItemBigPicture = (ImageView)convertView.findViewById(R.id.ItemBigPicture);  
                    
                    holder.Itemusername = (TextView)convertView.findViewById(R.id.Itemusername);  
                    holder.Itemchenghao = (TextView)convertView.findViewById(R.id.Itemchenghao); 
                    holder.Itemshijian = (TextView)convertView.findViewById(R.id.Itemshijian); 
                    
                    
                    holder.Itemleijijiaoxing_shu = (TextView)convertView.findViewById(R.id.Itemleijijiaoxing_shu);
                    holder.Itemjiaoxinglv_shu = (TextView)convertView.findViewById(R.id.Itemjiaoxinglv_shu);
                    holder.ItemButtonYuyin = (Button)convertView.findViewById(R.id.ItemButtonYuyin);
                    holder.ItemButtonDaoju = (Button)convertView.findViewById(R.id.ItemButtonDaoju);
                   
                    
                    convertView.setTag(holder);               
                }else {               
                    holder = (ViewHolder)convertView.getTag();  
                }         
                
               /**/ holder.Itemtouxiang.setBackgroundResource((Integer)listItem.get(position).get("Itemtouxiang"));
                holder.huangguan.setBackgroundResource((Integer)listItem.get(position).get("huangguan"));
                holder.ItemBigPicture.setBackgroundResource((Integer)listItem.get(position).get("ItemBigPicture"));
                
                holder.Itemusername.setText((String)listItem.get(position).get("Itemusername"));
                holder.Itemchenghao.setText((String)listItem.get(position).get("Itemchenghao"));
                holder.Itemshijian.setText((String)listItem.get(position).get("Itemshijian"));

                holder.Itemleijijiaoxing_shu.setText((String)listItem.get(position).get("Itemleijijiaoxing_shu"));
                holder.Itemjiaoxinglv_shu.setText((String)listItem.get(position).get("Itemjiaoxinglv_shu"));
                holder.ItemButtonYuyin.setText((String)listItem.get(position).get("ItemButtonYuyin"));
                holder.ItemButtonDaoju.setText((String)listItem.get(position).get("ItemButtonDaoju"));
                
                return convertView;
        	}
        }  
    }  
	
	public final class ViewHolder {   
        public ImageButton Itemtouxiang;  
        public ImageButton huangguan;  
        public ImageView ItemBigPicture;
        
        public TextView Itemusername;  
        public TextView Itemchenghao; 
        public TextView Itemshijian;
        
        public TextView Itemleijijiaoxing_shu;
        public TextView Itemjiaoxinglv_shu;
        public Button ItemButtonYuyin;
        public Button ItemButtonDaoju;
        
    }
	
	
	public final class ViewHolderHead {   
        public ImageView ItemBigPicture_head;  
        public Button ItemButtonPlayRecord_head;  
        public Button ItemButtonLike_head;  
        public Button ItemButtonListen_head; 
        public Button ItemButtonTalk_head;
    }
}
