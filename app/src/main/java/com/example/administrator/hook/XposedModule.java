package com.example.administrator.hook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by Administrator on 2016/8/23 0023.
 */

public class XposedModule implements IXposedHookLoadPackage{
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        //过滤掉包名不是 com.example.administrator.logintest 的应用
        String packname = "com.example.administrator.logintest";
        String correntPackage = loadPackageParam.packageName.toString();
        if(!correntPackage.equals(packname)) {
            return;
        }else {
            XposedBridge.log("Loaded_APP: " + loadPackageParam.packageName);
            //Hook MainActivity 中的 checkInfo 方法
            findAndHookMethod("com.example.administrator.logintest.MainActivity",
                    loadPackageParam.classLoader, "checkInfo", String.class, String.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            XposedBridge.log("BeginJack:");
                            XposedBridge.log("Param_one = " + param.args[0]);
                            XposedBridge.log("Param_two = " + param.args[1]);
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            XposedBridge.log("JackFinish:");
                            XposedBridge.log("Param_one = " + param.args[0]);
                            XposedBridge.log("Param_two = " + param.args[1]);
                        }
                    });
        }
    }
}
