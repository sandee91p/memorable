package org.android.os;

import org.Reflector;

public class UserHandle {
    public static final Reflector REF = Reflector.on("android.os.UserHandle");

    public static Reflector.StaticMethodWrapper<Integer> myUserId = REF.staticMethod("myUserId");
}
