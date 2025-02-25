package org.android.os.mount;

import android.os.IBinder;
import android.os.IInterface;

import org.Reflector;

public class IMountService {
    public static class Stub {
        public static final Reflector REF = Reflector.on("android.os.storage.IMountService$Stub");
        public static Reflector.StaticMethodWrapper<IInterface> asInterface = REF.staticMethod("asInterface", IBinder.class);
    }
}
