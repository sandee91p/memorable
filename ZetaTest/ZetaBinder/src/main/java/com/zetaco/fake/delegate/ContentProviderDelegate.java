package com.zetaco.fake.delegate;

import android.net.Uri;
import android.os.Build;
import android.os.IInterface;
import android.util.ArrayMap;

import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;

import org.android.app.ActivityThread;
import org.android.app.IActivityManager;
import org.android.content.ContentProviderHolderOreo;
import org.android.providers.Settings;
import com.zetaco.ZetaBCore;
import com.zetaco.fake.service.context.providers.ContentProviderStub;
import com.zetaco.fake.service.context.providers.SystemProviderStub;
import com.zetaco.utils.compat.BuildCompat;

public class ContentProviderDelegate {
    public static final String TAG = "ContentProviderDelegate";
    private static final Set<String> sInjected = new HashSet<>();

    public static void update(Object holder, String auth) {
        IInterface iInterface;
        if (BuildCompat.isOreo()) {
            iInterface = ContentProviderHolderOreo.provider.get(holder);
        } else {
            iInterface = IActivityManager.ContentProviderHolder.provider.get(holder);
        }

        if (iInterface instanceof Proxy) {
            return;
        }

        IInterface bContentProvider;
        switch (auth) {
            case "media":
            case "telephony":
            case "settings":
                bContentProvider = new SystemProviderStub().wrapper(iInterface, ZetaBCore.getHostPkg());
                break;
            default:
                bContentProvider = new ContentProviderStub().wrapper(iInterface, ZetaBCore.getHostPkg());
                break;
        }

        if (BuildCompat.isOreo()) {
            ContentProviderHolderOreo.provider.set(holder, bContentProvider);
        } else {
            IActivityManager.ContentProviderHolder.provider.set(holder, bContentProvider);
        }
    }

    public static void init() {
        clearSettingProvider();

        ZetaBCore.getContext().getContentResolver().call(Uri.parse("content://settings"), "", null, null);
        Object activityThread = ZetaBCore.mainThread();
        ArrayMap<Object, Object> map = (ArrayMap<Object, Object>) ActivityThread.mProviderMap.get(activityThread);

        for (Object value : map.values()) {
            String[] mNames = ActivityThread.ProviderClientRecordP.mNames.get(value);
            if (mNames == null || mNames.length <= 0) {
                continue;
            }

            String providerName = mNames[0];
            if (!sInjected.contains(providerName)) {
                sInjected.add(providerName);
                IInterface iInterface = ActivityThread.ProviderClientRecordP.mProvider.get(value);
                ActivityThread.ProviderClientRecordP.mProvider.set(value, new ContentProviderStub().wrapper(iInterface, ZetaBCore.getHostPkg()));
                ActivityThread.ProviderClientRecordP.mNames.set(value, new String[]{providerName});
            }
        }
    }

    public static void clearSettingProvider() {
        Object cache;
        cache = Settings.System.sNameValueCache.get();
        if (cache != null) {
            clearContentProvider(cache);
        }

        cache = Settings.Secure.sNameValueCache.get();
        if (cache != null) {
            clearContentProvider(cache);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            cache = Settings.Global.sNameValueCache.get();
            if (cache != null) {
                clearContentProvider(cache);
            }
        }
    }

    private static void clearContentProvider(Object cache) {
        if (BuildCompat.isOreo()) {
            Object holder = Settings.NameValueCacheOreo.mProviderHolder.get(cache);
            if (holder != null) {
                Settings.ContentProviderHolder.mContentProvider.set(holder, null);
            }
        } else {
            Settings.NameValueCache.mContentProvider.set(cache, null);
        }
    }
}
