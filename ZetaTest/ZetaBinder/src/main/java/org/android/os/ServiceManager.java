package org.android.os;

import android.os.IBinder;

import java.util.Map;

import org.Reflector;

public class ServiceManager {
    public static final Reflector REF = Reflector.on("android.os.ServiceManager");

    public static Reflector.FieldWrapper<Map<String, IBinder>> sCache = REF.field("sCache");

    public static Reflector.StaticMethodWrapper<IBinder> getService = REF.staticMethod("getService", String.class);
}
