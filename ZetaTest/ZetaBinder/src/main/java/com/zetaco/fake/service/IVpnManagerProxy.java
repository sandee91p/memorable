package com.zetaco.fake.service;

import org.android.net.IVpnManager;
import org.android.os.ServiceManager;
import com.zetaco.fake.hook.BinderInvocationStub;
import com.zetaco.fake.hook.ScanClass;

@ScanClass(VpnCommonProxy.class)
public class IVpnManagerProxy extends BinderInvocationStub {
    public static final String TAG = "IVpnManagerProxy";

    public IVpnManagerProxy() {
        super(ServiceManager.getService.call("vpn_management"));
    }

    @Override
    protected Object getWho() {
        return IVpnManager.Stub.asInterface.call(ServiceManager.getService.call("vpn_management"));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService("vpn_management");
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }
}
