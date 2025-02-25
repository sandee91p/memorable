package com.zetaco.fake.service;

import android.app.AppOpsManager;
import android.content.Context;

import java.lang.reflect.Method;

import org.android.os.ServiceManager;
import org.com.android.internal.app.IAppOpsService;
import com.zetaco.ZetaBCore;
import com.zetaco.fake.hook.BinderInvocationStub;
import com.zetaco.fake.hook.MethodHook;
import com.zetaco.fake.hook.ProxyMethod;
import com.zetaco.utils.MethodParameterUtils;

public class IAppOpsManagerProxy extends BinderInvocationStub {
    public IAppOpsManagerProxy() {
        super(ServiceManager.getService.call(Context.APP_OPS_SERVICE));
    }

    @Override
    protected Object getWho() {
        return IAppOpsService.Stub.asInterface.call(ServiceManager.getService.call(Context.APP_OPS_SERVICE));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        if (org.android.app.AppOpsManager.mService != null) {
            AppOpsManager appOpsManager = (AppOpsManager) ZetaBCore.getContext().getSystemService(Context.APP_OPS_SERVICE);
            try {
                org.android.app.AppOpsManager.mService.set(appOpsManager, getProxyInvocation());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        replaceSystemService(Context.APP_OPS_SERVICE);

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


    @ProxyMethod("noteProxyOperation")
    public static class NoteProxyOperation extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return AppOpsManager.MODE_ALLOWED;
        }
    }

    @ProxyMethod("checkPackage")
    public static class CheckPackage extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            // TODO
            return AppOpsManager.MODE_ALLOWED;
        }
    }

    @ProxyMethod("checkOperation")
    public static class CheckOperation extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceLastUid(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("noteOperation")
    public static class NoteOperation extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return method.invoke(who, args);
        }
    }
}
