package com.isep.moglistapp;

import com.parse.Parse;

import android.app.Application;

public class ParseApplication extends Application {

	String APPLICATION_ID = "78rBpEkRL6SHwYFELI6P9r1uIrOlvR0n81BQLgvh";
	String CLIENT_KEY = "vFK3t99ihwKty5J78N72pg6XCFNtNAfDDXMF8t7C";

	String appIdTest = "9M7bSmIVfHSwHbOo0xcJHEpwEVdKNpgGbwB6db5w";
	String cléTest = "6KrE4GjrWYV9eKwB1HCQnswTKRy2TyfWF1uSheJJ";

	@Override
	public void onCreate() {
		super.onCreate();

		// init MogList
		Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
		// init Test
		// Parse.initialize(this, appIdTest, cléTest);

	}
}
