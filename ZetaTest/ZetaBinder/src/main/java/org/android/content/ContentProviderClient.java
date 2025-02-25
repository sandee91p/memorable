package org.android.content;

import android.os.IInterface;

import org.Reflector;

public class ContentProviderClient {
    public static final Reflector REF = Reflector.on("android.content.ContentProviderClient");

    public static Reflector.FieldWrapper<IInterface> mContentProvider = REF.field("mContentProvider");
}
