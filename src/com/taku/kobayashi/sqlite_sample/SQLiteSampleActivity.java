package com.taku.kobayashi.sqlite_sample;

import com.taku.kobayashi.sqlite_sample.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class SQLiteSampleActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sqlite_sample);
		DBOpenHelper DBHelper = new DBOpenHelper(this, "sample", 1);
		DBHelper.getReadableDatabase();
	}
}
