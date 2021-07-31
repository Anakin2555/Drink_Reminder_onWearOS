package com.Anakin.drink_reminder;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class Activities {


    //用来存储所有创建的activity
    private List<Activity> list = new ArrayList<Activity>();

    private static Activities exit=null;

    private Activities() {

    }

    public static Activities getInstance() {
        if (null == exit) {
            exit = new Activities();
        }
        return exit;
    }
    //添加新创建的activity
    public void addActivity(Activity activity) {
        list.add(activity);
    }
    //关闭所有activity，退出程序
    public void exit(Context context) {
        for (Activity activity : list) {
            activity.finish();
        }

    }
}
