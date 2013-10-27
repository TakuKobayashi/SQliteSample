package com.taku.kobayashi.sqlite_sample;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;

public class Tools{

	private static final String TAG = "SQLite_Sample_Tools";

	public static JSONObject loadJSONFromAsset(Context context, String fileName) {
		JSONObject result = new JSONObject();
		Log.d(TAG,fileName);
		String json = "";
		try {
			InputStream is = context.getAssets().open(fileName);
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			json = new String(buffer, "UTF-8");
			Log.d(TAG,json);
			result = new JSONObject(json);
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d(TAG,""+result);
		return result;
	}

	public static ArrayList<String> makeMigrateSQLList(JSONObject migrationJSON){
		ArrayList<String> SQLList = new ArrayList<String>();
		JSONArray tables = migrationJSON.names();
		try {
			for(int i = 0; i < tables.length(); ++i){
				String SQL = "create table ";
				String tableName = tables.getString(i);
				SQL = SQL + tableName +"(";
				SQL = SQL + "id INTEGER PRIMARY KEY AUTOINCREMENT,";
				JSONObject tableColumns = migrationJSON.getJSONObject(tableName);
				JSONArray columnNames = tableColumns.names();
				for(int j = 0; j < columnNames.length(); ++j){
					String columnName = columnNames.getString(j);
					SQL = SQL + columnName + " ";
					JSONObject columnDataTypes = tableColumns.getJSONObject(columnName);
					JSONArray typeNames = columnDataTypes.names();
					for(int k = 0; k < typeNames.length(); ++k){
						String typeName = typeNames.getString(k);
						JSONArray options = columnDataTypes.getJSONArray(typeName);
						SQL = SQL + typeName + " ";
						for(int l = 0; l < options.length(); ++l){
							SQL = SQL + options.getString(l);
							if(l < options.length() - 1){
								SQL = SQL + " ";
							}
						}
					}
					SQL = SQL + ",";
				}
				SQL = SQL + "updated_at TEXT NOT NULL,";
				SQL = SQL + "created_at TEXT NOT NULL";
				SQL = SQL + ");";
				SQLList.add(SQL);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return SQLList;
	}

	public static String convertCurrentTime(){
		return DateFormat.format("yyyy-MM-dd_kk.mm.ss", System.currentTimeMillis()).toString();
	}
}
