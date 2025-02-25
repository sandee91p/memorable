package org.android.content;

import android.os.IInterface;

import org.Reflector;

public class ContentProviderHolderOreo {
    public static final Reflector REF = Reflector.on("android.app.ContentProviderHolder");

    public static Reflector.FieldWrapper<IInterface> provider = REF.field("provider");
}
