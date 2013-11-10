package com.taku.kobayashi.sqlite_sample;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TweetListAdapter extends BaseAdapter {
	//ここではGridViewやListViewで表示させるデータの操作を行う

	private static final String TAG = "Noz_Adapter";
	private ArrayList<String> _tweetTextList;
	private Activity _activity;
	private ActiveRecordPartial _model;

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public TweetListAdapter(Activity act) {
		_activity = act;
		_tweetTextList = new ArrayList<String>();
		_model = new ActiveRecordPartial(act, MigrationConfig.TWEET_TABLE);
		loadTweetText();
	}

	private void loadTweetText(){
		_tweetTextList.clear();
		ArrayList<Bundle> dataList = _model.all();
		for(int i = 0;i < dataList.size();++i){
			_tweetTextList.add(dataList.get(i).getString("message"));
		}
		this.notifyDataSetChanged();
	}

	public void insertTweetText(String tweet){
		Bundle bundle = new Bundle();
		String currentTime = Tools.convertCurrentTime();
		bundle.putString("message", tweet);
		bundle.putString("updated_at", currentTime);
		bundle.putString("created_at", currentTime);
		_model.create(bundle);
		loadTweetText();
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public int getCount() {
		return _tweetTextList.size();
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public Object getItem(int position) {
		return position;
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public long getItemId(int position) {
		return position;
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = _activity.getLayoutInflater().inflate(R.layout.tweetlist_cell, null);
		}
		TextView TweetText = (TextView) convertView.findViewById(R.id.TweetTextView);
		TweetText.setText(_tweetTextList.get(position));

		return convertView;
	}

	public void close(){

	}

}