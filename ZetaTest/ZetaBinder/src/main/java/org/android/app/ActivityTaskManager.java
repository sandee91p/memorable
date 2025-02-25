package org.android.app;

import org.Reflector;

public class ActivityTaskManager {
    public static final Reflector REF = Reflector.on("android.app.ActivityTaskManager");

    public static Reflector.FieldWrapper<Object> IActivityTaskManagerSingleton = REF.field("IActivityTaskManagerSingleton");
}
