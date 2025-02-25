package com.zetaco.fake.service;

import android.app.job.JobInfo;
import android.content.Context;

import java.lang.reflect.Method;

import org.android.app.job.IJobScheduler;
import org.android.os.ServiceManager;
import com.zetaco.ZetaBCore;
import com.zetaco.app.BActivityThread;
import com.zetaco.fake.hook.BinderInvocationStub;
import com.zetaco.fake.hook.MethodHook;
import com.zetaco.fake.hook.ProxyMethod;

public class IJobServiceProxy extends BinderInvocationStub {
    public static final String TAG = "JobServiceStub";

    public IJobServiceProxy() {
        super(ServiceManager.getService.call(Context.JOB_SCHEDULER_SERVICE));
    }

    @Override
    protected Object getWho() {
        return IJobScheduler.Stub.asInterface.call(ServiceManager.getService.call(Context.JOB_SCHEDULER_SERVICE));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService(Context.JOB_SCHEDULER_SERVICE);

    }


    @ProxyMethod("schedule")
    public static class Schedule extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            JobInfo jobInfo = (JobInfo) args[0];
            JobInfo proxyJobInfo = ZetaBCore.getBJobManager()
                    .schedule(jobInfo);
            args[0] = proxyJobInfo;
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("cancel")
    public static class Cancel extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            args[0] = ZetaBCore.getBJobManager()
                    .cancel(BActivityThread.getAppConfig().processName, (Integer) args[0]);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("cancelAll")
    public static class CancelAll extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            ZetaBCore.getBJobManager().cancelAll(BActivityThread.getAppConfig().processName);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("enqueue")
    public static class Enqueue extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            JobInfo jobInfo = (JobInfo) args[0];
            JobInfo proxyJobInfo = ZetaBCore.getBJobManager()
                    .schedule(jobInfo);
            args[0] = proxyJobInfo;
            return method.invoke(who, args);
        }
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }
}
