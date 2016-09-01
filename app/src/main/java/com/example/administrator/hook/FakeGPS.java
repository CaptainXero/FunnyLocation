package com.example.administrator.hook;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.Random;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by Administrator on 2016/9/1 0001.
 */

public class FakeGPS implements IXposedHookLoadPackage{
    private double latitude = 31.2307;
    private double longitude =121.5751;
    private int lac = 0;
    private int cid = 0;
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        //过滤掉包名不是 com.tencent.mm(微信) 的应用
        String packname = "com.tencent.mm";
        String correntPackage = loadPackageParam.packageName.toString();
        if(!correntPackage.equals(packname)) {
            return;
        }else {
            XposedBridge.log("Loaded_APP: " + loadPackageParam.packageName);
        }
//        final Object activityThread = XposedHelpers.callStaticMethod(XposedHelpers.findClass
//                ("android.app.ActivityThread", null), "currentActivityThread");
//        final Context systemContext = (Context) XposedHelpers.callMethod(activityThread, "getSystemContext");
//        Uri uri = Uri.parse("content://name.caiyao.fakegps.data.AppInfoProvider/app");
//        Cursor cursor = systemContext.getContentResolver().query(uri, new String[]{"latitude", "longitude","lac","cid"},
//                "package_name=?", new String[]{loadPackageParam.packageName}, null);
//        if (cursor != null && cursor.moveToNext()) {
//            //41019, 18511
//            double latitude = cursor.getDouble(cursor.getColumnIndex("latitude")) +
//                    (double) new Random().nextInt(100) / 1000000 + ((double) new Random().nextInt(99999999)) / 100000000000000d;
//            double longitude = cursor.getDouble(cursor.getColumnIndex("longitude")) +
//                    (double) new Random().nextInt(100) / 1000000 + ((double) new Random().nextInt(99999999)) / 100000000000000d;
//            int lac = cursor.getInt(cursor.getColumnIndex("lac"));
//            int cid = cursor.getInt(cursor.getColumnIndex("cid"));
            XposedBridge.log("模拟位置:" + loadPackageParam.packageName + "," + latitude + "," + longitude + "," + lac + "," + cid);
            HookUtils.HookAndChange(loadPackageParam.classLoader, latitude, longitude, lac, cid);
//            cursor.close();
    }
}
