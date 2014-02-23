package com.dem.on;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import com.dem.on.R;


@SuppressWarnings("deprecation")
public class stars extends TabActivity{
	
	TabHost tabHost;
	ImageView ButtonControl_search;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stars_activity);      
        tabHost = getTabHost();
		setTabs();   
		setView();
    }
    public void setView(){
    	ButtonControl_search  = (ImageView) findViewById(R.id.ButtonControl_search);
    	
    }
	
	public void doSearch(View s)
    {
    	Toast.makeText(this, "I lied, I love KUNG FUuuuuuuuuUUuuu...!!", Toast.LENGTH_LONG).show();
    }
	
	private void setTabs()
	{
	    	addTab("热门", R.drawable.tab_clock, stars_list.class);
	    	addTab("分类", R.drawable.tab_stars, star_list_class.class);
	}
	private void addTab(String labelId, int drawableId, Class<?> c)
	{
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);	
		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);
		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
	}
	@Override
	protected void onResume() {
		Log.i("刷新", "刷新");
		  super.onResume();
	 }
}
