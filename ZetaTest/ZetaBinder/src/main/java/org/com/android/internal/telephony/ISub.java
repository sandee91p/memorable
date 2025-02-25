package org.com.android.internal.telephony;

import android.os.IBinder;
import android.os.IInterface;

import org.Reflector;

public class ISub {
    public static class Stub {
        public static final Reflector REF = Reflector.on("com.android.internal.telephony.ISub$Stub");
        public static Reflector.StaticMethodWrapper<IInterface> asInterface = REF.staticMethod("asInterface", IBinder.class);
    }
}
