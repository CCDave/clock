package com.dem.on;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.widget.SimpleAdapter;

import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
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
	
	private final String RECORD_FILE_NAME = "/mmmm.amr";//"/myTempClock.mp3"
	private final int MIN_INTERVAL_TIME = 2000;
	private long startTime = 0;	//记录时间判断是否是有效录音
	WakeLock mWakeLock;//用WakeLock请求休眠锁，让其不会休眠
	private RecordHelper mRecorder;//录音类，实现录音功能

	
	private RecordButton btnRecord = null;//录音按钮
	//private Button btnUpload;//上传
	private Button btnAddPicture;//添加图片
	private Button btnPlayRecord;
	private ImageView iv_image = null;
	//private Boolean brecord = false;
	
	private String strSampleFilePath;
	private MySQLiteOpenHelper sqlHelpRecord; //录音数据
	private SQLiteDatabase sqlRecord ; 	//录音数据库
	private ListView list; //列表框
	
	private ArrayList<HashMap<String, Object>> listItem;
	private SimpleAdapter listItemAdapter;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_activity);
        imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
        setupDataBase();
        //CreateTable();
        setupViews();
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
		
		iv_image = (ImageView) findViewById(R.id.ViewPictureInRecord);
		//btnPlayRecord.setOnClickListener(this);
		//btnPlayRecord.setVisibility(View.GONE);

	}
	
	public void initlistview(){
		listItem = new ArrayList<HashMap<String, Object>>();
		list = (ListView) findViewById(R.id.ListViewRecord);
		listItemAdapter = new SimpleAdapter(this,listItem,
	            R.layout.recordlistitem,
	            
	            new String[] {"clickstartplayrecord"},   
	            
	            new int[] {R.id.clickstartplayrecord}  
	        );
		list.setAdapter(listItemAdapter); 
		list.setOnItemClickListener(new OnItemClickListener() {  
			  
            @Override  
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
                    long arg3) {  
            	HashMap<String, Object> map = listItem.get(arg2);
            	//播放音频
            	Object name = map.get("ItemClockTime");
            	Log.i("ddddddddddddddddddddddddddddddd",(String)name); 
            	startPlayback((String)name);
            }  
        }); 
		
		list.setOnItemLongClickListener(new ListView.OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.i("ddddddddddddddddddddddddddddddd","changdianji"); 
				return false;
			}
		});
		
		list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {  
            @Override  
            public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
                menu.add(0, 0, 0, "设置");  
                menu.add(0, 1, 0, "删除");     
            }  
        });    
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
                DeleteData(MID);
                refreshList();
               
                break;  
            default:  
                break;  
        }  
  
        return super.onContextItemSelected(item);  
  
    }  
	public void refreshList(){
		InitDataBase();
		listItemAdapter.notifyDataSetChanged();	
		listItemAdapter.notifyDataSetInvalidated();
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
				InsertData(strSampleFilePath, "1111");
				refreshList();
				Log.i("filadirectory",strSampleFilePath+"123123123123"); 
			}
		}
		return false;
	}
	
	public void setupDataBase() {
		sqlHelpRecord = new MySQLiteOpenHelper(this);// 实例一个数据库辅助器
		sqlRecord = sqlHelpRecord.getWritableDatabase();
	}
	
	//遍历数据库中的数据-并刷新列表
	public void InitDataBase() {
		Cursor cur = sqlRecord.rawQuery("SELECT * FROM "
				+ MySQLiteOpenHelper.TABLE_NAME, null);
		listItem.clear();

		if (cur != null) {
			String name = "";
			String time = "";
			
			while (cur.moveToNext()) {//直到返回false说明表中到了数据末尾
				name = cur.getString(0); 
				// 参数0 指的是列的下标,这里的0指的是id列
				time = cur.getString(1);
				// 这里的0相对于当前应该是咱们的text列了
				HashMap<String, Object> map = new HashMap<String, Object>();  
				map.put("ItemClockName", name);
				map.put("ItemClockTime", time);
				listItem.add(map);  
			}
		}
	}
	
	public void InsertData(String name, String time){
		ContentValues cv = new ContentValues();
		cv.put(MySQLiteOpenHelper.NAME, name);
		cv.put(MySQLiteOpenHelper.TIME, time);
		sqlRecord.insert(MySQLiteOpenHelper.TABLE_NAME, null, cv);
	}
	
	public void DeleteData(int index){
		sqlRecord.delete("himi", MySQLiteOpenHelper.ID + "=" + String.valueOf(index), null);
	}
	
	public void UpdateData(String name, String time){
		ContentValues cv = new ContentValues();
		cv.put(MySQLiteOpenHelper.NAME, name);
		cv.put(MySQLiteOpenHelper.TIME, time);
		sqlRecord.update("himi", cv, "id " + "=" + Integer.toString(3), null);
	}
	
	public void CreateTable(){
		String TABLE_NAME = "himi";
		String ID = "id";
		String TEXT = "text";
		String str_sql = "CREATE TABLE " + TABLE_NAME + "(" + ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + 
				"name" + " text ,"+
				"time" + " text ,"+
				TEXT + " text );";
		
		sqlRecord.execSQL(str_sql);
	}
	
	public void DropTable(){
		sqlRecord.execSQL("DROP TABLE himi");
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
    public static void savePhotoToSDCard(String path, String photoName, Bitmap photoBitmap) {
    	
    	
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
        	File dir = new File(path);
            if (!dir.exists()) {	
                dir.mkdirs();
            }
            File photoFile = new File(path, photoName); //在指定路径下创建文件
            FileOutputStream fileOutputStream = null;
            try {
            	if (!photoFile.canWrite())
            		return;
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100,
                            fileOutputStream)) {
                        fileOutputStream.flush();
                    }
                }
            } catch (FileNotFoundException e) {
                photoFile.delete();
                e.printStackTrace();
            } catch (IOException e) {
                photoFile.delete();
                e.printStackTrace();
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
