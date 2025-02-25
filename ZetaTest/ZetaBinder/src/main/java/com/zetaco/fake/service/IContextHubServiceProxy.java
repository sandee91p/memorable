package com.zetaco.fake.service;

import org.android.hardware.location.IContextHubService;
import org.android.os.ServiceManager;
import com.zetaco.fake.hook.BinderInvocationStub;
import com.zetaco.fake.service.base.ValueMethodProxy;
import com.zetaco.utils.compat.BuildCompat;

public class IContextHubServiceProxy extends BinderInvocationStub {
    public IContextHubServiceProxy() {
        super(ServiceManager.getService.call(getServiceName()));
    }

    private static String getServiceName() {
        return BuildCompat.isOreo() ? "contexthub" : "contexthub_service";
    }

    @Override
    protected Object getWho() {
        return IContextHubService.Stub.asInterface.call(ServiceManager.getService.call(getServiceName()));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService(getServiceName());
    }

    @Override
    protected void onBindMethod() {
        super.onBindMethod();
        addMethodHook(new ValueMethodProxy("registerCallback", 0));
        addMethodHook(new ValueMethodProxy("getContextHubInfo", null));
        addMethodHook(new ValueMethodProxy("getContextHubHandles",new int[]{}));
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }
}
