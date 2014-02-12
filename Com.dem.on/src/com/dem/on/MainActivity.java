package com.dem.on;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import com.dem.on.Clock;
import com.dem.on.R;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

	TabHost tabHost;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		tabHost = getTabHost();
		setTabs();                       
    }

    private void setTabs()
	{
    	addTab("clock", R.drawable.tab_clock, Clock.class);
    	addTab("stars", R.drawable.tab_stars, stars.class);
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

}
