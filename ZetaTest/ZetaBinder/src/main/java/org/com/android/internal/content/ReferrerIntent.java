package org.com.android.internal.content;

import android.content.Intent;

import org.Reflector;

public class ReferrerIntent {
    public static final Reflector REF = Reflector.on("com.android.internal.content.ReferrerIntent");

    public static Reflector.ConstructorWrapper<Intent> _new = REF.constructor(Intent.class, String.class);
}
