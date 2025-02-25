package com.zetaco.fake.service;

import org.android.os.ServiceManager;
import org.oem.vivo.IVivoPermissonService;
import com.zetaco.fake.hook.BinderInvocationStub;
import com.zetaco.fake.service.base.PkgMethodProxy;

/**
 * @author Findger
 * @function
 * @date :2023/10/8 20:36
 **/
public class IVivoPermissionServiceProxy extends BinderInvocationStub {
    public IVivoPermissionServiceProxy() {
        super(ServiceManager.getService.call("vivo_permission_service"));
    }

    @Override
    protected Object getWho() {
        return IVivoPermissonService.Stub.asInterface.call(ServiceManager.getService.call("vivo_permission_service"));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService("vivo_permission_service");
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @Override
    protected void onBindMethod() {
        addMethodHook(new PkgMethodProxy("checkPermission"));
        addMethodHook(new PkgMethodProxy("getAppPermission"));
        addMethodHook(new PkgMethodProxy("setAppPermission"));
        addMethodHook(new PkgMethodProxy("setWhiteListApp"));
        addMethodHook(new PkgMethodProxy("setBlackListApp"));
        addMethodHook(new PkgMethodProxy("noteStartActivityProcess"));
        addMethodHook(new PkgMethodProxy("isBuildInThirdPartApp"));
    }
}
