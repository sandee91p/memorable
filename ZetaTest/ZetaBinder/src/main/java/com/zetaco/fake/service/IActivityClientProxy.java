package com.zetaco.fake.service;

import android.app.ActivityManager;
import android.os.IBinder;

import java.lang.reflect.Method;

import org.android.app.ActivityClient;
import org.android.util.Singleton;
import com.zetaco.fake.frameworks.BActivityManager;
import com.zetaco.fake.hook.ClassInvocationStub;
import com.zetaco.fake.hook.MethodHook;
import com.zetaco.fake.hook.ProxyMethod;
import com.zetaco.utils.compat.TaskDescriptionCompat;

public class IActivityClientProxy extends ClassInvocationStub {
    public static final String TAG = "IActivityClientProxy";
    private final Object who;

    public IActivityClientProxy(Object who) {
        this.who = who;
    }

    @Override
    protected Object getWho() {
        if (who != null) {
            return who;
        }

        Object instance = ActivityClient.getInstance.call();
        Object singleton = ActivityClient.INTERFACE_SINGLETON.get(instance);
        return Singleton.get.call(singleton);
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        Object instance = ActivityClient.getInstance.call();
        Object singleton = ActivityClient.INTERFACE_SINGLETON.get(instance);
        Singleton.mInstance.set(singleton, proxyInvocation);

    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @Override
    public void onlyProxy(boolean only) {
        super.onlyProxy(only);
    }

    @ProxyMethod("finishActivity")
    public static class FinishActivity extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            IBinder token = (IBinder) args[0];
            BActivityManager.get().onFinishActivity(token);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("activityResumed")
    public static class ActivityResumed extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            IBinder token = (IBinder) args[0];
            BActivityManager.get().onActivityResumed(token);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("activityDestroyed")
    public static class ActivityDestroyed extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            IBinder token = (IBinder) args[0];
            BActivityManager.get().onActivityDestroyed(token);
            return method.invoke(who, args);
        }
    }

    // for >= Android 12
    @ProxyMethod("setTaskDescription")
    public static class SetTaskDescription extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            ActivityManager.TaskDescription td = (ActivityManager.TaskDescription) args[1];
            args[1] = TaskDescriptionCompat.fix(td);
            return method.invoke(who, args);
        }
    }
}
