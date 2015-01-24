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

public class HomeActivity extends ListActivity {
	private Button newList;
	private List<BeanMog> myLists;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (ParseUser.getCurrentUser() == null) {
			startActivity(new Intent(this, Connexion.class));
		} else {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
			setContentView(R.layout.activity_home);
			myLists = new ArrayList<BeanMog>();
			ArrayAdapter<BeanMog> adapter = new ArrayAdapter<BeanMog>(this,
					R.layout.list_mog_layout, myLists);
			setListAdapter(adapter);
			refreshMogLists();

			newList = (Button) findViewById(R.id.btnNewList);

			// Launch the activity to add a new list
			newList.setOnClickListener(new View.OnClickListener() {
				public void onClick(View arg) {
					Intent newListScreen = new Intent(getApplicationContext(),
							NewListActivity.class);
					startActivity(newListScreen);
				}
			});
		}
	}

	private void refreshMogLists() {

		// Retrieve data
		ParseQuery<ParseObject> query = ParseQuery.getQuery("MogList");
		query.whereEqualTo("viewers", ParseUser.getCurrentUser());
		query.addAscendingOrder("nameList");
		setProgressBarIndeterminateVisibility(true);

		query.findInBackground(new FindCallback<ParseObject>() {
			@SuppressWarnings("unchecked")
			@Override
			public void done(List<ParseObject> maList, ParseException e) {
				setProgressBarIndeterminateVisibility(false);
				if (e == null) {
					// If there are results, update the list
					// and notify the adapter
					myLists.clear();
					for (ParseObject po : maList) {
						BeanMog mog = new BeanMog(po.getObjectId(), po.getString("nameList"));
						myLists.add(mog);
					}
					((ArrayAdapter<BeanMog>) getListAdapter())
							.notifyDataSetChanged();
				} else {
					Log.d(getClass().getSimpleName(),
							"PARSEError: " + e.getMessage());
				}
			}
		});
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	    BeanMog mog = myLists.get(position);
	    Intent intent = new Intent(this, ShowTasksActivity.class);
	    intent.putExtra("mogListId", mog.getListId());
	    intent.putExtra("mogListName", mog.getListName());
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
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.action_refresh:
			refreshMogLists();
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
