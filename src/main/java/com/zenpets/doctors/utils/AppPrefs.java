package com.zenpets.doctors.utils;

import android.app.Application;

import com.zenpets.doctors.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class AppPrefs extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /** INSTANTIATE THE CALLIGRAPHY LIBRARY **/
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/HelveticaNeueLTW1G-Cn.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }
}