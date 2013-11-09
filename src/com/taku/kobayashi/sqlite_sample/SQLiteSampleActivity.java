package com.taku.kobayashi.sqlite_sample;

import java.util.HashMap;

import com.taku.kobayashi.sqlite_sample.R;

import android.os.Bundle;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class SQLiteSampleActivity extends Activity {

	private TweetListAdapter _tweetListAdapter;
	private EditText _tweetText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sqlite_sample);
		_tweetListAdapter = new TweetListAdapter(this);
		_tweetText = (EditText) findViewById(R.id.tweetEditText);
		ListView tweetList = (ListView) findViewById(R.id.tweetList);
		tweetList.setAdapter(_tweetListAdapter);
		Button saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_tweetListAdapter.insertTweetText(_tweetText.getText().toString());
			}
		});
	}
}
