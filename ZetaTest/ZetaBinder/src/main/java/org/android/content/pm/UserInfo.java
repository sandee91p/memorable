package org.android.content.pm;

import org.Reflector;

public class UserInfo {
    public static final Reflector REF = Reflector.on("android.content.pm.UserInfo");

    public static Reflector.ConstructorWrapper<Object> _new = REF.constructor(int.class, String.class, int.class);

    public static Reflector.FieldWrapper<Integer> FLAG_PRIMARY = REF.field("FLAG_PRIMARY");
}
