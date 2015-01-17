package com.isep.moglistapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class HomeActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
 
        TextView txtName = (TextView) findViewById(R.id.txtName);
        TextView txtEmail = (TextView) findViewById(R.id.txtEmail);
        //Button btnClose = (Button) findViewById(R.id.btnClose);
 
        Intent i = getIntent();
        // Receiving the Data
        String username = i.getStringExtra("name");
        String password = i.getStringExtra("pwd");
 
        // TEST : Displaying Received data
        txtName.setText(username);
        txtEmail.setText(password);
 
        // Get ListView object from xml
        ListView listView = (ListView) findViewById(R.id.list);
        // define list content (for the moment)
        String[] values = new String[] { "Android", 
                "JEE",
                "BI"
               };
        
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);
        listView.setAdapter(listViewAdapter);
        /*// Binding Click event to Button to close the activity
        btnClose.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View arg0) {
                //Closing SecondScreen Activity
                finish();
            }
        });*/
 
    }
}
