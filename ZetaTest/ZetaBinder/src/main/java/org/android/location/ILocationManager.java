package org.android.location;

import android.os.IBinder;
import android.os.IInterface;

import org.Reflector;

public class ILocationManager {
    public static class Stub {
        public static final Reflector REF = Reflector.on("android.location.ILocationManager$Stub");
        public static Reflector.StaticMethodWrapper<IInterface> asInterface = REF.staticMethod("asInterface", IBinder.class);
    }
}
