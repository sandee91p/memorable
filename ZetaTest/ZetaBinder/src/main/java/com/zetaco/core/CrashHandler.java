package com.zetaco.core;

import androidx.annotation.NonNull;

import com.zetaco.ZetaBCore;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private final Thread.UncaughtExceptionHandler mDefaultHandler;

    public static void create() {
        new CrashHandler();
    }

    public CrashHandler() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        if (ZetaBCore.get().getExceptionHandler() != null) {
            ZetaBCore.get().getExceptionHandler().uncaughtException(t, e);
        }
        mDefaultHandler.uncaughtException(t, e);
    }
}
