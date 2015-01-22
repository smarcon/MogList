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
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class HomeActivity extends ListActivity {
	private Button newList;
	private List<ListMog> myLists;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_home);

		myLists = new ArrayList<ListMog>();
		ArrayAdapter<ListMog> adapter = new ArrayAdapter<ListMog>(this,
				R.layout.list_item_layout, myLists);
		setListAdapter(adapter);

		refreshMogLists();

		newList = (Button) findViewById(R.id.btnNewList);

		TextView txtName = (TextView) findViewById(R.id.txtName);
		TextView txtEmail = (TextView) findViewById(R.id.txtEmail);
		// Button btnClose = (Button) findViewById(R.id.btnClose);

		Intent i = getIntent();
		// Receiving the Data
		String username = i.getStringExtra("name");
		String password = i.getStringExtra("pwd");

		// TEST : Displaying Received data
		txtName.setText(username);
		txtEmail.setText(password);

		// Launch the activity to add a new list
		newList.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg) {
				Intent newListScreen = new Intent(getApplicationContext(),
						NewListActivity.class);
				startActivity(newListScreen);
			}
		});
	}

	private void refreshMogLists() {

		ParseQuery<ParseObject> query = ParseQuery.getQuery("list");
		query.addAscendingOrder("nameList");
		setProgressBarIndeterminateVisibility(true);
		query.findInBackground(new FindCallback<ParseObject>() {
			@SuppressWarnings("unchecked")
			@Override
			public void done(List<ParseObject> maList, ParseException e) {
				setProgressBarIndeterminateVisibility(false);
				if (e == null) {
					// If there are results, update the list of posts
					// and notify the adapter
					myLists.clear();
					for (ParseObject po : maList) {
						ListMog mog = new ListMog(po.getObjectId(), po
								.getString("nameList"));
						myLists.add(mog);
					}
					 ((ArrayAdapter<ListMog>) getListAdapter()).notifyDataSetChanged();
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
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
