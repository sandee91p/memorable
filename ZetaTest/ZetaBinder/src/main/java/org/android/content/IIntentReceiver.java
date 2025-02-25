package org.android.content;

import android.content.Intent;
import android.os.Bundle;

import org.Reflector;

public class IIntentReceiver {
    public static final Reflector REF = Reflector.on("android.content.IIntentReceiver");

    public static Reflector.MethodWrapper<Void> performReceive = REF.method("performReceive", Intent.class, int.class, String.class, Bundle.class, boolean.class, boolean.class, int.class);
}
