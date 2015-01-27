package com.isep.moglistapp;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ShowTasksActivity extends ListActivity {
	private Button newTask;
	private Button delete;
	private Button share;
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
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			setContentView(R.layout.activity_show_tasks);
			setTitle(title);
			getActionBar().setDisplayHomeAsUpEnabled(true);

			myTasks = new ArrayList<BeanTask>();
			ArrayAdapter<BeanTask> adapter = new ArrayAdapter<BeanTask>(this,
					R.layout.list_tasks_layout, myTasks);
			setListAdapter(adapter);
			refreshTasks();

			// Launch the activity to add a new task
			newTask = (Button) findViewById(R.id.btnNewTask);
			newTask.setOnClickListener(new View.OnClickListener() {
				public void onClick(View arg) {
					Intent newTaskScreen = new Intent(getApplicationContext(),
							NewOrEditTask.class);
					newTaskScreen.putExtra("title", title);
					newTaskScreen.putExtra("mogId", id);
					startActivity(newTaskScreen);
				}
			});

			delete = (Button) findViewById(R.id.btnDeleteList);
			delete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					confirmDialog();
				}
			});
			
			share=(Button) findViewById(R.id.shareL);
			share.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent share = new Intent(getApplicationContext(),
							ShareActivity.class);
					share.putExtra("title", title);
					share.putExtra("mogId", id);
					startActivity(share);
				}
			});
		}

	}

	protected void confirmDialog() {
		AlertDialog.Builder dialBuilder = new AlertDialog.Builder(this);
		dialBuilder
				.setMessage("Etes-vous sûr de vouloir supprimer cette liste ?");
		dialBuilder.setNegativeButton("Annuler",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(getApplicationContext(),
								ShowTasksActivity.class);
						intent.putExtra("mogListId", id);
						intent.putExtra("mogListName", title);
						startActivity(intent);
					}
				});
		dialBuilder.setPositiveButton("Oui",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						deleteTask();
					}
				});
		AlertDialog alertDialog = dialBuilder.create();
		alertDialog.show();
	}

	protected void deleteTask() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("MogList");
		query.getInBackground(id, new GetCallback<ParseObject>() {
			public void done(ParseObject mog, ParseException e) {
				if (e == null) {
					ParseRelation<ParseObject> relation = mog
							.getRelation("viewers");
					try {
						if (relation.getQuery().count() == 1) {
							mog.deleteInBackground(new DeleteCallback() {
								
								@Override
								public void done(ParseException e) {
									if (e!=null){Log.d("Parse delete mog",e.toString());}
								}
							});
							ParseQuery<ParseObject> queryTask = ParseQuery
									.getQuery("MogTask");
							queryTask.whereEqualTo("idMogList", id);
							queryTask
									.findInBackground(new FindCallback<ParseObject>() {
										@Override
										public void done(List<ParseObject> po,
												ParseException e2) {
											for (ParseObject p : po) {
												p.deleteInBackground();
											}
											Toast.makeText(
													getApplicationContext(),
													title
															+ " et ses tâches ont été supprimées.",
													Toast.LENGTH_LONG).show();
											startActivity(new Intent(
													getApplicationContext(),
													HomeActivity.class));
										}
									});
						} else {
							relation.remove(ParseUser.getCurrentUser());
							mog.saveInBackground(new SaveCallback() {
								public void done(ParseException e) {
									Toast.makeText(
											getApplicationContext(),
											title
													+ " a été supprimée de vos listes.",
											Toast.LENGTH_LONG).show();
									startActivity(new Intent(
											getApplicationContext(),
											HomeActivity.class));
								}
							});
						}
					} catch (ParseException e1) {
						Log.d("PARSE", e1.toString());
					}
				}
			}
		});

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
		intent.putExtra("dt", (task.getDt() == null) ? "" : task.getDt()
				.getTime());
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
			startActivity(new Intent(this,MyAccount.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
