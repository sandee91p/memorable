package com.zetaco.fake.service;

import android.content.Context;
import android.os.Build;
import android.os.WorkSource;

import java.lang.reflect.Method;

import org.android.app.IAlarmManager;
import org.android.os.ServiceManager;
import com.zetaco.ZetaBCore;
import com.zetaco.fake.hook.BinderInvocationStub;
import com.zetaco.fake.hook.MethodHook;
import com.zetaco.fake.hook.ProxyMethod;
import com.zetaco.fake.service.base.ValueMethodProxy;
import com.zetaco.utils.ArrayUtils;

public class IAlarmManagerProxy extends BinderInvocationStub {
    public IAlarmManagerProxy() {
        super(ServiceManager.getService.call(Context.ALARM_SERVICE));
    }

    @Override
    protected Object getWho() {
        return IAlarmManager.Stub.asInterface.call(ServiceManager.getService.call(Context.ALARM_SERVICE));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService(Context.ALARM_SERVICE);
    }

    @Override
    protected void onBindMethod() {
        super.onBindMethod();
        //addMethodHook(new ValueMethodProxy("set", 0));
        addMethodHook(new ValueMethodProxy("setTimeZone",null));
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @ProxyMethod("set")
    public static class Set extends MethodHook {

        @Override
        protected String getMethodName() {
            return "set";
        }

        @Override
        protected Object beforeHook(Object who, Method method, Object[] args) throws Throwable {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && args[0] instanceof String) {
                args[0] = ZetaBCore.getHostPkg();
            }
            int index = ArrayUtils.indexOfFirst(args, WorkSource.class);
            if (index >= 0) {
                args[index] = null;
            }
            return true;
        }

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            try {
                return method.invoke(who, method, args);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    @ProxyMethod("setTime")
    public static class SetTime extends MethodHook {

        @Override
        protected String getMethodName() {
            return "setTime";
        }

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return false;
            }
            return null;
        }
    }
}
