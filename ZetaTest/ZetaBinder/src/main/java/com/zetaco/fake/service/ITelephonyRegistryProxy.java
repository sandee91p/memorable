package com.zetaco.fake.service;

import java.lang.reflect.Method;

import org.android.os.ServiceManager;
import org.com.android.internal.telephony.ITelephonyRegistry;
import com.zetaco.fake.hook.BinderInvocationStub;
import com.zetaco.fake.hook.MethodHook;
import com.zetaco.fake.hook.ProxyMethod;
import com.zetaco.utils.MethodParameterUtils;

public class ITelephonyRegistryProxy extends BinderInvocationStub {
    public ITelephonyRegistryProxy() {
        super(ServiceManager.getService.call("telephony.registry"));
    }

    @Override
    protected Object getWho() {
        return ITelephonyRegistry.Stub.asInterface.call(ServiceManager.getService.call("telephony.registry"));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService("telephony.registry");
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @ProxyMethod("listenForSubscriber")
    public static class ListenForSubscriber extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceFirstAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("listen")
    public static class Listen extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceFirstAppPkg(args);
            return method.invoke(who, args);
        }
    }
}
