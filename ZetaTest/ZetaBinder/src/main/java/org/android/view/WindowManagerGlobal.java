package org.android.view;

import android.os.IInterface;

import org.Reflector;

public class WindowManagerGlobal {
    public static final Reflector REF = Reflector.on("android.view.WindowManagerGlobal");
    
    public static Reflector.FieldWrapper<IInterface> sWindowManagerService = REF.field("sWindowManagerService");
}
