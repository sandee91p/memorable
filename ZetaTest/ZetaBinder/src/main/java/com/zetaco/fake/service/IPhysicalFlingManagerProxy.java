package com.zetaco.fake.service;

import org.android.os.ServiceManager;
import org.oem.vivo.IPhysicalFlingManager;
import com.zetaco.fake.hook.BinderInvocationStub;
import com.zetaco.fake.service.base.PkgMethodProxy;

/**
 * @author Findger
 * @function
 * @date :2023/10/8 20:11
 **/
public class IPhysicalFlingManagerProxy extends BinderInvocationStub {
    public IPhysicalFlingManagerProxy() {
        super(ServiceManager.getService.call("physical_fling_service"));
    }

    @Override
    protected Object getWho() {
        return IPhysicalFlingManager.Stub.asInterface.call(ServiceManager.getService.call("physical_fling_service"));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService("physical_fling_service");
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @Override
    protected void onBindMethod() {
        addMethodHook(new PkgMethodProxy("isSupportPhysicalFling"));
    }
}
