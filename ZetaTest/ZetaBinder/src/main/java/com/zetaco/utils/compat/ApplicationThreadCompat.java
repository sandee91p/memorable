package com.zetaco.utils.compat;

import android.os.IBinder;
import android.os.IInterface;

import org.android.app.ApplicationThreadNative;
import org.android.app.IApplicationThread;

public class ApplicationThreadCompat {
    public static IInterface asInterface(IBinder binder) {
        if (BuildCompat.isOreo()) {
            return IApplicationThread.Stub.asInterface.call(binder);
        }
        return ApplicationThreadNative.asInterface.call(binder);
    }
}
