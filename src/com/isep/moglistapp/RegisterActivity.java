package com.isep.moglistapp;

import com.parse.ParseUser;
import com.parse.ParseException;
import com.parse.SignUpCallback;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

public class RegisterActivity extends Activity {
	private TextView connection;
	private EditText username = null;
	private EditText mail = null;
	private EditText pwd = null;
	private EditText pwdConf = null;
	private Button register;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		connection = (TextView) findViewById(R.id.connection);
		username = (EditText) findViewById(R.id.username);
		mail = (EditText) findViewById(R.id.email);
		pwd = (EditText) findViewById(R.id.password);
		pwdConf = (EditText) findViewById(R.id.check_password);
		register = (Button) findViewById(R.id.register);

		// Launch the activity to login
		connection.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg) {
				Intent connectionScreen = new Intent(getApplicationContext(),
						Connexion.class);
				startActivity(connectionScreen);
			}
		});

		// Launch the activity HOME if valid data
		register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (username.getText().toString() != null && mail.getText().toString() != null && pwd.getText().toString() != null) {
					if ((pwd.getText().toString()).equals((pwdConf.getText()
							.toString()))) {
						ParseUser user = new ParseUser();
						user.setUsername(username.getText().toString());
						user.setPassword(pwd.getText().toString());
						user.setEmail(mail.getText().toString());

						user.signUpInBackground(new SignUpCallback() {
							public void done(ParseException e) {
								if (e == null) {
									startActivity(new Intent(
											getApplicationContext(),
											HomeActivity.class));
								} else {
									Toast.makeText(getBaseContext(),"Error : " + e.toString().substring(25),
											Toast.LENGTH_LONG).show();
								}
							}
						});
					} else {
						Toast.makeText(getBaseContext(), "Erreur : Mot de passe non confirmé",
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(getBaseContext(), "Erreur : Veuillez remplir tous les champs svp.",
							Toast.LENGTH_LONG).show();
				}

			}
		});
	}
}
