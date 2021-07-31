package com.Anakin.drink_reminder;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/2.ttf")
                .setFontAttrId(R.attr.fontPath)

                .build()
        );
    }
}
