package com.zetaco.fake.service;

import android.content.Context;
import android.os.IInterface;

import java.lang.reflect.Method;
import java.util.Objects;

import org.android.os.ServiceManager;
import org.android.view.IWindowManager;
import org.android.view.WindowManagerGlobal;
import com.zetaco.fake.hook.BinderInvocationStub;
import com.zetaco.fake.hook.MethodHook;
import com.zetaco.fake.hook.ProxyMethod;

public class IWindowManagerProxy extends BinderInvocationStub {
    public static final String TAG = "WindowManagerStub";

    public IWindowManagerProxy() {
        super(ServiceManager.getService.call(Context.WINDOW_SERVICE));
    }

    @Override
    protected Object getWho() {
        return IWindowManager.Stub.asInterface.call(ServiceManager.getService.call(Context.WINDOW_SERVICE));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService(Context.WINDOW_SERVICE);
        WindowManagerGlobal.sWindowManagerService.set(null);
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }


    @ProxyMethod("openSession")
    public static class OpenSession extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            IInterface session = (IInterface) method.invoke(who, args);
            IWindowSessionProxy IWindowSessionProxy = new IWindowSessionProxy(Objects.requireNonNull(session));
            IWindowSessionProxy.injectHook();
            return IWindowSessionProxy.getProxyInvocation();
        }
    }
}
