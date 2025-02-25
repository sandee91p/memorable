package org.android.app;

import android.os.IBinder;
import android.os.IInterface;

import org.Reflector;

public class IActivityTaskManager {
    public static class Stub {
        public static final Reflector REF = Reflector.on("android.app.IActivityTaskManager$Stub");
        public static Reflector.StaticMethodWrapper<IInterface> asInterface = REF.staticMethod("asInterface", IBinder.class);
    }
}
