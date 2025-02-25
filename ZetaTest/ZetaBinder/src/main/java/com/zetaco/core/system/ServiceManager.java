package com.zetaco.core.system;

import android.os.IBinder;

import java.util.HashMap;
import java.util.Map;

import com.zetaco.ZetaBCore;
import com.zetaco.core.system.accounts.BAccountManagerService;
import com.zetaco.core.system.am.BActivityManagerService;
import com.zetaco.core.system.am.BJobManagerService;
import com.zetaco.core.system.location.BLocationManagerService;
import com.zetaco.core.system.notification.BNotificationManagerService;
import com.zetaco.core.system.os.BStorageManagerService;
import com.zetaco.core.system.pm.BPackageManagerService;
import com.zetaco.core.system.pm.BXposedManagerService;
import com.zetaco.core.system.user.BUserManagerService;

public class ServiceManager {
    public static final String ACTIVITY_MANAGER = "activity_manager";
    public static final String JOB_MANAGER = "job_manager";
    public static final String PACKAGE_MANAGER = "package_manager";
    public static final String STORAGE_MANAGER = "storage_manager";
    public static final String USER_MANAGER = "user_manager";
    public static final String XPOSED_MANAGER = "xposed_manager";
    public static final String ACCOUNT_MANAGER = "account_manager";
    public static final String LOCATION_MANAGER = "location_manager";
    public static final String NOTIFICATION_MANAGER = "notification_manager";

    private final Map<String, IBinder> mCaches = new HashMap<>();

    private static final class SServiceManagerHolder {
        static final ServiceManager sServiceManager = new ServiceManager();
    }

    public static ServiceManager get() {
        return SServiceManagerHolder.sServiceManager;
    }

    public static IBinder getService(String name) {
        return get().getServiceInternal(name);
    }

    private ServiceManager() {
        mCaches.put(ACTIVITY_MANAGER, BActivityManagerService.get());
        mCaches.put(JOB_MANAGER, BJobManagerService.get());
        mCaches.put(PACKAGE_MANAGER, BPackageManagerService.get());
        mCaches.put(STORAGE_MANAGER, BStorageManagerService.get());
        mCaches.put(USER_MANAGER, BUserManagerService.get());
        mCaches.put(XPOSED_MANAGER, BXposedManagerService.get());
        mCaches.put(ACCOUNT_MANAGER, BAccountManagerService.get());
        mCaches.put(LOCATION_MANAGER, BLocationManagerService.get());
        mCaches.put(NOTIFICATION_MANAGER, BNotificationManagerService.get());
    }

    public IBinder getServiceInternal(String name) {
        return mCaches.get(name);
    }

    public static void initBlackManager() {
        ZetaBCore.get().getService(ACTIVITY_MANAGER);
        ZetaBCore.get().getService(JOB_MANAGER);
        ZetaBCore.get().getService(PACKAGE_MANAGER);
        ZetaBCore.get().getService(STORAGE_MANAGER);
        ZetaBCore.get().getService(USER_MANAGER);
        ZetaBCore.get().getService(XPOSED_MANAGER);
        ZetaBCore.get().getService(ACCOUNT_MANAGER);
        ZetaBCore.get().getService(LOCATION_MANAGER);
        ZetaBCore.get().getService(NOTIFICATION_MANAGER);
    }
}
