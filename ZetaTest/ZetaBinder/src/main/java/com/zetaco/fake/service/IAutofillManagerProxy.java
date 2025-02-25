package com.zetaco.fake.service;

import android.content.ComponentName;

import java.lang.reflect.Method;

import org.android.os.ServiceManager;
import org.android.view.IAutoFillManager;
import com.zetaco.ZetaBCore;
import com.zetaco.app.BActivityThread;
import com.zetaco.fake.hook.BinderInvocationStub;
import com.zetaco.fake.hook.MethodHook;
import com.zetaco.fake.hook.ProxyMethod;
import com.zetaco.proxy.ProxyManifest;

public class IAutofillManagerProxy extends BinderInvocationStub {
    public static final String TAG = "AutofillManagerStub";

    public IAutofillManagerProxy() {
        super(ServiceManager.getService.call("autofill"));
    }

    @Override
    protected Object getWho() {
        return IAutoFillManager.Stub.asInterface.call(ServiceManager.getService.call("autofill"));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService("autofill");

    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @Override
    protected void onBindMethod() {
        addMethodHook(new StartSession());
    }

    @ProxyMethod("startSession")
    public static class StartSession extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    if (args[i] == null) {
                        continue;
                    }

                    if (args[i] instanceof ComponentName) {
                        args[i] = new ComponentName(ZetaBCore.getHostPkg(), ProxyManifest.getProxyActivity(BActivityThread.getAppPid()));
                    }
                }
            }
            return method.invoke(who, args);
        }
    }
}
