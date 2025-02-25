package org.android.app;

import android.os.IBinder;
import android.os.IInterface;

import org.Reflector;

public class ApplicationThreadNative {
    public static final Reflector REF = Reflector.on("android.app.ApplicationThreadNative");

    public static Reflector.StaticMethodWrapper<IInterface> asInterface = REF.staticMethod("asInterface", IBinder.class);
}
