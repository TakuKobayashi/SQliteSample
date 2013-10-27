package com.taku.kobayashi.sqlite_sample;

import java.util.ArrayList;
import java.util.Iterator;

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
import android.util.Log;
import android.view.Menu;

public class DBOpenHelper extends SQLiteOpenHelper {

	private static final String TAG = "SQLite_Sample_DBOpenHelper";
	private static final String MIGRATION_JSON_FILE_NAME = "migrations.json";
	private Context _context;

	//context, DB名, カーソル, バージョン番号
	//データベースのクエリ結果に対してカーソルが特別な操作や検証などをやるような
	//第三引数は自作のカーソルクラスを拡張したものを作るときには、このカーソルを渡すもの。デフォルトの設定でいい時はnull
	public DBOpenHelper(Context context, String DBName, int version) {
		super(context, DBName, null, version);
		_context = context;
		Log.d(TAG, "open");
	}

	public DBOpenHelper(Context context, String DBName, CursorFactory factory,int version) {
		super(context, DBName, factory, version);
		_context = context;
	}

	//初回はcreateされるが二回目以降はcreateされない
	@Override
	public void onCreate(SQLiteDatabase db) {
		JSONObject migrationJSON = Tools.loadJSONFromAsset(_context, MIGRATION_JSON_FILE_NAME);
		ArrayList<String> SQLList = Tools.makeMigrateSQLList(migrationJSON);
		Log.d(TAG, ""+SQLList);
		for(int i = 0; i < SQLList.size(); i++){
			db.execSQL(SQLList.get(i));
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "upgrade");
	}

}
