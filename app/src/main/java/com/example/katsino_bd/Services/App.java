package com.example.katsino_bd.Services;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.SharedPreferences;

public class App extends Application {
    public SharedPreferences settings;
    public SharedPreferences.Editor editor;
    private static App instance;
    public static final App instance() {
        if (instance != null)
            return instance;
        throw new RuntimeException("App not instantiated yet!");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        settings = getSharedPreferences("UserInfo", 0);
        editor = settings.edit();
    }
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) { // Works for Activity
            String action = "APP_NOT_VISIBLE";
            Intent myService = new Intent(this, BackgroundSoundService.class);
            myService.setAction(action);
            startService(myService);
        }
        else if (level == ComponentCallbacks2.TRIM_MEMORY_COMPLETE) { // Works for FragmentActivty
            String action = "APP_NOT_VISIBLE";
            Intent myService = new Intent(this, BackgroundSoundService.class);
            myService.setAction(action);
            startService(myService);
        }
    }
}
