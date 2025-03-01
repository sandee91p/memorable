package com.zetaco.fake.service;

import android.content.Context;

import java.lang.reflect.Method;

import org.android.app.usage.IStorageStatsManager;
import org.android.os.ServiceManager;
import com.zetaco.fake.hook.BinderInvocationStub;
import com.zetaco.utils.MethodParameterUtils;

public class IStorageStatsManagerProxy extends BinderInvocationStub {
    public IStorageStatsManagerProxy() {
        super(ServiceManager.getService.call(Context.STORAGE_STATS_SERVICE));
    }

    @Override
    protected Object getWho() {
        return IStorageStatsManager.Stub.asInterface.call(ServiceManager.getService.call(Context.STORAGE_STATS_SERVICE));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService(Context.STORAGE_STATS_SERVICE);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodParameterUtils.replaceFirstAppPkg(args);
        MethodParameterUtils.replaceLastUid(args);
        return super.invoke(proxy, method, args);
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }
}
