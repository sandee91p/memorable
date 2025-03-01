package org.android.location.provider;

import org.Reflector;

public class ProviderProperties {
    public static final Reflector REF = Reflector.on("android.location.provider.ProviderProperties");

    public static Reflector.FieldWrapper<Boolean> mHasNetworkRequirement = REF.field("mHasNetworkRequirement");
    public static Reflector.FieldWrapper<Boolean> mHasCellRequirement = REF.field("mHasCellRequirement");
}
