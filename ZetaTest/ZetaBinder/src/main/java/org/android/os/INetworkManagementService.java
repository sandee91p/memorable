package org.android.os;

import android.os.IBinder;
import android.os.IInterface;

import org.Reflector;

public class INetworkManagementService {
    public static class Stub {
        public static final Reflector REF = Reflector.on("android.os.INetworkManagementService$Stub");
        public static Reflector.StaticMethodWrapper<IInterface> asInterface = REF.staticMethod("asInterface", IBinder.class);
    }
}
