package com.zetaco.app;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.zetaco.ZetaBCore;
import com.zetaco.R;
import com.zetaco.utils.Slog;

public class LauncherActivity extends Activity {
    public static final String TAG = "SplashScreen";

    public static final String KEY_INTENT = "launch_intent";
    public static final String KEY_PKG = "launch_pkg";
    public static final String KEY_USER_ID = "launch_user_id";
    private boolean isRunning = false;
    private boolean UnRunning = false;

    public static void launch(Intent intent, int userId) {
        Intent splash = new Intent();
        splash.setClass(ZetaBCore.getContext(), LauncherActivity.class);
        splash.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        splash.putExtra(LauncherActivity.KEY_INTENT, intent);
        splash.putExtra(LauncherActivity.KEY_PKG, intent.getPackage());
        splash.putExtra(LauncherActivity.KEY_USER_ID, userId);
        ZetaBCore.getContext().startActivity(splash);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //这里解决多进程问题
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }

        Intent launchIntent = intent.getParcelableExtra(KEY_INTENT);
        String packageName = intent.getStringExtra(KEY_PKG);
        int userId = intent.getIntExtra(KEY_USER_ID, 0);

        PackageInfo packageInfo = ZetaBCore.getBPackageManager().getPackageInfo(packageName, 0, userId);
        if (packageInfo == null) {
            Slog.e(TAG, packageName + " not installed!");
            finish();
            return;
        }
        Drawable drawable = packageInfo.applicationInfo.loadIcon(ZetaBCore.getPackageManager());
        setContentView(R.layout.activity_launcher);

        findViewById(R.id.iv_icon)
                .setBackground(drawable);
        new Thread(() -> ZetaBCore.getBActivityManager().startActivity(launchIntent, userId)).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRunning) {
            finish();
        }
    }
}
