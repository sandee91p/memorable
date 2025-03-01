package com.zetaco.fake.service;

import android.content.Context;

import org.android.os.ServiceManager;
import org.android.view.IGraphicsStats;
import com.zetaco.fake.hook.BinderInvocationStub;
import com.zetaco.fake.service.base.PkgMethodProxy;

public class IFingerprintManagerProxy extends BinderInvocationStub {
    public IFingerprintManagerProxy() {
        super(ServiceManager.getService.call(Context.FINGERPRINT_SERVICE));
    }

    @Override
    protected Object getWho() {
        return IGraphicsStats.Stub.asInterface.call(ServiceManager.getService.call(Context.FINGERPRINT_SERVICE));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService(Context.FINGERPRINT_SERVICE);
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @Override
    protected void onBindMethod() {
        super.onBindMethod();
        addMethodHook(new PkgMethodProxy("isHardwareDetected"));
        addMethodHook(new PkgMethodProxy("hasEnrolledFingerprints"));
        addMethodHook(new PkgMethodProxy("authenticate"));
        addMethodHook(new PkgMethodProxy("cancelAuthentication"));
        addMethodHook(new PkgMethodProxy("getEnrolledFingerprints"));
        addMethodHook(new PkgMethodProxy("getAuthenticatorId"));
    }
}
