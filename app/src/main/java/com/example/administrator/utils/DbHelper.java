package com.example.administrator.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/9/7 0007.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "laLong.db";
    private static final int DB_VERSION = 1;

    public DbHelper(Context context) {
        super(context,DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_APP_TABLE = "create table Location ("
                +"id integer primary key autoincrement,"
                +"latitude double,"
                +"longitude double)";
        sqLiteDatabase.execSQL(CREATE_APP_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
