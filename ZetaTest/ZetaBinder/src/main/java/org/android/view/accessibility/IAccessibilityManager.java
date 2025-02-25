package org.android.view.accessibility;

import android.os.IBinder;
import android.os.IInterface;

import org.Reflector;

public class IAccessibilityManager {
    public static class Stub {
        public static final Reflector REF = Reflector.on("android.view.accessibility.IAccessibilityManager$Stub");
        public static Reflector.StaticMethodWrapper<IInterface> asInterface = REF.staticMethod("asInterface", IBinder.class);
    }
}
