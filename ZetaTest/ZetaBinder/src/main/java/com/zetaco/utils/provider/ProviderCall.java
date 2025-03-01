package com.zetaco.utils.provider;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.zetaco.ZetaBCore;
import com.zetaco.utils.compat.ContentProviderCompat;

public class ProviderCall {
    public static Bundle callSafely(String authority, String methodName, String arg, Bundle bundle) {
        try {
            return call(authority, ZetaBCore.getContext(), methodName, arg, bundle, 5);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bundle call(String authority, Context context, String method, String arg, Bundle bundle, int retryCount) throws IllegalAccessException {
        Uri uri = Uri.parse("content://" + authority);
        return ContentProviderCompat.call(context, uri, method, arg, bundle, retryCount);
    }
}
