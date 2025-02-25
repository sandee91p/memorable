package com.container;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.container.utils.LoadingAnimation;
import com.container.utils.PermissionHelper;
import com.container.utils.PrefsHelper;
import com.zetaco.ZetaBCore;
import com.zetaco.entity.pm.InstallResult;
import com.ztest.R;

import java.io.File;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_BASIC_PERMISSIONS = 5678;

    private EditText editTextPkg;
    private Button clone, install, start, stop, uninstall;
    private TextView Status, systemLog;
    private LoadingAnimation loadingAnimation;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private PrefsHelper prefsHelper;
    private PermissionHelper permissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefsHelper = new PrefsHelper(this);
        permissionHelper = new PermissionHelper(this);
        checkAndRequestPermissions();
        setupUI();
    }

    private void checkAndRequestPermissions() {
        Log.d(TAG, "Checking permissions");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!permissionHelper.hasAllPermissions()) {
                Log.d(TAG, "Permissions missing, requesting permissions");
                permissionHelper.requestAllPermissions();
            } else {
                Log.d(TAG, "All permissions granted, proceeding with server action");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "Activity result received: " + requestCode + ", result: " + resultCode);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            permissionHelper.onActivityResult(requestCode, resultCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "Permission result received for request: " + requestCode);

        if (requestCode == REQUEST_BASIC_PERMISSIONS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (permissionHelper.hasAllPermissions()) {
                    Log.d(TAG, "All permissions granted after permission request");
                } else {
                    showPermissionError();
                }
            }
        }
    }

    private void showPermissionError() {
        Log.d(TAG, "Showing permission error dialog");
        new AlertDialog.Builder(this)
                .setTitle("Permissions Required")
                .setMessage("This app requires certain permissions to function properly. Please grant all required permissions.")
                .setPositiveButton("Grant Permissions", (dialog, which) -> checkAndRequestPermissions())
                .setNegativeButton("Cancel", (dialog, which) -> Log.d(TAG, "User cancelled permission request"))
                .show();
    }

    private void setupUI() {
        initializeViews();
        initializeViewsOnClick();
    }

    private void initializeViews() {
        Status = findViewById(R.id.Status);
        systemLog = findViewById(R.id.systemLog);
        editTextPkg = findViewById(R.id.editTextPkg);
        clone = findViewById(R.id.clone);
        install = findViewById(R.id.install);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        uninstall = findViewById(R.id.uninstall);
        loadingAnimation = new LoadingAnimation();

        editTextPkg.setText(prefsHelper.getSelectedGame());
    }

    private void initializeViewsOnClick() {
        clone.setOnClickListener(v -> handleClone());
        install.setOnClickListener(v -> handleInstall());
        start.setOnClickListener(v -> handleStart());
        stop.setOnClickListener(v -> handleStop());
        uninstall.setOnClickListener(v -> handleUninstall());
    }

    private void handleClone() {
        String pkg = editTextPkg.getText().toString().trim();
        if (pkg.isEmpty()) {
            updateStatusAndLog("Please enter a package name", false);
            return;
        }

        prefsHelper.setSelectedGame(pkg.trim());

        executeTask("Cloning", () -> {
            try {
                InstallResult result = ZetaBCore.get().installPackageAsUser(pkg, 0);
                updateStatusAndLog("Cloned: " + pkg, result.success);
            } catch (Exception e) {
                updateStatusAndLog("Clone error: " + e.getMessage(), false);
            }
        });
    }

    private void handleInstall() {
        String pkg = editTextPkg.getText().toString().trim();
        if (pkg.isEmpty()) {
            updateStatusAndLog("Please enter a package name", false);
            return;
        }

        //prefsHelper.setSelectedGame();

        executeTask("Installing", () -> {
            try {
                File apkFile = new File("/sdcard/" + pkg + ".apk");
                InstallResult result = ZetaBCore.get().installPackageAsUser(apkFile, 0);
                updateStatusAndLog("Installed: " + pkg, result.success);
            } catch (Exception e) {
                updateStatusAndLog("Install error: " + e.getMessage(), false);
            }
        });
    }

    private void handleStart() {
        String pkg = editTextPkg.getText().toString().trim();
        if (pkg.isEmpty()) {
            updateStatusAndLog("Please enter a package name", false);
            return;
        }
        executeTask("Launching", () -> {
            try {
                boolean success = ZetaBCore.get().launchApk(pkg, 0);
                updateStatusAndLog("Launched: " + pkg, success);
            } catch (Exception e) {
                updateStatusAndLog("Launch error: " + e.getMessage(), false);
            }
        });
    }

    private void handleStop() {
        String pkg = editTextPkg.getText().toString().trim();
        if (pkg.isEmpty()) {
            updateStatusAndLog("Please enter a package name", false);
            return;
        }
        executeTask("Stopping", () -> {
            try {
                ZetaBCore.get().stopPackage(pkg, 0);
                updateStatusAndLog("Stopped: " + pkg, true);
            } catch (Exception e) {
                updateStatusAndLog("Stop error: " + e.getMessage(), false);
            }
        });
    }

    private void handleUninstall() {
        String pkg = editTextPkg.getText().toString().trim();
        if (pkg.isEmpty()) {
            updateStatusAndLog("Please enter a package name", false);
            return;
        }
        executeTask("Uninstalling", () -> {
            try {
                ZetaBCore.get().uninstallPackageAsUser(pkg, 0);
                updateStatusAndLog("Uninstalled: " + pkg, true);
            } catch (Exception e) {
                updateStatusAndLog("Uninstall error: " + e.getMessage(), false);
            }
        });
    }

    private void executeTask(String taskName, Runnable task) {
        loadingAnimation.showLoading(this, taskName);
        new Thread(() -> {
            try {
                task.run();
            } finally {
                handler.post(loadingAnimation::hideLoading);
            }
        }).start();
    }

    private void updateStatusAndLog(String message, boolean isSuccess) {
        handler.post(() -> {
            Status.setText(isSuccess ? "Status: Success" : "Status: Failed");
            systemLog.append(message + "\n");
        });
    }
}