package org.android.util;

import org.Reflector;

public class Singleton {
    public static final Reflector REF = Reflector.on("android.util.Singleton");

    public static Reflector.FieldWrapper<Object> mInstance = REF.field("mInstance");

    public static Reflector.MethodWrapper<Object> get = REF.method("get");
}
