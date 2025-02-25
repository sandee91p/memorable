package com.zetaco.fake.service;

import android.content.Context;

import org.android.os.IPowerManager;
import org.android.os.ServiceManager;
import com.zetaco.fake.hook.BinderInvocationStub;
import com.zetaco.fake.service.base.ValueMethodProxy;

public class IPowerManagerProxy extends BinderInvocationStub {
    public IPowerManagerProxy() {
        super(ServiceManager.getService.call(Context.POWER_SERVICE));
    }

    @Override
    protected Object getWho() {
        return IPowerManager.Stub.asInterface.call(ServiceManager.getService.call(Context.POWER_SERVICE));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService(Context.POWER_SERVICE);
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @Override
    protected void onBindMethod() {
        super.onBindMethod();
        addMethodHook(new ValueMethodProxy("acquireWakeLock", 0));
        addMethodHook(new ValueMethodProxy("acquireWakeLockWithUid", 0));
        addMethodHook(new ValueMethodProxy("releaseWakeLock", 0));
        addMethodHook(new ValueMethodProxy("updateWakeLockWorkSource", 0));
        addMethodHook(new ValueMethodProxy("isWakeLockLevelSupported", true));
        addMethodHook(new ValueMethodProxy("reboot", null));
        addMethodHook(new ValueMethodProxy("rebootSafeMode", null));
        addMethodHook(new ValueMethodProxy("shutdown", null));
    }
}
