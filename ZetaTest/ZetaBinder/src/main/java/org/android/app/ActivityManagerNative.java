package org.android.app;

import android.os.IInterface;

import org.Reflector;

public class ActivityManagerNative {
    public static final Reflector REF = Reflector.on("android.app.ActivityManagerNative");

    public static Reflector.FieldWrapper<Object> gDefault = REF.field("gDefault");

    public static Reflector.StaticMethodWrapper<IInterface> getDefault = REF.staticMethod("getDefault");
}
