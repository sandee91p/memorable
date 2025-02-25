package com.zetaco.utils.compat;

import android.content.Context;
import android.content.ContextWrapper;

import org.android.app.ContextImpl;
import org.android.app.ContextImplKitkat;
import org.android.content.AttributionSource;
import org.android.content.AttributionSourceState;
import org.android.content.ContentResolver;
import com.zetaco.ZetaBCore;
import com.zetaco.app.BActivityThread;

public class ContextCompat {
    public static final String TAG = "ContextCompat";

    public static void fixAttributionSourceState(Object obj, int uid) {
        Object mAttributionSourceState;
        if (obj != null && AttributionSource.mAttributionSourceState != null) {
            mAttributionSourceState = AttributionSource.mAttributionSourceState.get(obj);

            AttributionSourceState.packageName.set(mAttributionSourceState, ZetaBCore.getHostPkg());
            AttributionSourceState.uid.set(mAttributionSourceState, uid);
            fixAttributionSourceState(AttributionSource.getNext.call(obj), uid);
        }
    }

    public static void fix(Context context) {
        try {
            int deep = 0;
            while (context instanceof ContextWrapper) {
                context = ((ContextWrapper) context).getBaseContext();
                deep++;
                if (deep >= 10) {
                    return;
                }
            }

            ContextImpl.mPackageManager.set(context, null);
            try {
                context.getPackageManager();
            } catch (Throwable e) {
                e.printStackTrace();
            }

            ContextImpl.mBasePackageName.set(context, ZetaBCore.getHostPkg());
            ContextImplKitkat.mOpPackageName.set(context, ZetaBCore.getHostPkg());
            ContentResolver.mPackageName.set(context.getContentResolver(), ZetaBCore.getHostPkg());

            if (BuildCompat.isS()) {
                fixAttributionSourceState(ContextImpl.getAttributionSource.call(context), BActivityThread.getBUid());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
