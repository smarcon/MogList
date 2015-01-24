package com.isep.moglistapp;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
	private EditText listName = null;
	private EditText friendMail = null;
	private ParseUser user;
	private ParseRelation<ParseObject> relation;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newlist);
		addList = (Button) findViewById(R.id.add_list);
	//	listName = (EditText) findViewById(R.id.listName);
		//friendMail = (EditText) findViewById(R.id.friend);

		addList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listName.length() > 3) {
					// insert list in parse
					ParseObject mog = new ParseObject("MogList");
					mog.put("nameList", listName);
					relation = mog.getRelation("viewers");
					relation.add(ParseUser.getCurrentUser());

					if (friendMail.length() > 2) {
						@SuppressWarnings("deprecation")
						ParseQuery<ParseUser> queryUser = ParseUser.getQuery();
						queryUser.whereEqualTo("email", friendMail);
						queryUser.setLimit(1);
						user = new ParseUser();
						queryUser
								.findInBackground(new FindCallback<ParseUser>() {
									public void done(List<ParseUser> objects,
											ParseException e) {
										if (e == null) {
											// The query was successful.
											user = objects.get(0);
											relation.add(user);
										}
									}
								});
					}

					mog.saveInBackground(new SaveCallback() {
						public void done(ParseException e) {
							if (e == null) {
								// Saved successfully.
								Toast.makeText(getApplicationContext(),
										"Saved", Toast.LENGTH_SHORT).show();

								// let's add tasks now
								Intent addTasksScreen = new Intent(
										getApplicationContext(),
										NewTasksActivity.class);
								startActivity(addTasksScreen);
							} else {
								// The save failed.
								Toast.makeText(getApplicationContext(),
										"Failed to Save", Toast.LENGTH_SHORT)
										.show();
							}
						}
					});

				} else {
					Toast.makeText(
							getApplicationContext(),
							"Erreur: le nom de la liste doit contenir plus de 3 caractères",
							Toast.LENGTH_LONG).show();
				}

			}
		});
	}

}
