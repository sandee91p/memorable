package com.zetaco.fake.service;

import android.os.IInterface;
import android.os.storage.StorageVolume;

import java.lang.reflect.Method;

import org.android.os.ServiceManager;
import org.android.os.mount.IMountService;
import org.android.os.storage.IStorageManager;
import com.zetaco.ZetaBCore;
import com.zetaco.app.BActivityThread;
import com.zetaco.fake.hook.BinderInvocationStub;
import com.zetaco.fake.hook.MethodHook;
import com.zetaco.fake.hook.ProxyMethod;
import com.zetaco.utils.compat.BuildCompat;

public class IStorageManagerProxy extends BinderInvocationStub {
    public IStorageManagerProxy() {
        super(ServiceManager.getService.call("mount"));
    }

    @Override
    protected Object getWho() {
        IInterface mount;
        if (BuildCompat.isOreo()) {
            mount = IStorageManager.Stub.asInterface.call(ServiceManager.getService.call("mount"));
        } else {
            mount = IMountService.Stub.asInterface.call(ServiceManager.getService.call("mount"));
        }
        return mount;
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService("mount");
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @ProxyMethod("getVolumeList")
    public static class GetVolumeList extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (args == null) {
                StorageVolume[] volumeList = ZetaBCore.getBStorageManager().getVolumeList(BActivityThread.getBUid(), null, 0, BActivityThread.getUserId());
                if (volumeList == null) {
                    return method.invoke(who, args);
                }
                return volumeList;
            }

            try {
                int uid = (int) args[0];
                String packageName = (String) args[1];
                int flags = (int) args[2];

                StorageVolume[] volumeList = ZetaBCore.getBStorageManager().getVolumeList(uid, packageName, flags, BActivityThread.getUserId());
                if (volumeList == null) {
                    return method.invoke(who, args);
                }
                return volumeList;
            } catch (Throwable t) {
                return method.invoke(who, args);
            }
        }
    }

    @ProxyMethod("mkdirs")
    public static class MkDirs extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return 0;
        }
    }
}
