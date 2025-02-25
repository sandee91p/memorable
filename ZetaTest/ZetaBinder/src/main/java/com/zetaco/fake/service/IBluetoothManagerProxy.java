package com.zetaco.fake.service;

import java.lang.reflect.Method;

import org.android.bluetooth.IBluetoothManager;
import org.android.os.ServiceManager;
import com.zetaco.fake.hook.BinderInvocationStub;
import com.zetaco.fake.hook.MethodHook;
import com.zetaco.fake.hook.ProxyMethod;

public class IBluetoothManagerProxy extends BinderInvocationStub {
    public static final String TAG = "IBluetoothManagerProxy";

    public IBluetoothManagerProxy() {
        super(ServiceManager.getService.call("bluetooth_manager"));
    }

    @Override
    protected Object getWho() {
        return IBluetoothManager.Stub.asInterface.call(ServiceManager.getService.call("bluetooth_manager"));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService("bluetooth_manager");

    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @Override
    protected void onBindMethod() {
        addMethodHook(new GetName());
    }

    @ProxyMethod("getName")
    public static class GetName extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return null;
        }
    }
}
