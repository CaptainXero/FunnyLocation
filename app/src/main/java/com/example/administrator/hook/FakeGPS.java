package com.example.administrator.hook;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.administrator.utils.DbHelper;

import java.util.Random;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


/**
 * Created by Administrator on 2016/9/1 0001.
 */

public class FakeGPS implements IXposedHookLoadPackage{

    private DbHelper myDbHelper;
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        final Object activityThread = XposedHelpers.callStaticMethod(XposedHelpers
                .findClass("android.app.ActivityThread", null), "currentActivityThread");
        final Context systemContext = (Context) XposedHelpers.callMethod(activityThread, "getSystemContext");
        Uri uri = Uri.parse("content://com.example.administrator.utils.AppInfoProvider/app");
        Cursor cursor = systemContext.getContentResolver().query(uri, new String[]{"latitude", "longitude","lac","cid"}
                , "package_name=?", new String[]{loadPackageParam.packageName}, null);
        if (cursor != null && cursor.moveToNext()) {
            //41019, 18511
            double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"))
                    + (double) new Random().nextInt(100) / 1000000 + ((double) new Random().nextInt(99999999)) / 100000000000000d;
            double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"))
                    + (double) new Random().nextInt(100) / 1000000 + ((double) new Random().nextInt(99999999)) / 100000000000000d;
            int lac = cursor.getInt(cursor.getColumnIndex("lac"));
            int cid = cursor.getInt(cursor.getColumnIndex("cid"));
            XposedBridge.log("模拟位置:" + loadPackageParam.packageName + "," + latitude + "," + longitude + "," + lac + "," + cid);
            HookUtils.HookAndChange(loadPackageParam.classLoader, latitude, longitude, lac, cid);
            cursor.close();
        }
    }

}
