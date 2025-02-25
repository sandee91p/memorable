package org.android.app;

import org.Reflector;

public class NotificationChannel {
    public static final Reflector REF = Reflector.on("android.app.NotificationChannel");

    public static Reflector.FieldWrapper<String> mId = REF.field("mId");
}
