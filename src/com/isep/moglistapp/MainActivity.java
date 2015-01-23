package com.isep.moglistapp;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class MainActivity extends Activity {

	String APPLICATION_ID = "78rBpEkRL6SHwYFELI6P9r1uIrOlvR0n81BQLgvh";
	String CLIENT_KEY = "vFK3t99ihwKty5J78N72pg6XCFNtNAfDDXMF8t7C";
	
	String appIdTest = "9M7bSmIVfHSwHbOo0xcJHEpwEVdKNpgGbwB6db5w";
	String cléTest = "6KrE4GjrWYV9eKwB1HCQnswTKRy2TyfWF1uSheJJ";

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//init MogList
		Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
		//init Test
		//Parse.initialize(this, appIdTest, cléTest);
		
		ParseObject testObject = new ParseObject("openedAt");
		Date d = new Date();
		d.setHours(d.getHours() + 1);
		testObject.put("date", d);
		testObject.saveInBackground();
		if (ParseUser.getCurrentUser() == null) {
			startActivity(new Intent(this, Connexion.class));
		} else {
			Intent HomeScreen = new Intent(this, HomeActivity.class);
			HomeScreen.putExtra("name", ParseUser.getCurrentUser()
					.getUsername());
			startActivity(HomeScreen);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_logout:
			ParseUser.logOut();
			startActivity(new Intent(this, Connexion.class));
			return true;
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
