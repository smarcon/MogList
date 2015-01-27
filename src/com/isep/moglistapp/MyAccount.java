package com.isep.moglistapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MyAccount extends Activity {
	private TextView lost_pwd;
	private EditText usrnm;
	private EditText mail;
	private ParseUser you;
	private Button save;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_my_account);
		
		if(ParseUser.getCurrentUser()==null){
			startActivity(new Intent(this, Connexion.class));
		}else{
			you = ParseUser.getCurrentUser();
			usrnm = (EditText) findViewById(R.id.username);
			mail = (EditText) findViewById(R.id.email);
			usrnm.setText(you.getUsername());
			mail.setText(you.getEmail());
			
			lost_pwd = (TextView) findViewById(R.id.lost_pwd);
			lost_pwd.setOnClickListener(new View.OnClickListener() {
				public void onClick(View arg) {
					try {
						ParseUser.requestPasswordReset(you.getEmail());
						Toast.makeText(
								getApplicationContext(),
								"Un mail vous a été envoyé pour réinitialiser votre mot de passe",
								Toast.LENGTH_LONG).show();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}});
			save = (Button) findViewById(R.id.saveSettings);
			save.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (usrnm.getText().toString() != null
							&& mail.getText().toString() != null){
						you.setUsername(usrnm.getText().toString()); // attempt to change username
						you.setEmail(mail.getText().toString());
						you.saveInBackground(new SaveCallback() {
							@Override
							public void done(ParseException e) {
								if (e==null){
									Toast.makeText(
											getApplicationContext(),
											"Vos données ont été mises à jour.",
											Toast.LENGTH_LONG).show();
									startActivity(new Intent(getApplicationContext(),MyAccount.class));
								}else{
									Toast.makeText(
											getApplicationContext(),
											"Error : "
													+ e.toString(),
											Toast.LENGTH_LONG).show();
								}
							}
						}); 
					}else {
						Toast.makeText(getApplicationContext(),
								"Erreur : Veuillez remplir tous les champs svp.",
								Toast.LENGTH_LONG).show();
					}
				}
			});
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
			finish();
			return true;
		case R.id.action_logout:
			ParseUser.logOut();
			startActivity(new Intent(this, Connexion.class));
			return true;
		case R.id.action_settings:
			startActivity(new Intent(this, MyAccount.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
