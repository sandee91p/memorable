package org.android.content;

import org.Reflector;

public class AttributionSourceState {
    public static final Reflector REF = Reflector.on("android.content.AttributionSourceState");

    public static Reflector.FieldWrapper<String> packageName = REF.field("packageName");
    public static Reflector.FieldWrapper<Integer> uid = REF.field("uid");
}
