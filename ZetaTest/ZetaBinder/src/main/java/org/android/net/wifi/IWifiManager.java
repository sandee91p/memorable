package org.android.net.wifi;

import android.os.IBinder;
import android.os.IInterface;

import org.Reflector;

public class IWifiManager {
    public static class Stub {
        public static final Reflector REF = Reflector.on("android.net.wifi.IWifiManager$Stub");
        public static Reflector.StaticMethodWrapper<IInterface> asInterface = REF.staticMethod("asInterface", IBinder.class);
    }
}
