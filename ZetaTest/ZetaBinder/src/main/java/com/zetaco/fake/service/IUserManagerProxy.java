package com.zetaco.fake.service;

import android.content.Context;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.android.content.pm.UserInfo;
import org.android.os.IUserManager;
import org.android.os.ServiceManager;
import com.zetaco.ZetaBCore;
import com.zetaco.app.BActivityThread;
import com.zetaco.fake.hook.BinderInvocationStub;
import com.zetaco.fake.hook.MethodHook;
import com.zetaco.fake.hook.ProxyMethod;
import com.zetaco.fake.service.base.ValueMethodProxy;

public class IUserManagerProxy extends BinderInvocationStub {
    public IUserManagerProxy() {
        super(ServiceManager.getService.call(Context.USER_SERVICE));
    }

    @Override
    protected Object getWho() {
        return IUserManager.Stub.asInterface.call(ServiceManager.getService.call(Context.USER_SERVICE));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService(Context.USER_SERVICE);
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @Override
    protected void onBindMethod() {
        addMethodHook(new ValueMethodProxy("getProfileParent",null));
        addMethodHook(new ValueMethodProxy("getUserIcon",null));
        addMethodHook(new ValueMethodProxy("getDefaultGuestRestrictions",null));
        addMethodHook(new ValueMethodProxy("setDefaultGuestRestrictions",null));
        addMethodHook(new ValueMethodProxy("removeRestrictions",null));
        addMethodHook(new ValueMethodProxy("createUser",null));
        addMethodHook(new ValueMethodProxy("createProfileForUser",null));
    }

    @ProxyMethod("getApplicationRestrictions")
    public static class GetApplicationRestrictions extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            args[0] = ZetaBCore.getHostPkg();
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("getProfileParent")
    public static class GetProfileParent extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return UserInfo._new.newInstance(BActivityThread.getUserId(), "ZetaB", UserInfo.FLAG_PRIMARY);
        }
    }

    @ProxyMethod("getUsers")
    public static class GetUsers extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return new ArrayList<>();
        }
    }
}
