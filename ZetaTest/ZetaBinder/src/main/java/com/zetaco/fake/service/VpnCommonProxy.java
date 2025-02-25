package com.zetaco.fake.service;

import java.lang.reflect.Method;
import java.util.List;

import org.com.android.internal.net.VpnConfig;
import com.zetaco.ZetaBCore;
import com.zetaco.app.BActivityThread;
import com.zetaco.fake.hook.MethodHook;
import com.zetaco.fake.hook.ProxyMethod;
import com.zetaco.proxy.ProxyVpnService;
import com.zetaco.utils.MethodParameterUtils;

public class VpnCommonProxy {
    @ProxyMethod("setVpnPackageAuthorization")
    public static class SetVpnPackageAuthorization extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceFirstAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("prepareVpn")
    public static class PrepareVpn extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceFirstAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("establishVpn")
    public static class EstablishVpn extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            VpnConfig.user.set(args[0], ProxyVpnService.class.getName());

            handlePackage(VpnConfig.allowedApplications.get());
            handlePackage(VpnConfig.disallowedApplications.get());
            return method.invoke(who, args);
        }

        private void handlePackage(List<String> applications) {
            if (applications == null) {
                return;
            }

            if (applications.contains(BActivityThread.getAppPackageName())) {
                applications.add(ZetaBCore.getHostPkg());
            }
        }
    }
}
