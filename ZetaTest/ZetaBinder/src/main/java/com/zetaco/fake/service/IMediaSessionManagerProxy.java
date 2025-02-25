package com.zetaco.fake.service;

import android.content.Context;

import java.lang.reflect.Method;

import org.android.media.session.ISessionManager;
import org.android.os.ServiceManager;
import com.zetaco.ZetaBCore;
import com.zetaco.fake.hook.BinderInvocationStub;
import com.zetaco.fake.hook.MethodHook;
import com.zetaco.fake.hook.ProxyMethod;

public class IMediaSessionManagerProxy extends BinderInvocationStub {
    public IMediaSessionManagerProxy() {
        super(ServiceManager.getService.call(Context.MEDIA_SESSION_SERVICE));
    }

    @Override
    protected Object getWho() {
        return ISessionManager.Stub.asInterface.call(ServiceManager.getService.call(Context.MEDIA_SESSION_SERVICE));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService(Context.MEDIA_SESSION_SERVICE);

    }

    @Override
    public boolean isBadEnv() {
        return false;
    }


    @ProxyMethod("createSession")
    public static class CreateSession extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (args != null && args.length > 0 && args[0] instanceof String) {
                args[0] = ZetaBCore.getHostPkg();
            }
            return method.invoke(who, args);
        }
    }
}
