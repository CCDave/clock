package com.dem.on;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
 
public class musicActivity extends ListActivity
{
	private ImageButton startc;      //播放
	private ImageButton stop;		//停止
	private ImageButton sound;		//声音
	private ImageButton next;  		//下一曲
	private ImageButton last;     	//上一曲
	private ImageButton selected;    //选择
	
	public static final int CURRENTVOLUME=0;//当前音量
	public static int currentVolume = CURRENTVOLUME;
	private static final int currentVolumeNO=1;//静音
	private static final int currentVolumeOFF=2;//当前音量
	
	//定义进度handler，显示百分比进度 
	Handler mPercentHandler = new Handler();  

	// 定义拖动条 
	private SeekBar mSeekBar = null;
	
	//定义显示文本框 
	private TextView curtimeAndTotaltime = null;
	private TextView curtimeandtotaltime1 = null;
	// 播放对象  
	public MediaPlayer mMediaPlayer = null;
	//歌名显示
	private TextView musicname;
	// 播放列表,用来存放从指定文件中搜索到的文件
	private List<String> myMusicList = new ArrayList<String>();
	private List<String> myMusicTitleList = new ArrayList<String>();
	// 定义在播放列表中的当前选择项
	private int currentListItem = 0;
	// 音乐的路径 虚拟器
	private static String MUSIC_PATH = new String("");
	
	private int[] _ids;       //存放音乐文件的id数组
	private String[]  _titles; //存放音乐文件的标题数组
	private String[]  _path;   //存放音乐文件的路径
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去除Activity名称
		setContentView(R.layout.select_local_music);
		
		//更新播放列表
		musicList();
		//初始化多媒体对象
		mMediaPlayer = new MediaPlayer();
	
		//初始化图像按钮 
		startc = (ImageButton) findViewById(R.id.startc);
		stop = (ImageButton) findViewById(R.id.stop);
		next = (ImageButton) findViewById(R.id.next);
		sound = (ImageButton) findViewById(R.id.sound);
		last = (ImageButton) findViewById(R.id.last);
		musicname = (TextView) findViewById(R.id.musicname);
		selected = (ImageButton) findViewById(R.id.selected);
		
		//初始化拖动条和当前进度显示值
		mSeekBar=(SeekBar)findViewById(R.id.SeekBar01);
		curtimeAndTotaltime=(TextView)findViewById(R.id.curtimeandtotaltime);
		curtimeandtotaltime1=(TextView)findViewById(R.id.curtimeandtotaltime1);
		   
		//监听拖动条 
		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser)
			{
				// TODO Auto-generated method stub
				//如果拖动进度发生改变，则显示当前进度值 
				mPercentHandler.post(updatesb);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{
				// TODO Auto-generated method stub
				//正在拖动进度音乐暂停
				mMediaPlayer.pause(); 
				mPercentHandler.post(updatesb);
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{
				// TODO Auto-generated method stub
				int dest = seekBar.getProgress();
				int mMax = mMediaPlayer.getDuration();
				int sMax = mSeekBar.getMax();
				//拖动进度完成音乐播放
				mMediaPlayer.start();
				mMediaPlayer.seekTo(mMax * dest / sMax);

			}
		});
		
		//静音 恢复音量
		sound.setOnClickListener(new OnClickListener()
		{
			
			public void onClick(View v)
			{
				switch (currentVolume) 
				{					
					case CURRENTVOLUME:
						currentVolume = currentVolumeNO;
						mMediaPlayer.setVolume(0,0);
						sound.setImageResource(R.drawable.mute);
						break;
					
					case currentVolumeNO:	
						currentVolume = currentVolumeOFF;
						mMediaPlayer.setVolume(getDeviceSound(), getDeviceSound());
						sound.setImageResource(R.drawable.sound);
						
						break;
					
					case currentVolumeOFF:	
						currentVolume = currentVolumeNO;
						sound.setImageResource(R.drawable.mute);
						mMediaPlayer.setVolume(0,0);
						break;
				}
				
			}
		});
		// 停止
		stop.setOnClickListener(new OnClickListener()
		{

			
			@Override
			public void onClick(View v)
			{
				mMediaPlayer.seekTo(0);
				mMediaPlayer.pause();
				curtimeAndTotaltime.setText("00:00");
				curtimeandtotaltime1.setText("00:00");
				startc.setImageResource(R.drawable.start);
				
			}
		});
		// 开始
		startc.setOnClickListener(new OnClickListener()
		{
			
				@Override
				public void onClick(View v)
				{
						if(mMediaPlayer.isPlaying())
						{
							
							//正在播放就暂停播放且切换图片												
							mMediaPlayer.pause();
							startc.setImageResource(R.drawable.start);			
							
						}
						else 
						{
							mPercentHandler.post(updatesb);
							
							//已经暂停就开始播放且切换图片
							mMediaPlayer.start();
							startc.setImageResource(R.drawable.pause);
							//开始播放歌曲时，同步进行更新拖动条进度
							startSeekBarUpdate();
						}
				}
			
			
		});
		// 下一首
		next.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				nextMusic();
				mPercentHandler.post(updatesb);
			}
		});

		// 上一首
		last.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub

				lastMusic();
				mPercentHandler.post(updatesb);
			}
		});
		
		// 选择当前歌曲并关闭对话框
		selected.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//判断文件路径
				File file = new File(myMusicList.get(currentListItem));
				  if (file.exists()) {
					//给出提示
					//结束
					  Uri data = Uri.parse(myMusicList.get(currentListItem));
						  Intent result = new Intent(null, data);
						  result.putExtra("mic_dir", myMusicList.get(currentListItem));
						  result.putExtra("mic_name", myMusicTitleList.get(currentListItem));
						  setResult(RESULT_OK, result);
						  mMediaPlayer.seekTo(0);
						  mMediaPlayer.pause();
						  finish();
					return ;
				  }
				  Toast.makeText(musicActivity.this, "请选择声音！",
							Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	
	// 下一首
	void nextMusic()               
	{
		MusicName();
		if (++currentListItem >= myMusicList.size())
		{
			currentListItem = 0;
			Toast.makeText(musicActivity.this, "已是最后一首",
					Toast.LENGTH_SHORT).show();
			
		} else
		{
			playMusic(MUSIC_PATH + myMusicList.get(currentListItem));
			
		}
	}

	// 上一首
	void lastMusic()
	{
		// 判断是否是第一首歌
		if (currentListItem != 0)
		{
			MusicName();
			if (--currentListItem >= 0)
			{
				
				playMusic(MUSIC_PATH +  myMusicList.get(currentListItem));
			} 
			else
			{
				playMusic(MUSIC_PATH + myMusicList.get(currentListItem));
			}
		} else
		{
			Toast.makeText(musicActivity.this, "已是第一首", Toast.LENGTH_SHORT)
			.show();
		}
	}
	
	
	// 绑定音乐  更新播放列表
	public void musicList()
	{
		try
		{
			Cursor c = this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
					new String[]{MediaStore.Video.Media.TITLE,        //音乐名
					MediaStore.Audio.Media.DURATION,                        //音乐的总时间
					MediaStore.Audio.Media.ARTIST,                        //艺术家
					MediaStore.Audio.Media._ID,                                //id号
					MediaStore.Audio.Media.DISPLAY_NAME,                //音乐文件名
					MediaStore.Audio.Media.DATA//音乐文件的路径
					}, 
					null,             //查询条件，相当于sql中的where语句
					null, //查询条件中使用到的数据
					null);//查询结果的排序方式	
			
			c.moveToFirst();
			_ids = new int[c.getCount()];
			_titles = new String[c.getCount()];
			_path = new String[c.getCount()];
			for(int i=0;i<c.getCount();i++){
			        _ids[i] = c.getInt(3);
			        _titles[i] = c.getString(0);
			        _path[i] = c.getString(5).substring(4);
			        myMusicTitleList.add(c.getString(0));
			        myMusicList.add(c.getString(5));
			        c.moveToNext();
			}
			
			ArrayAdapter<String> musicList = new ArrayAdapter<String>(
					musicActivity.this, R.layout.musicitme, myMusicTitleList);	
			setListAdapter(musicList);
		}
		 catch (Exception ex)
		{
			 //\n请设置路径为: /sdcard/music/mp3"+ ex.getMessage()
			this.ShowDialog("音乐列表加载失败!");
		}
		
		/*这个字符串数组表示要查询的列
		try
		{
			//从指定的路径中读取文件，并与播放列表关联 
			File home = new File(MUSIC_PATH);
			//读取指定类型的文件
			if (home.listFiles(new MusicFilter()).length > 0)
			{
				//读取文件 
				for (File file : home.listFiles(new MusicFilter()))
				{
					myMusicList.add(file.getName());
				}
				//播放文件与播放列表关联 
				ArrayAdapter<String> musicList = new ArrayAdapter<String>(
						musicActivity.this, R.layout.musicitme, myMusicList);	
				setListAdapter(musicList);
			}
		}
		 catch (Exception ex)
		{
			this.ShowDialog("音乐列表加载失败!\n请设置路径为: /sdcard/music/mp3"+ ex.getMessage());
		}*/
	}
	public void ShowDialog(String str)
	{
		new AlertDialog.Builder(this).setTitle("提示").setMessage(str)
				.setPositiveButton("OK", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
					}
				}).show();
	}


	// 播放指定路径中的音乐
	private void playMusic(String path)
	{
		try
		{
			MusicName();
			// 重置多媒体 
			mMediaPlayer.reset();
			//读取mp3文件
			mMediaPlayer.setDataSource(path);
			//准备播放
			mMediaPlayer.prepare();
			//开始播放
			mMediaPlayer.start();
			//更新进度条时间
			mPercentHandler.post(updatesb);
			//监听播放是否完成
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener()
			{

				@Override
				public void onCompletion(MediaPlayer mp)
				{
					// TODO Auto-generated method stub
					//播放完当前歌曲，自动播放下一首
					//nextMusic();
				}
			});
			// 循环播放
			mMediaPlayer.setLooping(false);
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer.prepare();
		} 
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	// 选择项监听方法，即当前鼠标在列表中选择的第几项
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		// TODO Auto-generated method stub
		currentListItem = position;
		playMusic(MUSIC_PATH + myMusicList.get(currentListItem));
		MusicName();
		v.setSelected(true);
		startc.setImageResource(R.drawable.pause);
	}
	//显示歌曲名称
	public void MusicName()
	{
		musicname.setText(myMusicList.get(currentListItem));
	}
	//声音
	public float getDeviceSound()
	{
		AudioManager mgr = (AudioManager) getBaseContext().getSystemService(
				Context.AUDIO_SERVICE);
		float streamVolumeCurrent = mgr
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		float streamVolumeMax = mgr
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);// 设置最大音量
		float volume = streamVolumeCurrent / streamVolumeMax; // 设备的音量
		return volume;
	}
	
	//更新拖动条进度 
	public void startSeekBarUpdate()
	{
		mPercentHandler.post(start);
	}

	Runnable start = new Runnable()
	{

		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			mPercentHandler.post(updatesb);
			// 用一个handler更新SeekBar
		}

	};

	Runnable updatesb = new Runnable()
	{

		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			int position = mMediaPlayer.getCurrentPosition();
			int mMax = mMediaPlayer.getDuration();
			int sMax = mSeekBar.getMax();
			mSeekBar.setProgress(position * sMax / mMax);
			curtimeAndTotaltime.setText(toTime(mMediaPlayer.getCurrentPosition()));
			curtimeandtotaltime1.setText(toTime(mMediaPlayer.getDuration()));
			mPercentHandler.postDelayed(updatesb, 1000);
			// 每秒钟更新一次
		}
     
	};
	public static String toTime(int time)
	{

		time /= 1000;
		int minute = time / 60;
		int hour = minute / 60;
		int second = time % 60;
		minute %= 60;
		return String.format("%02d:%02d", minute, second);
	}
	
	//按键处理时间,当按下返回按键时的处理方法 
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(musicActivity.this);
			builder.setTitle("是否退出");
			builder.setIcon(R.drawable.icon);
			builder.setPositiveButton("是", 
					new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface arg0, int arg1)
						{
							// TODO Auto-generated method stub
							mMediaPlayer.pause();
							finish();
							onDestroy();
							
						}
					});
			
			builder.setNegativeButton("否", 
					new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface arg0, int arg1)
						{
							// TODO Auto-generated method stub
					
							
						}
					});
			builder.create().show();
			return true;
		}
		return false;
	}
	
	// 判断Menu菜单
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// TODO Auto-generated method stub	
		menu.add(0, 2, 2, R.string.exit);
		menu.add(0, 1, 1, R.string.about);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO Auto-generated method stub

		if (item.getItemId() == 1)
		{
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);

			dialog.setTitle("关于 Magic Groan").setMessage("简单的音乐播放器给大家参考下!")
					.show();

		}
		if (item.getItemId() == 2)
		{
			mMediaPlayer.pause();
			finish();
			onDestroy();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}
