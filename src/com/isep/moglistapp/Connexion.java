package com.isep.moglistapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class Connexion extends Activity {
	private EditText username = null;
	private EditText password = null;
	private TextView register;
	private TextView lost_pwd;
	private Button login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connexion);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		login = (Button) findViewById(R.id.login);
		register = (TextView) findViewById(R.id.register);
		lost_pwd = (TextView) findViewById(R.id.lost_pwd);

		// listening to CONNEXION button event
		login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg) {

				ParseUser.logInInBackground(username.getText().toString(),
						password.getText().toString(), new LogInCallback() {
							public void done(ParseUser user, ParseException e) {
								if (user != null) {
									// Hooray! The user is logged in.
									// starting a new intent (to open new
									// activity)
									Intent HomeScreen = new Intent(
											getApplicationContext(),
											HomeActivity.class);

									// Sending data to another Activity
									// HomeScreen.putExtra("name",
									// username.getText().toString());
									 //HomeScreen.putExtra("pwd", password.getText().toString());
									Log.e("n", username.getText() + "."
											+ password.getText());

									startActivity(HomeScreen);
								} else {
									Toast.makeText(getBaseContext(),
											"Erreur de connexion",
											Toast.LENGTH_LONG).show();
								}
							}
						});
			}
		});

		// listening to INSCRIPTION button event
		register.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg) {
				Intent RegisterScreen = new Intent(getApplicationContext(),
						RegisterActivity.class);
				startActivity(RegisterScreen);
			}
		});

		// listening to LOST PWD button event
		lost_pwd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg) {
				if (!username.getText().toString().contains("@")) {
					Toast.makeText(getBaseContext(),
							"Veuillez renseigner votre mail svp",
							Toast.LENGTH_LONG).show();
				} else {
					ParseUser.requestPasswordResetInBackground(username
							.getText().toString(),
							new RequestPasswordResetCallback() {
								public void done(ParseException e) {
									if (e == null) {
										Toast.makeText(
												getBaseContext(),
												"Un mail a été envoyé pour réinitialiser votre mot de passe",
												Toast.LENGTH_LONG).show();
									} else {
										Toast.makeText(
												getBaseContext(),
												"Error : "
														+ e.toString()
																.substring(25),
												Toast.LENGTH_LONG).show();
									}
								}
							});
				}
			}
		});
	}
}
