package org.android.os;

import android.os.Handler.Callback;

import org.Reflector;

public class Handler {
    public static final Reflector REF = Reflector.on("android.os.Handler");

    public static Reflector.FieldWrapper<Callback> mCallback = REF.field("mCallback");
}
