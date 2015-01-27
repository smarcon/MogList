package com.isep.moglistapp;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ShareActivity extends Activity {

	private Intent i;
	private String id;
	private String title;
	private EditText mail;
	private String mailString;
	private Button share;

	private ParseUser user;
	private ParseRelation<ParseObject> relation;
	private ParseObject mog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (ParseUser.getCurrentUser() == null) {
			startActivity(new Intent(this, Connexion.class));
		} else {
			i = getIntent();
			id = i.getStringExtra("mogId");
			title = i.getStringExtra("title");
			super.onCreate(savedInstanceState);
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			setContentView(R.layout.activity_share);
			setTitle(title);
			getActionBar().setDisplayHomeAsUpEnabled(true);

			mail = (EditText) findViewById(R.id.mailF);
			share = (Button) findViewById(R.id.share);
			share.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mailString = mail.getText().toString();
					if (mailString.length() > 0) {
						try {
							mog = ParseQuery.getQuery("MogList").get(id);
							relation = mog.getRelation("viewers");
							@SuppressWarnings("deprecation")
							ParseQuery<ParseUser> queryUser = ParseUser
									.getQuery();
							if (mailString.contains("@")) {
								queryUser.whereEqualTo("email", mailString);
							} else {
								queryUser.whereEqualTo("username", mailString);
							}
							user = new ParseUser();
							queryUser
									.findInBackground(new FindCallback<ParseUser>() {
										public void done(
												List<ParseUser> objects,
												ParseException e) {
											if (e == null) {
												// The query was successful.
												try {
													user = objects.get(0);
													relation.add(user);
													saveInBackGround(mog, true);
												} catch (Exception e2) {
													saveInBackGround(mog, false);
												}
											} else {
												Log.d("DEBUG ERROR PARSE MAIL",
														e.toString());
											}
										}
									});
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
					} else {
						Toast.makeText(getApplicationContext(),"Veuillez renseigner un email ou nom d'utilisateur svp.",
								Toast.LENGTH_LONG).show();
					}
				}
			});
		}
	}

	protected void saveInBackGround(final ParseObject mog2,
			final boolean mailExist) {
		mog2.saveInBackground(new SaveCallback() {
			@SuppressWarnings({ "deprecation", "static-access" })
			public void done(ParseException e) {
				if (e == null) {
					if (mailExist) {
						Toast.makeText(getApplicationContext(),
								"Votre ami a été notifié.", Toast.LENGTH_LONG).show();
						//retrieve installation of friend
						ParseQuery<ParseInstallation> query = ParseInstallation
								.getQuery();
						query.whereEqualTo("user", user);
						//create new push to send to friend
						ParsePush push = new ParsePush();
						push.setMessage(ParseUser.getCurrentUser()
								.getUsername()
								+ " partage la liste "
								+ mog2.getString("nameList") + " avec vous !");
						push.setQuery(query);
						
						//subscribe current & friend to idlist channel
						ParsePush.subscribeInBackground(mog2.getObjectId());
						push.subscribeInBackground(mog2.getObjectId());
						
						push.sendInBackground();
						Intent addTasksScreen = new Intent(
								getApplicationContext(),
								ShowTasksActivity.class);
						addTasksScreen.putExtra("mogListId", mog2.getObjectId());
						addTasksScreen.putExtra("mogListName", title);
						startActivity(addTasksScreen);
					} else {
						Toast.makeText(
								getApplicationContext(),
								"Désolé, cet utilisateur nous est inconnu. Vous pouvez réessayer.",
								Toast.LENGTH_LONG).show();
					}
				} else {
					// The save failed.
					Toast.makeText(getApplicationContext(), "Error : " + e,
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_setting_logout, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.action_logout:
			ParseUser.logOut();
			startActivity(new Intent(this, Connexion.class));
			return true;
		case R.id.action_settings:
			startActivity(new Intent(this,MyAccount.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
