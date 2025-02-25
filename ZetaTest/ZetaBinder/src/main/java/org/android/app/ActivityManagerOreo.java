package org.android.app;

import org.Reflector;

public class ActivityManagerOreo {
    public static final Reflector REF = Reflector.on("android.app.ActivityManager");

    public static Reflector.FieldWrapper<Object> IActivityManagerSingleton = REF.field("IActivityManagerSingleton");
}
