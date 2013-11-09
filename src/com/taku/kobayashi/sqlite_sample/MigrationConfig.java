package com.taku.kobayashi.sqlite_sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class MigrationConfig{

	//DBのバージョン番号、これを変えると、upgradeが走る
	public static final int DBVERSION = 1;
	public static final String TWEET_TABLE = "tweet";

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
		tableData.put(TWEET_TABLE, columns1);
		return tableData;
	}
}
