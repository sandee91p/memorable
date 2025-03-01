package com.zetaco.fake.service.context.providers;

import android.os.IInterface;

import java.lang.reflect.Method;

import org.android.content.AttributionSource;
import com.zetaco.ZetaBCore;
import com.zetaco.fake.hook.ClassInvocationStub;
import com.zetaco.utils.compat.ContextCompat;

public class SystemProviderStub extends ClassInvocationStub implements BContentProvider {
    public static final String TAG = "SystemProviderStub";
    private IInterface mBase;
    private String mAppPkg;

    @Override
    public IInterface wrapper(IInterface contentProviderProxy, String appPkg) {
        mBase = contentProviderProxy;
        mAppPkg = appPkg;

        injectHook();
        return (IInterface) getProxyInvocation();
    }


    @Override
    protected Object getWho() {
        return mBase;
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) { }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("asBinder".equals(method.getName())) {
            return method.invoke(mBase, args);
        }

        if (args != null && args.length > 0) {
            Object arg = args[0];
            if (arg instanceof String) {
                args[0] = mAppPkg;
            } else if (arg.getClass().getName().equals(AttributionSource.REF.getClazz().getName())) {
                ContextCompat.fixAttributionSourceState(arg, ZetaBCore.getHostUid());
            }
        }
        return method.invoke(mBase, args);
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }
}
