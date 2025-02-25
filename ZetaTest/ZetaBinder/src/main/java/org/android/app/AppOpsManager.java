package org.android.app;

import android.os.IInterface;

import org.Reflector;

public class AppOpsManager {
    public static final Reflector REF = Reflector.on("android.app.AppOpsManager");

    public static Reflector.FieldWrapper<IInterface> mService = REF.field("mService");
}
