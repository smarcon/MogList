package com.isep.moglistapp;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.PushService;

public class MainActivity extends Activity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// To track statistics around application
		ParseAnalytics.trackAppOpened(getIntent());
		// inform the Parse Cloud that it is ready for notifications
		PushService.setDefaultPushCallback(this, MainActivity.class);

		// save user & date in Parse.com
		ParseObject testObject = new ParseObject("openedAt");
		Date d = new Date();
		d.setHours(d.getHours() + 1);
		testObject.put("date", d);
		if (ParseUser.getCurrentUser() == null) {
			testObject.saveInBackground();
			// redirect to connexion page
			startActivity(new Intent(this, Connexion.class));
		} else {
			testObject.put("user", ParseUser.getCurrentUser());
			testObject.saveInBackground();
			ParseInstallation installation = ParseInstallation.getCurrentInstallation();
			installation.put("user", ParseUser.getCurrentUser());
			installation.saveInBackground();
			Intent HomeScreen = new Intent(this, HomeActivity.class);
			HomeScreen.putExtra("name", ParseUser.getCurrentUser()
					.getUsername());
			// redirect to home page (my lists)
			startActivity(HomeScreen);
		}
	}

}
