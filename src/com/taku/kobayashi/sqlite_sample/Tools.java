package com.taku.kobayashi.sqlite_sample;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
					SQL = SQL + tableColumns.getString(columnName);
					SQL = SQL + ",";
					Log.d(TAG,SQL);
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
		return DateFormat.format("yyyy/MM/dd kk:mm:ss", System.currentTimeMillis()).toString();
	}

	public static Date convertStringToDate(String dateString){
		if(dateString == null) return null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
		try {
			return sdf.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String join(String[] list, String with) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < list.length; i++) {
		if (i != 0) { buf.append(with);}
			buf.append(list[i]);
		}
		return buf.toString();
	}

	public static String join(ArrayList<String> list, String with) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			if (i != 0) { buf.append(with);}
			buf.append(list.get(i));
		}
		return buf.toString();
	}

	public static final void putObjectToContentValues(ContentValues cv, String key, Object value){
		if(value instanceof String){
			String v = (String) value;
			cv.put(key, v);
		}else if(value instanceof Byte){
			byte v = (Byte) value;
			cv.put(key, v);
		}else if(value instanceof byte[]){
			byte[] v = (byte[]) value;
			cv.put(key, v);
		}else if(value instanceof Integer){
			int v = (Integer) value;
			cv.put(key, v);
		}else if(value instanceof Float){
			float v = (Float) value;
			cv.put(key, v);
		}else if(value instanceof Short){
			short v = (Short) value;
			cv.put(key, v);
		}else if(value instanceof Double){
			double v = (Double) value;
			cv.put(key, v);
		}else if(value instanceof Long){
			long v = (Long) value;
			cv.put(key, v);
		}else if(value instanceof Boolean){
			boolean v = (Boolean) value;
			cv.put(key, v);
		}
	}

	public static final void putCursorToBundle(Bundle b, Cursor c){
		String[] columnNames = c.getColumnNames();
		for(int i = 0;i < columnNames.length; ++i){
			String key = columnNames[i];
			String value = c.getString(i);
			b.putString(key, value);
		}
	}

	public static final void putObjectToStringBundle(Bundle b, String key, Object value){
		b.putString(key, value.toString());
	}
}
