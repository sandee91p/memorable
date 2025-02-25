package org.android.view;

import android.os.IBinder;
import android.os.IInterface;

import org.Reflector;

public class IWindowManager {
    public static class Stub {
        public static final Reflector REF = Reflector.on("android.view.IWindowManager$Stub");
        public static Reflector.StaticMethodWrapper<IInterface> asInterface = REF.staticMethod("asInterface", IBinder.class);
    }
}
