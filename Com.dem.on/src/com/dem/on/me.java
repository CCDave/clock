package com.dem.on;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import android.app.Activity;
import android.content.Intent;




public class me extends Activity{
	Button ItemButtonMyHomePage;
	Activity This;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me); 
        ItemButtonMyHomePage = (Button)findViewById(R.id.ItemButtonMyHomePage);
        This = this;
        ItemButtonMyHomePage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
			    bundle.putString("id", "4");
			    Intent intent = new Intent(This, setup.class);
			    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        intent.putExtras(bundle);
		        startActivity(intent);
			}
		});
        ItemButtonMyHomePage.setVisibility(View.GONE);
    }
}
