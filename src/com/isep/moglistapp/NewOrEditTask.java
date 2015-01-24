package com.isep.moglistapp;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class NewOrEditTask extends Activity {
	private Intent intentReceived;
	private String taskId = null;
	private String mogId;
	private String name;
	private String term;
	private Date dt;
	private EditText taskNameEditText;
	private TextView taskTermText;
	private CalendarView cal;
	private Button save;
	private String title;
	private Intent intentOk;
	private ImageButton ok;
	private long milli;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		intentOk = new Intent(this, ShowTasksActivity.class);
		intentReceived = getIntent();
		taskId = intentReceived.getStringExtra("taskId");
		name = intentReceived.getStringExtra("taskName");
		mogId = intentReceived.getStringExtra("mogId");
		title = intentReceived.getStringExtra("title");
		term = intentReceived.getStringExtra("termDate");

		Calendar cal2 = Calendar.getInstance();
		long ms = cal2.getTimeInMillis();
		milli = intentReceived.getLongExtra("dt", ms);

		super.onCreate(savedInstanceState);

		// bouton retour
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_new_or_edit_task);
		setTitle(title);

		taskNameEditText = (EditText) findViewById(R.id.nameTask);
		taskNameEditText.setText(name);

		cal = (CalendarView) findViewById(R.id.calendarView);

		taskTermText = (TextView) findViewById(R.id.termTask);
		taskTermText.setText(term);
		taskTermText.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("static-access")
			public void onClick(View arg) {
				if (cal.getVisibility() == arg.VISIBLE) {
					cal.setVisibility(arg.GONE);
					taskTermText.setText("Echéance (facultatif)");
					dt = null;
				} else {
					dt = milli == 0 ? new Date() : new Date(milli);
					cal.setVisibility(arg.VISIBLE);
					cal.setDate(milli);
					cal.setOnDateChangeListener(new OnDateChangeListener() {
						@Override
						public void onSelectedDayChange(CalendarView view,
								int year, int month, int dayOfMonth) {
							taskTermText.setText(dayOfMonth
									+ " "
									+ (new DateFormatSymbols().getMonths()[month])
											.substring(0, 3) + ". " + year);
							dt.setTime(cal.getDate());
						}
					});
				}
			}
		});

		save = (Button) findViewById(R.id.saveTask);
		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveTask();
			}
		});

		ok = (ImageButton) findViewById(R.id.validate);
		if (taskId != null) {
			// 0=visible 1=invisible 2=gone
			ok.setVisibility(0);
		}
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ParseQuery<ParseObject> query = ParseQuery.getQuery("MogTask");
				query.getInBackground(taskId, new GetCallback<ParseObject>() {
					public void done(ParseObject po, ParseException e) {
						if (e == null) {
							po.deleteInBackground();
							intentOk.putExtra("mogListId", mogId);
							intentOk.putExtra("mogListName", title);
							startActivity(intentOk);
						}
					}
				});
			}
		});
	}

	private void saveTask() {
		name = taskNameEditText.getText().toString();

		if (name.length() < 3) {
			Toast.makeText(getApplicationContext(),
					"Erreur : le nom doit contenir au moins 3 caractères",
					Toast.LENGTH_LONG).show();
		} else {
			if (taskId == null) {
				// create a task in DB
				ParseObject taskPO = new ParseObject("MogTask");
				taskPO.put("nameTask", name);
				taskPO.put("idMogList", mogId);
				if (dt != null) {
					taskPO.put("termDate", dt);
				}
				taskPO.saveInBackground(new SaveCallback() {
					public void done(ParseException e) {
						if (e == null) {
							intentOk.putExtra("mogListId", mogId);
							intentOk.putExtra("mogListName", title);
							startActivity(intentOk);
						} else {
							Toast.makeText(getApplicationContext(),
									"Error: " + e, Toast.LENGTH_LONG).show();
						}
					}
				});
			} else {
				// update = delete and recreate
				ParseQuery<ParseObject> query = ParseQuery.getQuery("MogTask");
				query.getInBackground(taskId, new GetCallback<ParseObject>() {
					public void done(ParseObject po, ParseException e) {
						if (e == null) {
							po.deleteInBackground();
							ParseObject taskPO = new ParseObject("MogTask");
							taskPO.put("idMogList", mogId);
							taskPO.put("nameTask", name);
							if (dt != null) {
								taskPO.put("termDate", dt);
							}
							taskPO.saveInBackground(new SaveCallback() {
								public void done(ParseException e) {
									if (e == null) {
										intentOk.putExtra("mogListId", mogId);
										intentOk.putExtra("mogListName", title);
										startActivity(intentOk);
									} else {
										Toast.makeText(getApplicationContext(),
												"Error: " + e,
												Toast.LENGTH_LONG).show();
									}
								}
							});
						}
					}
				});
			}
		}
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
			Intent intent = new Intent(this, ShowTasksActivity.class);
			intent.putExtra("mogListId", mogId);
			intent.putExtra("mogListName", title);
			startActivity(intent);
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
