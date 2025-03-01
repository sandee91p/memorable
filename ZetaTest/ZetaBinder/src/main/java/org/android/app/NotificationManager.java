package org.android.app;

import android.os.IInterface;

import org.Reflector;

public class NotificationManager {
    public static final Reflector REF = Reflector.on("android.app.NotificationManager");

    public static Reflector.FieldWrapper<IInterface> sService = REF.field("sService");
    public static Reflector.StaticMethodWrapper<IInterface> getService = REF.staticMethod("getService");
}
