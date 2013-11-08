package com.taku.kobayashi.sqlite_sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

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

public class MigrationConfig{

	private static final String TABLENAME1 = "tweet";

	public static ArrayList<String> createTable() {
		ArrayList<String> SQLList = new ArrayList<String>();
		HashMap<String, String[]> tableDatas = tableSettings();
		for(Entry<String, String[]> e : tableDatas.entrySet()) {
			String SQL = "create table "+ e.getKey() +"(";
			String[] columnList = e.getValue();
			for(int i = 0;i < columnList.length; ++i){
				SQL = SQL + columnList[i];
				if(i != columnList.length - 1){
					SQL = SQL + ",";
				}
			}
			SQL = SQL + ");";
			SQLList.add(SQL);
		}
		return SQLList;
	}

	//ここに定数を打ち込んでいく
	private static HashMap<String, String[]> tableSettings(){
		HashMap<String, String[]> tableData = new HashMap<String, String[]>();
		//TODO nameは後でuserIdに変わる予定
		String[] columns1 = {
			"id INTEGER PRIMARY KEY AUTOINCREMENT",
			"name TEXT",
			"message TEXT",
			"updated_at TEXT NOT NULL",
			"created_at TEXT NOT NULL"
		};
		tableData.put(TABLENAME1, columns1);
		return tableData;
	}
}
