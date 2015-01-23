package com.isep.moglistapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class NewListActivity extends Activity {
	private ImageView backImg;
	private LinearLayout addPeople;
	private ListView peopleAddedList;
    private static final int TEXT_ID = 0;
    private Button addList;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newlist);
		backImg = (ImageView) findViewById(R.id.back);
		addPeople = (LinearLayout) findViewById(R.id.add_people);
		peopleAddedList = (ListView) findViewById(R.id.list_people);
		addList = (Button) findViewById(R.id.add_list);
		
		backImg.setOnClickListener(new View.OnClickListener(){
			public void onClick(View img){
				finish();
			}
		});
		
		String[] peopleAdded = new String[] { "personne1","personne2"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, peopleAdded);
		peopleAddedList.setAdapter(adapter);
		
		addPeople.setOnClickListener(new View.OnClickListener(){
			public void onClick(View layout){
				createDialog();
			}
		});
	    
		addList.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent addTasksScreen = new Intent(getApplicationContext(), NewTasksActivity.class);	
				startActivity(addTasksScreen);
			}
		});
		}		
		
		private Dialog createDialog(){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Partager la liste");
			builder.setMessage("Nom ou email de la personne?");
			
			// Use an EditText view to get user input.
	         final EditText input = new EditText(this);
	         input.setId(TEXT_ID);
	         builder.setView(input);
	         
	         builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	        	 
	             @Override
	             public void onClick(DialogInterface dialog, int whichButton) {
	                 String value = input.getText().toString();
	                 Log.d("DialogActivity", "User name: " + value);
	                 return;
	             }
	         });
	  
	         builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	  
	             @Override
	             public void onClick(DialogInterface dialog, int which) {
	                 return;
	             }
	         });
	  
	         return builder.show();
	};
}
