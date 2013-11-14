package com.taku.kobayashi.sqlite_sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
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
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.Menu;

//ActiveRecordっぽいものを作ってみました
public class ActiveRecordPartial{

	private static final String TAG = "SQLite_Sample_ActiveRecordPartial";
	private SQLiteDatabase _db;
	protected String _tableName;
	private ArrayList<String> _whereList;
	private ArrayList<String> _orderList;
	private ArrayList<String> _selectList;
	private String _limitSQL = "";

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	//継承先でmodelとして使いたいときはthis.getClass().getSimpleName()でテーブル名をとってきて扱う方がいい
	public ActiveRecordPartial(Context context, String tableName){
		String dbName = context.getPackageName();
		DBOpenHelper DBHelper = new DBOpenHelper(context, dbName, MigrationConfig.DBVERSION);
		_db = DBHelper.getWritableDatabase();
		_tableName = tableName;
		_whereList = new ArrayList<String>();
		_orderList = new ArrayList<String>();
		_selectList = new ArrayList<String>();
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	//TODO
	public static void importInstances(ArrayList<ActiveRecordPartialInstance> list){
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	//TODO
	public ActiveRecordPartialInstance newInstance(){
		return null;
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	//TODO
	public ActiveRecordPartialInstance newInstance(Bundle data){
		return null;
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	//TODO
	public ActiveRecordPartialInstance newInstance(Map<String,Object> data){
		return null;
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public ActiveRecordPartialInstance create(String key,Object data){
		Bundle b = new Bundle();
		ContentValues cv = new ContentValues();
		Tools.putObjectToContentValues(cv, key, data);
		b.putString(key, data.toString());
		_db.insert(_tableName, null, cv);
		this.release();
		return new ActiveRecordPartialInstance(this, b);
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public ActiveRecordPartialInstance create(Map<String,Object> data){
		ContentValues cv = new ContentValues();
		Bundle b = new Bundle();
		for(Entry<String, Object> e : data.entrySet()) {
			if(e.getValue() == null) continue;
			Tools.putObjectToContentValues(cv, e.getKey(), e.getValue());
			b.putString(e.getKey(), e.getValue().toString());
		}
		_db.insert(_tableName, null, cv);
		this.release();
		return new ActiveRecordPartialInstance(this, b);
		//bulk_insertする時用
		//db.execSQL("insert into person_table(name,age) values (?, ?);",new Object[]{"本田 圭佑",24});
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public ActiveRecordPartialInstance create(Bundle data){
		Set<String> keys = data.keySet();
		ContentValues cv = new ContentValues();
		Bundle b = new Bundle();
		for (String key : keys) {
			if(data.get(key) == null) continue;
			Tools.putObjectToContentValues(cv, key, data.get(key));
			b.putString(key, data.get(key).toString());
		}
		_db.insert(_tableName, null, cv);
		this.release();
		return new ActiveRecordPartialInstance(this, b);
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public String to_sql(){
		return makeSelectSQL();
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public ArrayList<ActiveRecordPartialInstance> all(){
		String sql = makeSelectSQL();
		ArrayList<ActiveRecordPartialInstance> list = new ArrayList<ActiveRecordPartialInstance>();
		Cursor data = _db.rawQuery(sql, new String[]{});
		this.release();
		boolean next = data.moveToFirst();
		while (next) {
			Bundle b = new Bundle();
			int columnCount = data.getColumnCount();
			for(int i = 0;i < columnCount; ++i){
				Tools.putCursorToBundle(b, data);
			}
			list.add(new ActiveRecordPartialInstance(this, b));
			next = data.moveToNext();
		}
		data.close();
		return list;
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public ActiveRecordPartialInstance first(){
		_limitSQL = " LIMIT 1";
		String sql = makeSelectSQL();
		Bundle b = new Bundle();
		Cursor data = _db.rawQuery(sql, new String[]{});
		this.release();
		boolean first = data.moveToFirst();
		if(first){
			Tools.putCursorToBundle(b, data);
			data.close();
			return new ActiveRecordPartialInstance(this, b);
		}else{
			data.close();
			return null;
		}
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public ActiveRecordPartialInstance last(){
		_limitSQL = " LIMIT 1";
		String sql = makeSelectSQL();
		Bundle b = new Bundle();
		Cursor data = _db.rawQuery(sql, new String[]{});
		this.release();
		boolean last = data.moveToLast();
		if(last){
			Tools.putCursorToBundle(b, data);
			data.close();
			return new ActiveRecordPartialInstance(this, b);
		}else{
			data.close();
			return null;
		}
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------


	public ActiveRecordPartial where(Map<String,Object> data){
		for(Entry<String, Object> e : data.entrySet()) {
			if(e.getValue() == null) continue;
			String whereSQL = "";
			whereSQL = "'" + e.getKey() + "' = " + e.getValue();
			_whereList.add(whereSQL);
		}
		return this;
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public ActiveRecordPartial where(Bundle data){
		Set<String> keys = data.keySet();
		for (String key : keys) {
			if(data.get(key) == null) continue;
			String whereSQL = "";
			whereSQL = "'" + key + "' = " + data.get(key).toString();
			_whereList.add(whereSQL);
		}
		return this;
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public ActiveRecordPartial where(String str){
		_whereList.add(str);
		return this;
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public int update_all(Map<String,Object> data){
		ContentValues cv = new ContentValues();
		for(Entry<String, Object> e : data.entrySet()) {
			if(e.getValue() == null) continue;
			Tools.putObjectToContentValues(cv, e.getKey(), e.getValue());
		}
		int count = _db.update(_tableName, cv, Tools.join(_whereList, " AND "), null);
		this.release();
		return count;
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public int update_all(Bundle data){
		Set<String> keys = data.keySet();
		ContentValues cv = new ContentValues();
		for (String key : keys) {
			if(data.get(key) == null) continue;
			Tools.putObjectToContentValues(cv, key, data.get(key));
		}
		int count = _db.update(_tableName, cv, Tools.join(_whereList, " AND "), null);
		this.release();
		return count;
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public int destroy_all(){
		int count = _db.delete(_tableName, Tools.join(_whereList, " AND "), null);
		this.release();
		return count;
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public int count(){
		String sql = makeSelectSQL();
		Cursor data = _db.rawQuery(sql, new String[]{});
		return data.getCount();
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	//SQLを発行するものは全部一度SQLの条件をクリアにする必要があるので、このメソッドを呼ぶ
	private void release(){
		_whereList.clear();
		_orderList.clear();
		_selectList.clear();;
		_limitSQL = "";
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	private String makeSelectSQL(){
		String sql = "";
		if(_selectList.isEmpty()){
			sql = "SELECT '"+ _tableName + "'.* FROM '" + _tableName + "'";
		}else{
			String selects = "";
			selects = Tools.join(_selectList, ",");
			sql = "SELECT " + selects + " FROM '" + _tableName + "'";
		}
		String wheres = "";
		wheres = Tools.join(_whereList, " AND ");
		if(wheres.isEmpty() == false){
			sql = sql + " WHERE " + wheres;
		}
		String orders = "";
		orders = Tools.join(_orderList, ",");
		if(_orderList.isEmpty() == false){
			sql = sql + " ORDER BY " + orders;
		}
		sql = sql + _limitSQL;
		sql = sql + ";";
		return sql;
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------
}
