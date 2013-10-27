package com.taku.kobayashi.sqlite_sample;

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

	//context, DB名, カーソル, バージョン番号
	//データベースのクエリ結果に対してカーソルが特別な操作や検証などをやるような
	//第三引数は自作のカーソルクラスを拡張したものを作るときには、このカーソルを渡すもの。デフォルトの設定でいい時はnull
	public DBOpenHelper(Context context, String DBName, int version) {
		super(context, DBName, null, version);
		Log.d(TAG, "open");
	}

	public DBOpenHelper(Context context, String DBName, CursorFactory factory,int version) {
		super(context, DBName, factory, version);
	}

	//初回はcreateされるが二回目以降はcreateされない
	@Override
	public void onCreate(SQLiteDatabase db) {
		// table create
		db.execSQL(
			"create table sample_table("+
			"   name text not null,"+
			"   age text"+
			");"
		);
		Log.d(TAG, "create");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "upgrade");
	}

}
