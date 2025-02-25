package com.zetaco.fake.service;

import java.lang.reflect.Method;

import org.android.os.IDeviceIdentifiersPolicyService;
import org.android.os.ServiceManager;
import com.zetaco.ZetaBCore;
import com.zetaco.fake.hook.BinderInvocationStub;
import com.zetaco.fake.hook.MethodHook;
import com.zetaco.fake.hook.ProxyMethod;
import com.zetaco.fake.service.base.PkgMethodProxy;
import com.zetaco.utils.Md5Utils;

public class IDeviceIdentifiersPolicyProxy extends BinderInvocationStub {
    public IDeviceIdentifiersPolicyProxy() {
        super(ServiceManager.getService.call("device_identifiers"));
    }

    @Override
    protected Object getWho() {
        return IDeviceIdentifiersPolicyService.Stub.asInterface.call(ServiceManager.getService.call("device_identifiers"));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService("device_identifiers");

    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @Override
    protected void onBindMethod() {
        addMethodHook(new PkgMethodProxy("getSerialForPackage"));
    }

    @ProxyMethod("getSerialForPackage")
    public static class GetSerialForPackage extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return Md5Utils.md5(ZetaBCore.getHostPkg());
        }
    }
}
