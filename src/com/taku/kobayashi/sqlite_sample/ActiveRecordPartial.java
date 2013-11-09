package com.taku.kobayashi.sqlite_sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
import android.util.Log;
import android.view.Menu;

//ActiveRecordっぽいものを作ってみました
public class ActiveRecordPartial{

	private static final String TAG = "SQLite_Sample_ActiveRecordPartial";;
	private SQLiteDatabase _db;
	protected String _tableName;
	private String _sql;

	//継承先でmodelとして使いたいときはthis.getClass().getSimpleName()でテーブル名をとってきて扱う方がいい
	public ActiveRecordPartial(Context context, String tableName){
		String dbName = context.getPackageName();
		DBOpenHelper DBHelper = new DBOpenHelper(context, dbName, MigrationConfig.DBVERSION);
		_db = DBHelper.getWritableDatabase();
		Log.d(TAG, dbName);
		Log.d(TAG, this.getClass().getSimpleName());
		_tableName = tableName;
	}

	public void create(String key,Object data){
		if(data == null) return;
		ContentValues cv = new ContentValues();
		putObjectToContentValues(cv, key, data);
		_db.insert(_tableName, null, cv);
	}

	public void create(Map<String,Object> data){
		ContentValues cv = new ContentValues();
		for(Entry<String, Object> e : data.entrySet()) {
			if(e.getValue() == null) continue;
			putObjectToContentValues(cv, e.getKey(), e.getValue());
		}
		_db.insert(_tableName, null, cv);
	}

	public void create(Bundle data){
		Set<String> keys = data.keySet();
		ContentValues cv = new ContentValues();
		for (String key : keys) {
			if(data.get(key) == null) continue;
			putObjectToContentValues(cv, key, data.get(key));
		}
		_db.insert(_tableName, null, cv);
	}

	public Cursor all(){
		_sql = "SELECT '"+ _tableName + "'.* FROM " + _tableName + ";";
		return _db.rawQuery(_sql, new String[]{});
	}


	public void update_attributes(Map<String,Object> data){
	}

	public void update_attributes(Bundle data){

	}

	private final void putObjectToContentValues(ContentValues cv, String key, Object value){
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
}
