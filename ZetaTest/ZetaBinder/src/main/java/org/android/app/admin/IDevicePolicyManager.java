package org.android.app.admin;

import android.os.IBinder;
import android.os.IInterface;

import org.Reflector;

public class IDevicePolicyManager {
    public static class Stub {
        public static final Reflector REF = Reflector.on("android.app.admin.IDevicePolicyManager$Stub");
        public static Reflector.StaticMethodWrapper<IInterface> asInterface = REF.staticMethod("asInterface", IBinder.class);
    }
}
