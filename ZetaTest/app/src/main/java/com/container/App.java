package com.container;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.zetaco.ZetaBCore;
import com.zetaco.app.configuration.ClientConfiguration;


public class App extends Application {
    private static final String TAG = "App";
    private static App instance = null;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            ZetaBCore.get().doAttachBaseContext(base, new ClientConfiguration() {
                @Override
                public String getHostPackageName() {
                    return base.getPackageName();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        try {
            ZetaBCore.get().doCreate();
            Log.i(TAG, "ZetaBCore initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Failed to create ZetaBCore", e);
            Toast.makeText(this, "Failed to initialize virtualization engine", Toast.LENGTH_LONG).show();
        }
    }
}