package org.android.app;

import org.Reflector;

public class ContextImplKitkat {
    public static final Reflector REF = Reflector.on("android.app.ContextImpl");

    public static Reflector.FieldWrapper<String> mOpPackageName = REF.field("mOpPackageName");
}
