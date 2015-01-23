package com.isep.moglistapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class NewTasksActivity extends Activity {
	private ImageView backImg;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_tasks);
		backImg = (ImageView) findViewById(R.id.back);
		
		backImg.setOnClickListener(new View.OnClickListener(){
			public void onClick(View img){
				finish();
			}
		});
	}
}
