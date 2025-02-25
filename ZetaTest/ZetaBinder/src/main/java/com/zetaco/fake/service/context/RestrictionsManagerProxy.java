package com.zetaco.fake.service.context;

import android.content.Context;

import java.lang.reflect.Method;

import org.android.content.IRestrictionsManager;
import org.android.os.ServiceManager;
import com.zetaco.ZetaBCore;
import com.zetaco.fake.hook.BinderInvocationStub;
import com.zetaco.fake.hook.MethodHook;
import com.zetaco.fake.hook.ProxyMethod;

public class RestrictionsManagerProxy extends BinderInvocationStub {
    public RestrictionsManagerProxy() {
        super(ServiceManager.getService.call(Context.RESTRICTIONS_SERVICE));
    }

    @Override
    protected Object getWho() {
        return IRestrictionsManager.Stub.asInterface.call(ServiceManager.getService.call(Context.RESTRICTIONS_SERVICE));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService(Context.RESTRICTIONS_SERVICE);
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @ProxyMethod("getApplicationRestrictions")
    public static class GetApplicationRestrictions extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            args[0] = ZetaBCore.getHostPkg();
            return method.invoke(who, args);
        }
    }
}
