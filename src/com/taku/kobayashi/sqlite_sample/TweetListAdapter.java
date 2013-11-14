package com.taku.kobayashi.sqlite_sample;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TweetListAdapter extends BaseAdapter {
	//ここではGridViewやListViewで表示させるデータの操作を行う

	private static final String TAG = "SQLite_Sample_TweetListAdapter";
	private ArrayList<ActiveRecordPartialInstance> _tweetDataList;
	private Activity _activity;
	private ActiveRecordPartial _model;
	private int _selectPosition = 0;

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public TweetListAdapter(Activity act) {
		_activity = act;
		_tweetDataList = new ArrayList<ActiveRecordPartialInstance>();
		_model = new ActiveRecordPartial(act, MigrationConfig.TWEET_TABLE);
		loadTweetText();
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	private void loadTweetText(){
		_tweetDataList.clear();
		_tweetDataList = _model.all();
		this.notifyDataSetChanged();
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

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
		return _tweetDataList.size();
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
		ActiveRecordPartialInstance data = _tweetDataList.get(position);
		TweetText.setText(data.get("message"));
		Button updateButton = (Button) convertView.findViewById(R.id.UpdateButton);
		updateButton.setTag(position);
		updateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_selectPosition = (Integer) v.getTag();
				updateDialog();
			}
		});
		Button deleteButton = (Button) convertView.findViewById(R.id.DeleteButton);
		deleteButton.setTag(position);
		deleteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_selectPosition = (Integer) v.getTag();
				deleteDialog();
			}
		});
		return convertView;
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	private void deleteDialog(){
		AlertDialog.Builder deleteDialog = new AlertDialog.Builder(_activity);
		deleteDialog.setMessage(_activity.getString(R.string.deleteDialogMessage));
		deleteDialog.setCancelable(true);
		deleteDialog.setPositiveButton(_activity.getString(R.string.selectYesText), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				ActiveRecordPartialInstance data = _tweetDataList.get(_selectPosition);
				data.destroy();
				loadTweetText();
			}
		});
		deleteDialog.setNegativeButton(_activity.getString(R.string.selectNoText), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		deleteDialog.create().show();
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	private void updateDialog(){
		final EditText editView = new EditText(_activity);
		editView.setText( _tweetDataList.get(_selectPosition).get("message"));
		AlertDialog.Builder updateDialog = new AlertDialog.Builder(_activity);
		updateDialog.setMessage(_activity.getString(R.string.updateDialogTitle));
		updateDialog.setCancelable(true);
		updateDialog.setView(editView);
		updateDialog.setPositiveButton(_activity.getString(R.string.updateDecideButtonText), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				ActiveRecordPartialInstance data = _tweetDataList.get(_selectPosition);
				Bundle b = new Bundle();
				b.putString("message", editView.getText().toString());
				data.update_attributes(b);
				loadTweetText();
			}
		});
		updateDialog.setNegativeButton(_activity.getString(R.string.cancelButtonText), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		updateDialog.create().show();
	}

	public void close(){

	}

}