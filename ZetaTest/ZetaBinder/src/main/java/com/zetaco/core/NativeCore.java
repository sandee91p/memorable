package com.zetaco.core;

import android.annotation.SuppressLint;
import android.os.Binder;
import android.os.Process;
import android.util.Log;

import androidx.annotation.Keep;

import java.io.File;

import com.zetaco.ZetaBCore;
import com.zetaco.app.BActivityThread;

@SuppressLint({"UnsafeDynamicallyLoadedCode", "SdCardPath"})
public class NativeCore {
    public static final String TAG = "NativeCore";

    static {
        System.loadLibrary("zetalib");

    }

    public static native void init(int apiLevel);

    public static native void enableIO();

    public static native void addWhiteList(String path);

    public static native void addIORule(String targetPath, String relocatePath);

    private static native void nativeIORedirect(String origPath, String newPath);

    public static native void hideXposed();

    @Keep
    public static int getCallingUid(int origCallingUid) {
        // 系统uid
        if (origCallingUid > 0 && origCallingUid < Process.FIRST_APPLICATION_UID) {
            return origCallingUid;
        }
        // 非用户应用
        if (origCallingUid > Process.LAST_APPLICATION_UID) {
            return origCallingUid;
        }

        if (origCallingUid == ZetaBCore.getHostUid()) {
            int callingPid = Binder.getCallingPid();
            int bUid = ZetaBCore.getBPackageManager().getUidByPid(callingPid);
            if (bUid != -1) {
                return bUid;
            }
            return BActivityThread.getCallingBUid();
        }
        return origCallingUid;
    }

    @Keep
    public static String redirectPath(String path) {
        return IOCore.get().redirectPath(path);
    }

    @Keep
    public static File redirectPath(File path) {
        return IOCore.get().redirectPath(path);
    }

//    // Native methods
//    public static native void enableCustomInput(boolean enable);
//    public static native void enableSwapTouch(boolean enable);
//    public static native void injectSwipe(float x1, float y1, float x2, float y2);
//    public static native void injectClick(float x, float y);
}
