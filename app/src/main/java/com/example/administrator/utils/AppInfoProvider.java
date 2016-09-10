package com.example.administrator.utils;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2016/9/10 0010.
 */

public class AppInfoProvider extends ContentProvider {
    public static final String AUTHRITY = "com.example.administrator.utils.AppInfoProvider";
    public static final Uri APP_CONTENT_URI = Uri.parse("content://" + AUTHRITY + "/app");
    public static final int APP_URI_CODE = 0;
    private static final UriMatcher myUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        myUriMatcher.addURI(AUTHRITY, "app", APP_URI_CODE);
    }
    private SQLiteDatabase mySQLiteDb;

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String tablename = getTableName(uri);
        if(tablename == null)
            throw new IllegalArgumentException("UnKnowURI: "+uri);
        return mySQLiteDb.query(tablename, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String tablename = getTableName(uri);
        if(tablename == null)
            throw new IllegalArgumentException("UnKnowURI: " + uri);
        int count = mySQLiteDb.delete(tablename, selection, selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }
    //在Provider被使用时执行OnCreate函数，初始化数据库等
    @Override
    public boolean onCreate() {
        mySQLiteDb = new DbHelper(getContext()).getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        String tablename = getTableName(uri);
        if(tablename == null)
            throw new IllegalArgumentException("UnKnowURI:" + uri);
        mySQLiteDb.insert(tablename, null, contentValues);
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        String tablename = getTableName(uri);
        if(tablename == null)
            throw new IllegalArgumentException("UnKnowURI:" + uri);
        int row = mySQLiteDb.update(tablename,contentValues,selection,selectionArgs);
        if (row>0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return row;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }
    private String getTableName(Uri uri) {
        String tableName = null;
        switch (myUriMatcher.match(uri)) {
            case APP_URI_CODE:
                tableName = DbHelper.APP_TABLE_NAME;
                break;
        }
        return tableName;
    }
}
