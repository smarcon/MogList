package com.isep.moglistapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;

public class RegisterActivity extends Activity {
	private TextView connection;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		connection = (TextView) findViewById(R.id.connection);
		
		//Launch the activity to login
		connection.setOnClickListener(new View.OnClickListener(){
			public void onClick(View arg){
				Intent connectionScreen = new Intent(getApplicationContext(), Connexion.class);
				startActivity(connectionScreen);
			}
		});
	}

}
