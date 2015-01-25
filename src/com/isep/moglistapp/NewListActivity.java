package com.isep.moglistapp;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class NewListActivity extends Activity {
	private Button addList;
	private EditText listName;
	private EditText friendMail;
	private String name;
	private String mail;
	private ParseUser user;
	private ParseRelation<ParseObject> relation;
	private ParseObject mog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_newlist);
		addList = (Button) findViewById(R.id.saveList);
		listName = (EditText) findViewById(R.id.nameList);
		friendMail = (EditText) findViewById(R.id.mailFriend);

		addList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveList();
			}
		});
	}

	private void saveList() {
		name = listName.getText().toString();
		mail = friendMail.getText().toString();
		mog = new ParseObject("MogList");
		if (name.length() > 2) {
			// insert list in parse
			mog.put("nameList", name);
			relation = mog.getRelation("viewers");
			relation.add(ParseUser.getCurrentUser());
			if (mail.length() > 0) {
				@SuppressWarnings("deprecation")
				ParseQuery<ParseUser> queryUser = ParseUser.getQuery();
				if (mail.contains("@")) {
					queryUser.whereEqualTo("email", mail);
				} else {
					queryUser.whereEqualTo("username", mail);
				}
				user = new ParseUser();
				queryUser.findInBackground(new FindCallback<ParseUser>() {
					public void done(List<ParseUser> objects, ParseException e) {
						if (e == null) {
							// The query was successful.
							try {
								user = objects.get(0);
								relation.add(user);
								saveInBackGround(mog, 1);
							} catch (Exception e2) {
								saveInBackGround(mog, 2);
							}
						} else {
							
						}
					}
				});
			} else {
				saveInBackGround(mog, 0);
			}
		} else {
			Toast.makeText(getApplicationContext(),"Erreur: le nom de la liste doit contenir plus de 3 caractères", Toast.LENGTH_LONG).show();
		}

	}

	private void saveInBackGround(final ParseObject mog2, final int mail2) {
		// 0=mailMissing 1=mailOK 2=mailWrong
		mog2.saveInBackground(new SaveCallback() {
			public void done(ParseException e) {
				if (e == null) {
					String msg = null;
					switch (mail2) {
					case 0:
						msg = "";
						break;
					case 1:
						msg="Votre ami a été notifié.";
						break;
					case 2:
						msg="Désolé, cet utilisateur nous est inconnu. Vous pouvez réessayer.";
						break;
					}
					Toast.makeText(
							getApplicationContext(),
							"Votre liste "
									+ name
									+ " a été sauvegardée, vous pouvez y ajouter des tâches.\n"+msg,
							Toast.LENGTH_LONG).show();
					// let's add tasks now
					Intent addTasksScreen = new Intent(getApplicationContext(),
							NewOrEditTask.class);
					addTasksScreen.putExtra("mogId", mog2.getObjectId());
					addTasksScreen.putExtra("title", name);
					startActivity(addTasksScreen);
				} else {
					// The save failed.
					Toast.makeText(getApplicationContext(), "Failed to Save",
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
			startActivity(new Intent(this, HomeActivity.class));
			return true;
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
