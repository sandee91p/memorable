package com.zetaco.fake.service;

import android.content.Context;

import org.android.net.IConnectivityManager;
import org.android.os.ServiceManager;
import com.zetaco.fake.hook.BinderInvocationStub;
import com.zetaco.fake.service.base.ValueMethodProxy;

public class IConnectivityManagerProxy extends BinderInvocationStub {
    public static final String TAG = "IConnectivityManagerProxy";

    public IConnectivityManagerProxy() {
        super(ServiceManager.getService.call(Context.CONNECTIVITY_SERVICE));
    }

    @Override
    protected Object getWho() {
        return IConnectivityManager.Stub.asInterface.call(ServiceManager.getService.call(Context.CONNECTIVITY_SERVICE));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    protected void onBindMethod() {
        super.onBindMethod();
        addMethodHook(new ValueMethodProxy("getAllNetworkInfo", null));
        addMethodHook(new ValueMethodProxy("getAllNetworks",null));
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }
}
