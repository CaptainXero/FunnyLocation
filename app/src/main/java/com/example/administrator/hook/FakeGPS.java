package com.example.administrator.hook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.ui.MainActivity;
import com.example.administrator.utils.DbHelper;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.example.administrator.ui.MainActivity.mylatitude;
import static com.example.administrator.ui.MainActivity.mylongitude;

/**
 * Created by Administrator on 2016/9/1 0001.
 */

public class FakeGPS implements IXposedHookLoadPackage{
    private int lac = 0;
    private int cid = 0;
    private DbHelper myDbHelper;
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        final Object activityThread = XposedHelpers.callStaticMethod(XposedHelpers.findClass("android.app.ActivityThread", null), "currentActivityThread");
        final Context systemContext = (Context) XposedHelpers.callMethod(activityThread, "getSystemContext");
        //过滤掉包名不是 com.tencent.mm(微信) 的应用
        String packname = "com.tencent.mm";
        String correntPackage = loadPackageParam.packageName.toString();
        if(!correntPackage.equals(packname)) {
            return;
        }else {
            XposedBridge.log("Loaded_APP: " + loadPackageParam.packageName);
        }
        SQLiteDatabase db = new DbHelper(systemContext).getWritableDatabase();
        Cursor cursor = db.query("Location",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                Double la = cursor.getDouble(cursor.getColumnIndex("latitude"));
                Double lon = cursor.getDouble(cursor.getColumnIndex("longitude"));
                XposedBridge.log("DB_Location: " + "latitude" + la + "\n" + "longitude" + lon);
            }while (cursor.moveToNext());
        }
        cursor.close();

        HookUtils.HookAndChange(loadPackageParam.classLoader,mylatitude,mylongitude, lac, cid);

        XposedBridge.log("Hook_Finish: " + loadPackageParam.packageName + "," + mylatitude + "," + mylongitude + "," + lac + "," + cid);

    }


}
