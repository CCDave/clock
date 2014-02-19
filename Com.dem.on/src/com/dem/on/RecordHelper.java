package com.dem.on;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

public class RecordHelper implements OnCompletionListener, OnErrorListener {
   // static final String SAMPLE_PREFIX = "recording";
    static final String SAMPLE_PATH_KEY = "sample_path";
    static final String SAMPLE_LENGTH_KEY = "sample_length";

    public static final int IDLE_STATE = 0;
    public static final int RECORDING_STATE = 1;
    public static final int PLAYING_STATE = 2;
    
    int mState = IDLE_STATE;

    public static final int NO_ERROR = 0;
    public static final int SDCARD_ACCESS_ERROR = 1;
    public static final int INTERNAL_ERROR = 2;
    public static final int IN_CALL_RECORD_ERROR = 3;

    
    public interface OnStateChangedListener {
        public void onStateChanged(int state);
        public void onError(int error);
    }
    OnStateChangedListener mOnStateChangedListener = null;
    
    long mSampleStart = 0;       // time at which latest record or play operation started 最新的记录时间或者玩操作开始
    int mSampleLength = 0;      // length of current sample 长度的当前样本
    File mSampleFile = null;
    
    MediaRecorder mRecorder = null;
    MediaPlayer mPlayer = null;
    
    public RecordHelper() {
    }
    
    public void saveState(Bundle recorderState) {
        recorderState.putString(SAMPLE_PATH_KEY, mSampleFile.getAbsolutePath());
        recorderState.putInt(SAMPLE_LENGTH_KEY, mSampleLength);
    }
    
    public int getMaxAmplitude() {
        if (mState != RECORDING_STATE)
            return 0;
        return mRecorder.getMaxAmplitude();
    }
    
    public void restoreState(Bundle recorderState) {
        String samplePath = recorderState.getString(SAMPLE_PATH_KEY);
        if (samplePath == null)
            return;
        int sampleLength = recorderState.getInt(SAMPLE_LENGTH_KEY, -1);
        if (sampleLength == -1)
            return;

        File file = new File(samplePath);
        if (!file.exists())
            return;
        if (mSampleFile != null
                && mSampleFile.getAbsolutePath().compareTo(file.getAbsolutePath()) == 0)
            return;
        
        delete();
        mSampleFile = file;
        mSampleLength = sampleLength;

        signalStateChanged(IDLE_STATE);
    }
    
    public void setOnStateChangedListener(OnStateChangedListener listener) {
        mOnStateChangedListener = listener;
    }
    
    public int state() {
        return mState;
    }
    
    public int progress() {
        if (mState == RECORDING_STATE || mState == PLAYING_STATE)
            return (int) ((System.currentTimeMillis() - mSampleStart)/1000);
        return 0;
    }
    
    public int sampleLength() {
        return mSampleLength;
    }

    public File sampleFile() {
       Log.i("ss",mSampleFile+"1111111111111111111111"); 
       return mSampleFile;
        
    }
    
    /**
     * 重置记录器状态。如果一个样本被记录,文件被删除。
     */
    public void delete() {
        stop();
        
        if (mSampleFile != null)
            mSampleFile.delete();

        mSampleFile = null;
        mSampleLength = 0;
        
        signalStateChanged(IDLE_STATE);
    }
    
    /**
     * Resets the recorder state. If a sample was recorded, the file is left on disk and will 
     * be reused for a new recording.
     * 
     * 重置记录器状态。如果一个样本被记录,文件是留在磁盘并将重用一个新的记录。
     */
    public void clear() {
        stop();
        
        mSampleLength = 0;
        
        signalStateChanged(IDLE_STATE);
    }
    
    public void startRecording(int outputfileformat, String extension, Context context) {
        stop();
        
        
		mSampleFile = new File(DateFormat.format("yyyyMMdd_hhmmss",
				Calendar.getInstance(Locale.CHINA))+"");
        Log.i("ss",mSampleFile+"1111111111111111111111111");
        
		String aString=DateFormat.format("yyyyMMdd_hhmmss",
				Calendar.getInstance(Locale.CHINA))+"";
        File sd=Environment.getExternalStorageDirectory(); 
        String path=sd.getPath()+"/Recording"; 
        File file=new File(path); 
        if(!file.exists()) 
        	file.mkdir(); 
        
            File sampleDir = Environment.getExternalStorageDirectory();
            Log.i("ss",sampleDir+"aaaa");
           // if (!sampleDir.canWrite()) // Workaround for broken sdcard support on the device.
                sampleDir = new File(context.getString(R.string._sdcard_recording_));
            
            try {
                mSampleFile = File.createTempFile(aString, extension, sampleDir);
                Log.i("ss",sampleDir+extension+aString);
            } catch (IOException e) {
                setError(SDCARD_ACCESS_ERROR);
                Log.i("ss",mSampleFile.getName()+"错误");
                return;
            }
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mRecorder.setOutputFile(mSampleFile.getAbsolutePath());
        Log.i("ss",mSampleFile.getName()+"44444444444444444");
        
        
        // Handle IOException
        try {
            mRecorder.prepare();
        } catch(IOException exception) {
            setError(INTERNAL_ERROR);
            mRecorder.reset();
            mRecorder.release();
            mRecorder = null;
            return;
        }
        // Handle RuntimeException if the recording couldn't start
        try {
            mRecorder.start();
        } catch (RuntimeException exception) {
            AudioManager audioMngr = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
            boolean isInCall = ((audioMngr.getMode() == AudioManager.MODE_IN_CALL) ||
                    (audioMngr.getMode() == AudioManager.MODE_INVALID));
            if (isInCall) {
                setError(IN_CALL_RECORD_ERROR);
            } else {
                setError(INTERNAL_ERROR);
            }
            mRecorder.reset();
            mRecorder.release();
            mRecorder = null;
            return;
        }
        mSampleStart = System.currentTimeMillis();
        setState(RECORDING_STATE);
    }
    
    public String getSampleFile(){
    	return mSampleFile.getAbsolutePath();
    }
    
    public void stopRecording() {
        if (mRecorder == null)
        {	
        	Log.i("ss",mSampleLength+"mRecorder == NULL555555555555555555555555555");
            return;
        }
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;

        mSampleLength = (int)( (System.currentTimeMillis() - mSampleStart)/1000 );
        //录制时间  
        Log.i("ss",mSampleLength+"录制时间555555555555555555555555555");
        setState(IDLE_STATE);
    }
    
    
    public void startPlayback(String str) {
        stop();
        Log.i("path",str);
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(str);
            mPlayer.setOnCompletionListener(this);
            mPlayer.setOnErrorListener(this);
            mPlayer.prepare();
            mPlayer.start();
            Log.i("secced",str);
        } catch (IllegalArgumentException e) {
            setError(INTERNAL_ERROR);
            mPlayer = null;
            Log.i("error",str);
            return;
        } catch (IOException e) {
            setError(SDCARD_ACCESS_ERROR);
            Log.i("error",str);
            mPlayer = null;
            return;
        }
        
        mSampleStart = System.currentTimeMillis();
        setState(PLAYING_STATE);
    }
    
    public void stopPlayback() {
        if (mPlayer == null) // we were not in playback
            return;

        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
        setState(IDLE_STATE);
    }
    
    public void stop() {
        stopPlayback();
        stopRecording();
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        stop();
        setError(SDCARD_ACCESS_ERROR);
        return true;
    }

    public void onCompletion(MediaPlayer mp) {
        stop();
    }
    
    private void setState(int state) {
        if (state == mState)
            return;
        
        mState = state;
        signalStateChanged(mState);
    }
    
    private void signalStateChanged(int state) {
        if (mOnStateChangedListener != null)
            mOnStateChangedListener.onStateChanged(state);
    }
    
    private void setError(int error) {
        if (mOnStateChangedListener != null)
            mOnStateChangedListener.onError(error);
    }
}
