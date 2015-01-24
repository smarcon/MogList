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
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ShowTasksActivity extends ListActivity {
	private Button newTask;
	private List<BeanTask> myTasks;
	private Intent i;
	private String id;
	private String title;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		if (ParseUser.getCurrentUser() == null) {
			startActivity(new Intent(this, Connexion.class));
		} else {
			i = getIntent();
			id = i.getStringExtra("mogListId");
			title = i.getStringExtra("mogListName");

			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
			setContentView(R.layout.activity_show_tasks);
			setTitle(title);
			getActionBar().setDisplayHomeAsUpEnabled(true);

			myTasks = new ArrayList<BeanTask>();
			ArrayAdapter<BeanTask> adapter = new ArrayAdapter<BeanTask>(this,
					R.layout.list_tasks_layout, myTasks);
			setListAdapter(adapter);
			refreshTasks();

			newTask = (Button) findViewById(R.id.btnNewTask);

			// Launch the activity to add a new task
			newTask.setOnClickListener(new View.OnClickListener() {
				public void onClick(View arg) {
					Intent newTaskScreen = new Intent(getApplicationContext(),
							NewOrEditTask.class);
					newTaskScreen.putExtra("title", title);
					newTaskScreen.putExtra("mogId", id);
					startActivity(newTaskScreen);
				}
			});
		}
	}

	private void refreshTasks() {

		// Retrieve data
		ParseQuery<ParseObject> query = ParseQuery.getQuery("MogTask");
		query.whereEqualTo("idMogList", id);
		query.addAscendingOrder("nameTask");
		setProgressBarIndeterminateVisibility(true);

		query.findInBackground(new FindCallback<ParseObject>() {
			@SuppressWarnings({ "unchecked", "deprecation" })
			@Override
			public void done(List<ParseObject> maList, ParseException e) {
				setProgressBarIndeterminateVisibility(false);
				if (e == null) {
					// If there are results, update the list
					// and notify the adapter
					myTasks.clear();
					for (ParseObject po : maList) {
						String d = (po.getDate("termDate")) == null ? null
								: (po.getDate("termDate")).toLocaleString();
						BeanTask task = new BeanTask(po.getObjectId(), po
								.getString("nameTask"), po
								.getString("idMogList"), d, po
								.getDate("termDate"));
						myTasks.add(task);
					}
					((ArrayAdapter<BeanTask>) getListAdapter())
							.notifyDataSetChanged();
				} else {
					Log.d(getClass().getSimpleName(),
							"PARSEError: " + e.getMessage());
				}
			}
		});
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id2) {
		BeanTask task = myTasks.get(position);
		Intent intent = new Intent(this, NewOrEditTask.class);
		intent.putExtra("taskId", task.getIdTask());
		intent.putExtra("taskName", task.getNameTask());
		intent.putExtra("termDate", task.getTermDate().length() < 2 ? "" : task
				.getTermDate().substring(1));
		intent.putExtra("title", title);
		intent.putExtra("mogId", id);
		intent.putExtra("dt",(task.getDt()==null)?"":task.getDt().getTime());
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_refresh_settings_logout, menu);
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
		case R.id.action_refresh:
			refreshTasks();
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
