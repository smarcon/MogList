package com.isep.moglistapp;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class EditList extends ListActivity {
	private Button addList;
	private EditText listName;
	private Intent intentReceived;
	private Intent intentOk;
	private String name;
	private String mogId;
	private ParseRelation<ParseObject> relation;
	private List<String> viewers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_edit_list);
		intentOk = new Intent(this, HomeActivity.class);
		intentReceived = getIntent();
		name = intentReceived.getStringExtra("mogListName");
		addList = (Button) findViewById(R.id.saveList);
		listName = (EditText) findViewById(R.id.nameList);
		listName.setText(name);
		mogId = intentReceived.getStringExtra("mogListId");

		viewers = new ArrayList<String>();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.list_viewers_layout, viewers);
		setListAdapter(adapter);

		refreshViewers();

		addList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				name = listName.getText().toString();
				if (name.length() < 3) {
					Toast.makeText(
							getApplicationContext(),
							"Erreur : le nom doit contenir au moins 3 caractères",
							Toast.LENGTH_LONG).show();
				} else {
					// Create a pointer
					ParseObject point = ParseObject.createWithoutData(
							"MogList", mogId);
					// Set a new value on quantity
					point.put("nameList", name);

					// Save
					point.saveInBackground(new SaveCallback() {
						public void done(ParseException e) {
							if (e == null) {
								startActivity(intentOk);
							} else {
								// The save failed.
								Toast.makeText(getApplicationContext(),
										"Error: " + e, Toast.LENGTH_LONG)
										.show();

							}
						}
					});

				}
			}
		});
	}

	private void refreshViewers() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("MogList");
		query.getInBackground(mogId, new GetCallback<ParseObject>() {
			public void done(ParseObject mog, ParseException e) {
				if (e == null) {
					relation = mog.getRelation("viewers");
					relation.getQuery().findInBackground(
							new FindCallback<ParseObject>() {
								@SuppressWarnings("unchecked")
								@Override
								public void done(List<ParseObject> objects,
										ParseException e) {
									for (ParseObject po : objects) {
										if (!po.getObjectId().equals(
												ParseUser.getCurrentUser()
														.getObjectId())) {
											viewers.add(po
													.getString("username"));
										}
									}

									((ArrayAdapter<String>) getListAdapter())
											.notifyDataSetChanged();
								}
							});

				} else {
					Log.d(getClass().getSimpleName(),
							"PARSEError: " + e.getMessage());
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
			startActivity(new Intent(this,MyAccount.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
