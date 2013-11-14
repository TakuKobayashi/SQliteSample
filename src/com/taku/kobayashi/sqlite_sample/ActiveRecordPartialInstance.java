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
public class ActiveRecordPartialInstance{

	private static final String TAG = "SQLite_Sample_ActiveRecordPartial";
	private ActiveRecordPartial _activeRecordPartial;
	private Bundle _instanceValue;

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	//継承先でmodelとして使いたいときはthis.getClass().getSimpleName()でテーブル名をとってきて扱う方がいい
	public ActiveRecordPartialInstance(ActiveRecordPartial arp, Bundle values){
		_activeRecordPartial = arp;
		_instanceValue = values;
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	//parseは各々やってください
	public String get(String key){
		return _instanceValue.getString(key);
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	//parseは各々やってください
	public void set(String key, String value){
		_instanceValue.putString(key,value);
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	//TODO
	public void save(){

	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public Bundle attributes(){
		return _instanceValue;
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public void update_attributes(Map<String,Object> data){
		_activeRecordPartial.where("id = " + _instanceValue.get("id")).update_all(data);
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public void update_attributes(Bundle data){
		_activeRecordPartial.where("id = " + _instanceValue.get("id")).update_all(data);
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public void destroy(){
		_activeRecordPartial.where("id = " + _instanceValue.get("id")).destroy_all();
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

}
