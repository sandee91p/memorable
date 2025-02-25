package org.android.os;

import org.Reflector;

public class Process {
    public static final Reflector REF = Reflector.on("android.os.Process");

    public static Reflector.StaticMethodWrapper<Void> setArgV0 = REF.staticMethod("setArgV0", String.class);
}
