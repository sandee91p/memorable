package com.zetaco.fake.service;

import android.app.ActivityManager;

import java.lang.reflect.Method;

import org.android.app.ActivityTaskManager;
import org.android.app.IActivityTaskManager;
import org.android.os.ServiceManager;
import org.android.util.Singleton;
import com.zetaco.fake.hook.BinderInvocationStub;
import com.zetaco.fake.hook.MethodHook;
import com.zetaco.fake.hook.ProxyMethod;
import com.zetaco.fake.hook.ScanClass;
import com.zetaco.utils.compat.TaskDescriptionCompat;

@ScanClass(ActivityManagerCommonProxy.class)
public class IActivityTaskManagerProxy extends BinderInvocationStub {
    public static final String TAG = "ActivityTaskManager";

    public IActivityTaskManagerProxy() {
        super(ServiceManager.getService.call("activity_task"));
    }

    @Override
    protected Object getWho() {
        return IActivityTaskManager.Stub.asInterface.call(ServiceManager.getService.call("activity_task"));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService("activity_task");

        Object o = ActivityTaskManager.IActivityTaskManagerSingleton.get();
        Singleton.mInstance.set(o, IActivityTaskManager.Stub.asInterface.call(this));

    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @Override
    protected void onBindMethod() {
        addMethodHook(new SetTaskDescription());
    }

    // for >= Android 10 && < Android 12
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
