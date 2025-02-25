package org.android.app;

import android.os.IBinder;

import java.util.List;

import org.Reflector;

public class ActivityThreadQ {
    public static final Reflector REF = Reflector.on("android.app.ActivityThread");

    public static Reflector.MethodWrapper<Void> handleNewIntent = REF.method("handleNewIntent", IBinder.class, List.class);
}
