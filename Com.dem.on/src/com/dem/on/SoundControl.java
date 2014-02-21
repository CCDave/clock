package com.dem.on;

import java.io.File;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class SoundControl {
	// 播放对象  
		public MediaPlayer mMediaPlayer = null;
		String music_dir;
		private int FLAG = 0;
		SoundControl(int flag){
			mMediaPlayer = new MediaPlayer();
			FLAG = flag;
		}
		
		public void playMusic(String path)
		{
			try
			{	music_dir = path;
				// 重置多媒体 
				mMediaPlayer.reset();
				//读取mp3文件
				mMediaPlayer.setDataSource(path);
				//准备播放
				mMediaPlayer.prepare();
				//开始播放
				mMediaPlayer.start();
				//监听播放是否完成
				mMediaPlayer.setOnCompletionListener(new OnCompletionListener()
				{
					@Override
					public void onCompletion(MediaPlayer mp)
					{
						// TODO Auto-generated method stub
						//播放完当前歌曲，自动重放
						if (FLAG != 0)
						{
							ReplayMusic();
						}
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
		private void ReplayMusic(){
			File file = new File(music_dir);
			if (file.exists()){
				playMusic(music_dir);
			}
		}
		public void pause(){
			if (mMediaPlayer.isPlaying()){
				mMediaPlayer.pause();
			}
		}
}
