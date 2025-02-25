package org.dalvik.system;

import org.Reflector;

public class VMRuntime {
    public static final Reflector REF = Reflector.on("dalvik.system.VMRuntime");

    public static Reflector.StaticMethodWrapper<Object> getRuntime = REF.staticMethod("getRuntime");
    public static Reflector.MethodWrapper<Void> setTargetSdkVersion = REF.method("setTargetSdkVersion", int.class);
}
