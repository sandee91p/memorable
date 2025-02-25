package com.zetaco.fake.service;

import org.android.net.wifi.IWifiManager;
import org.android.os.ServiceManager;
import com.zetaco.fake.hook.BinderInvocationStub;

public class IWifiScannerProxy extends BinderInvocationStub {
    public IWifiScannerProxy() {
        super(ServiceManager.getService.call("wifiscanner"));
    }

    @Override
    protected Object getWho() {
        return IWifiManager.Stub.asInterface.call(ServiceManager.getService.call("wifiscanner"));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService("wifiscanner");
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }
}
