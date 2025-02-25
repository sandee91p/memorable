package com.zetaco.fake.service.libcore;

import android.os.Process;

import java.lang.reflect.Method;
import java.util.Objects;

import org.Reflector;
import org.libcore.io.Libcore;
import com.zetaco.ZetaBCore;
import com.zetaco.app.BActivityThread;
import com.zetaco.fake.hook.ClassInvocationStub;
import com.zetaco.fake.hook.MethodHook;
import com.zetaco.fake.hook.ProxyMethod;
import com.zetaco.fake.hook.ProxyMethods;

public class OsProxy extends ClassInvocationStub {
    public static final String TAG = "OsProxy";
    private final Object mBase;

    public OsProxy() {
        this.mBase = Libcore.os.get();
    }

    @Override
    protected Object getWho() {
        return mBase;
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        Libcore.os.set(proxyInvocation);
    }

    @Override
    public boolean isBadEnv() {
        return Libcore.os.get() != getProxyInvocation();
    }

    @ProxyMethod("getuid")
    public static class GetUID extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            int callUid = (int) method.invoke(who, args);
            return getFakeUid(callUid);
        }
    }

    @ProxyMethods({"lstat", "stat"})
    public static class Stat extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            Object invoke;
            try {
                invoke = method.invoke(who, args);
            } catch (Throwable e) {
                throw Objects.requireNonNull(e.getCause());
            }

            Reflector.on("android.system.StructStat")
                    .field("st_uid").set(invoke, getFakeUid(-1));
            return invoke;
        }
    }

    private static int getFakeUid(int callUid) {
        if (callUid > 0 && callUid <= Process.FIRST_APPLICATION_UID) {
            return callUid;
        }

        if (BActivityThread.isThreadInit() && BActivityThread.currentActivityThread().isInit()) {
            return BActivityThread.getBAppId();
        }
        return ZetaBCore.getHostUid();
    }
}
