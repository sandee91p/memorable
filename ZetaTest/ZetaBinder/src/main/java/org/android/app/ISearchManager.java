package org.android.app;

import android.os.IBinder;
import android.os.IInterface;

import org.Reflector;

/**
 * @author Findger
 * @function
 * @date :2023/10/8 19:57
 **/
public class ISearchManager {
    public static final Reflector REF = Reflector.on("android.app.ISearchManager");

    public static class Stub {
        public static final Reflector REF = Reflector.on("android.app.ISearchManager$Stub");
        public static Reflector.StaticMethodWrapper<IInterface> asInterface = REF.staticMethod("asInterface", IBinder.class);
    }
}
