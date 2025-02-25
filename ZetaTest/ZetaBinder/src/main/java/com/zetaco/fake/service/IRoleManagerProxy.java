package com.zetaco.fake.service;

import android.content.Context;

import org.android.os.ServiceManager;
import org.android.role.IRoleManager;
import com.zetaco.fake.hook.BinderInvocationStub;
import com.zetaco.fake.service.base.PkgMethodProxy;

/**
 * @author Findger
 * @function
 * @date :2023/10/8 19:45
 **/
public class IRoleManagerProxy extends BinderInvocationStub {

    public IRoleManagerProxy() {
        super(ServiceManager.getService.call(Context.ROLE_SERVICE));
    }

    @Override
    protected Object getWho() {
        return IRoleManager.Stub.asInterface.call(ServiceManager.getService.call(Context.ROLE_SERVICE));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService(Context.ROLE_SERVICE);
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @Override
    protected void onBindMethod() {
        addMethodHook(new PkgMethodProxy("isRoleHeld"));
    }
}
