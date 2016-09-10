package com.example.administrator.utils;

import android.app.Application;
import android.content.Context;

import com.example.administrator.ui.MainActivity;

/**
 * Created by Administrator on 2016/9/3 0003.
 */

public class ContextUtils extends Application{
    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

}
