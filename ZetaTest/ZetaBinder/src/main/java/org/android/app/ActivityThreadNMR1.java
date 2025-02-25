package org.android.app;

import android.os.IBinder;

import java.util.List;

import org.Reflector;

public class ActivityThreadNMR1 {
    public static final Reflector REF = Reflector.on("android.app.ActivityThread");

    public static Reflector.MethodWrapper<Void> performNewIntents = REF.method("performNewIntents", IBinder.class, List.class, boolean.class);
}
