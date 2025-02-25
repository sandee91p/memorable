package com.zetaco.fake.service;

import org.android.os.ServiceManager;
import org.oem.vivo.ISuperResolutionManager;
import com.zetaco.fake.hook.BinderInvocationStub;
import com.zetaco.fake.service.base.PkgMethodProxy;

/**
 * @author Findger
 * @function
 * @date :2023/10/8 20:26
 **/
public class ISuperResolutionManagerProxy extends BinderInvocationStub {

    public ISuperResolutionManagerProxy() {
        super(ServiceManager.getService.call("SuperResolutionManager"));
    }

    @Override
    protected Object getWho() {
        return ISuperResolutionManager.Stub.asInterface.call(ServiceManager.getService.call("SuperResolutionManager"));
    }



    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService("SuperResolutionManager");
    }


    @Override
    public boolean isBadEnv() {
        return false;
    }

    @Override
    protected void onBindMethod() {
        addMethodHook(new PkgMethodProxy("registerPackageSettingStateChangeListener"));
        addMethodHook(new PkgMethodProxy("unRegisterPackageSettingStateChangeListener"));
        addMethodHook(new PkgMethodProxy("registerSuperResolutionStateChange"));
        addMethodHook(new PkgMethodProxy("unRegisterSuperResolutionStateChange"));
        addMethodHook(new PkgMethodProxy("getPackageSettingState"));
        addMethodHook(new PkgMethodProxy("putPackageSettingState"));
    }
}
