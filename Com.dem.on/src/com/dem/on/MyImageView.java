package com.dem.on;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.widget.ImageView;


public class MyImageView extends ImageView {   
    
    private Bitmap mBitmap;   
    private Matrix mMatrix = new Matrix();   
    private String strFilePath;
    private static int mScreenWidth;   
    private static int mScreenHeight;   
       
    public MyImageView(Context context ,String str) {   
        super(context);   
        strFilePath = str;
        initialize();   
    }   
    public void setPath(String str){
    	strFilePath = str;
    }
    private void initialize() {   
        DisplayMetrics dm = getResources().getDisplayMetrics();   
        mScreenWidth = dm.widthPixels;   
        mScreenHeight = dm.heightPixels;
        File f = new File(strFilePath);
        if (f.exists()){
        	 Bitmap bmp = BitmapFactory.decodeFile(strFilePath);   
             mBitmap = Bitmap.createScaledBitmap(bmp, mScreenWidth, mScreenHeight, true);
        }
    }   
       
    @Override 
    protected void onDraw(Canvas canvas) {   
//      super.onDraw(canvas);  //当然，如果界面上还有其他元素需要绘制，只需要将这句话写上就行了。   
        canvas.drawBitmap(mBitmap, 0, 0, null);   
    }   
}  