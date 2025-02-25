package com.zetaco.fake.service;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.android.content.pm.IShortcutService;
import org.android.os.ServiceManager;
import org.com.android.internal.infra.AndroidFuture;
import com.zetaco.fake.hook.BinderInvocationStub;
import com.zetaco.fake.hook.MethodHook;
import com.zetaco.fake.hook.ProxyMethod;
import com.zetaco.fake.service.base.PkgMethodProxy;
import com.zetaco.utils.MethodParameterUtils;
import com.zetaco.utils.compat.ParceledListSliceCompat;

public class IShortcutManagerProxy extends BinderInvocationStub {
    public IShortcutManagerProxy() {
        super(ServiceManager.getService.call(Context.SHORTCUT_SERVICE));
    }

    @Override
    protected Object getWho() {
        return IShortcutService.Stub.asInterface.call(ServiceManager.getService.call(Context.SHORTCUT_SERVICE));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService(Context.SHORTCUT_SERVICE);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodParameterUtils.replaceAllAppPkg(args);
        return super.invoke(proxy, method, args);
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @Override
    protected void onBindMethod() {
        super.onBindMethod();
        addMethodHook(new PkgMethodProxy("getShortcuts")); //修复WhatsApp启动黑屏问题
        addMethodHook(new PkgMethodProxy("disableShortcuts"));
        addMethodHook(new PkgMethodProxy("enableShortcuts"));
        addMethodHook(new PkgMethodProxy("getRemainingCallCount"));
        addMethodHook(new PkgMethodProxy("getRateLimitResetTime"));
        addMethodHook(new PkgMethodProxy("getIconMaxDimensions"));
        addMethodHook(new PkgMethodProxy("getMaxShortcutCountPerActivity"));
        addMethodHook(new PkgMethodProxy("reportShortcutUsed"));
        addMethodHook(new PkgMethodProxy("onApplicationActive"));
        addMethodHook(new PkgMethodProxy("hasShortcutHostPermission"));
        addMethodHook(new PkgMethodProxy("removeAllDynamicShortcuts"));
        addMethodHook(new PkgMethodProxy("removeDynamicShortcuts"));
        addMethodHook(new PkgMethodProxy("removeLongLivedShortcuts"));
        addMethodHook(new WrapperShortcutInfo("pushDynamicShortcut", 1,  null));
        addMethodHook(new WrapperShortcutInfo("requestPinShortcut", 1,  false));
        addMethodHook(new WrapperShortcutInfo("addDynamicShortcuts", 1,  false));
        addMethodHook(new WrapperShortcutInfo("setDynamicShortcuts", 1,  false));
        addMethodHook(new PkgMethodProxy("getManifestShortcuts") {

            @Override
            protected Object hook(Object who, Method method, Object[] args) {
                return ParceledListSliceCompat.create(new ArrayList<ShortcutInfo>());
            }
        });
    }

    static class WrapperShortcutInfo extends MethodHook{
        private int infoIndex;
        private Object defValue;
        private String MethodName;

        public WrapperShortcutInfo(String name,int infoIndex, Object defValue) {
            this.infoIndex = infoIndex;
            this.defValue = defValue;
            this.MethodName = name;
        }


        @Override
        protected String getMethodName() {
            return MethodName;
        }

        private Object wrapperResult(Method method,Object result) {
            if (!method.toString().contains("AndroidFuture")) {
                return result;
            }
            Object ret = AndroidFuture.ctor.newInstance();
            AndroidFuture.complete.call(ret, result);
            return ret;
        }

        @RequiresApi(api = Build.VERSION_CODES.N_MR1)
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return wrapperResult(method,defValue);
        }
    }


    @ProxyMethod("requestPinShortcut")
    public static class RequestPinShortcut extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return true;
        }
    }

    @ProxyMethod("setDynamicShortcuts")
    public static class SetDynamicShortcuts extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return true;
        }
    }

    @ProxyMethod("createShortcutResultIntent")
    public static class CreateShortcutResultIntent extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return new Intent();
        }
    }

    @ProxyMethod("getMaxShortcutCountPerActivity")
    public static class GetMaxShortcutCountPerActivity extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return 0;
        }
    }
}
