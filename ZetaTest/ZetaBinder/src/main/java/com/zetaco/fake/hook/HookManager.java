package com.zetaco.fake.hook;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import org.oem.flyme.IFlymePermissionService;
import org.oem.vivo.IPhysicalFlingManager;
import org.oem.vivo.IPopupCameraManager;
import org.oem.vivo.ISuperResolutionManager;
import org.oem.vivo.ISystemDefenceManager;
import org.oem.vivo.IVivoPermissonService;
import com.zetaco.ZetaBCore;
import com.zetaco.fake.delegate.AppInstrumentation;
import com.zetaco.fake.service.BuildProxy;
import com.zetaco.fake.service.HCallbackProxy;
import com.zetaco.fake.service.IAccessibilityManagerProxy;
import com.zetaco.fake.service.IAccountManagerProxy;
import com.zetaco.fake.service.IActivityClientProxy;
import com.zetaco.fake.service.IActivityManagerProxy;
import com.zetaco.fake.service.IActivityTaskManagerProxy;
import com.zetaco.fake.service.IAlarmManagerProxy;
import com.zetaco.fake.service.IAppOpsManagerProxy;
import com.zetaco.fake.service.IAppWidgetManagerProxy;
import com.zetaco.fake.service.IAutofillManagerProxy;
import com.zetaco.fake.service.IBluetoothManagerProxy;
import com.zetaco.fake.service.IConnectivityManagerProxy;
import com.zetaco.fake.service.IContextHubServiceProxy;
import com.zetaco.fake.service.IDeviceIdentifiersPolicyProxy;
import com.zetaco.fake.service.IDevicePolicyManagerProxy;
import com.zetaco.fake.service.IDisplayManagerProxy;
import com.zetaco.fake.service.IFingerprintManagerProxy;
import com.zetaco.fake.service.IFlymePermissionServiceProxy;
import com.zetaco.fake.service.IGraphicsStatsProxy;
import com.zetaco.fake.service.IJobServiceProxy;
import com.zetaco.fake.service.ILauncherAppsProxy;
import com.zetaco.fake.service.ILocationManagerProxy;
import com.zetaco.fake.service.IMediaRouterServiceProxy;
import com.zetaco.fake.service.IMediaSessionManagerProxy;
import com.zetaco.fake.service.INetworkManagementServiceProxy;
import com.zetaco.fake.service.INotificationManagerProxy;
import com.zetaco.fake.service.IPackageManagerProxy;
import com.zetaco.fake.service.IPermissionManagerProxy;
import com.zetaco.fake.service.IPersistentDataBlockServiceProxy;
import com.zetaco.fake.service.IPhoneSubInfoProxy;
import com.zetaco.fake.service.IPhysicalFlingManagerProxy;
import com.zetaco.fake.service.IPopupCameraManagerProxy;
import com.zetaco.fake.service.IPowerManagerProxy;
import com.zetaco.fake.service.IRoleManagerProxy;
import com.zetaco.fake.service.ISearchManagerProxy;
import com.zetaco.fake.service.IShortcutManagerProxy;
import com.zetaco.fake.service.IStorageManagerProxy;
import com.zetaco.fake.service.IStorageStatsManagerProxy;
import com.zetaco.fake.service.ISubProxy;
import com.zetaco.fake.service.ISuperResolutionManagerProxy;
import com.zetaco.fake.service.ISystemDefenceManagerProxy;
import com.zetaco.fake.service.ISystemUpdateProxy;
import com.zetaco.fake.service.ITelephonyManagerProxy;
import com.zetaco.fake.service.ITelephonyRegistryProxy;
import com.zetaco.fake.service.IUserManagerProxy;
import com.zetaco.fake.service.IVibratorServiceProxy;
import com.zetaco.fake.service.IVivoPermissionServiceProxy;
import com.zetaco.fake.service.IVpnManagerProxy;
import com.zetaco.fake.service.IWifiManagerProxy;
import com.zetaco.fake.service.IWifiScannerProxy;
import com.zetaco.fake.service.IWindowManagerProxy;
import com.zetaco.fake.service.context.ContentServiceProxy;
import com.zetaco.fake.service.context.RestrictionsManagerProxy;
import com.zetaco.fake.service.libcore.OsProxy;
import com.zetaco.utils.Slog;
import com.zetaco.utils.compat.BuildCompat;

public class HookManager {
    public static final String TAG = "HookManager";

    private static final HookManager sHookManager = new HookManager();
    private final Map<Class<?>, IInjectHook> mInjectors = new HashMap<>();

    public static HookManager get() {
        return sHookManager;
    }

    public void init() {
        if (ZetaBCore.get().isBlackProcess() || ZetaBCore.get().isServerProcess()) {
            addInjector(new IDisplayManagerProxy());
            addInjector(new OsProxy());

            addInjector(new ILocationManagerProxy());
            // AM and PM hook
            addInjector(new IActivityManagerProxy());
            addInjector(new IPackageManagerProxy());
            addInjector(new ITelephonyManagerProxy());
            addInjector(new HCallbackProxy());

            /*
             * It takes time to test and enhance the compatibility of WifiManager
             * (only tested in Android 10).
             * commented by BlackBoxing at 2022/03/08
             * */
            addInjector(new IWifiManagerProxy());
            addInjector(new IWifiScannerProxy());
            addInjector(new IBluetoothManagerProxy());

            addInjector(new ISubProxy());
            addInjector(new IAppOpsManagerProxy());
            addInjector(new INotificationManagerProxy());
            addInjector(new IAlarmManagerProxy());
            addInjector(new IAppWidgetManagerProxy());
            addInjector(new ContentServiceProxy());
            addInjector(new IWindowManagerProxy());
            addInjector(new IUserManagerProxy());
            addInjector(new RestrictionsManagerProxy());
            addInjector(new IMediaSessionManagerProxy());
            addInjector(new IStorageManagerProxy());
            addInjector(new ILauncherAppsProxy());
            addInjector(new IJobServiceProxy());
            addInjector(new IAccessibilityManagerProxy());
            addInjector(new ITelephonyRegistryProxy());
            addInjector(new IDevicePolicyManagerProxy());
            addInjector(new IAccountManagerProxy());
            addInjector(new ISearchManagerProxy());
            addInjector(new IConnectivityManagerProxy());
            addInjector(new IPhoneSubInfoProxy());
            addInjector(new IMediaRouterServiceProxy());
            addInjector(new IPowerManagerProxy());
            addInjector(new IVibratorServiceProxy());
            addInjector(new IPersistentDataBlockServiceProxy());
            addInjector(AppInstrumentation.get());

            addInjector(new BuildProxy());
            // 12.0
            if (BuildCompat.isS()) {
                addInjector(new IActivityClientProxy(null));
                addInjector(new IVpnManagerProxy());
            }
            // 11.0
            if (BuildCompat.isR()) {
                addInjector(new IPermissionManagerProxy());
            }
            // 10.0
            if (BuildCompat.isQ()) {
                addInjector(new IDeviceIdentifiersPolicyProxy());
                addInjector(new IRoleManagerProxy());
                addInjector(new IActivityTaskManagerProxy());
            }
            // 9.0
            if (BuildCompat.isPie()) {
                addInjector(new ISystemUpdateProxy());
            }
            //fix flyme service
            if (IFlymePermissionService.TYPE != null) {
                addInjector(new IFlymePermissionServiceProxy());
            }
            // 8.0
            if (BuildCompat.isOreo()) {
                addInjector(new IAutofillManagerProxy());
                addInjector(new IStorageStatsManagerProxy());
            }
            // 7.0
            if (BuildCompat.isN()) {
                addInjector(new IContextHubServiceProxy());
                addInjector(new INetworkManagementServiceProxy());
                addInjector(new IShortcutManagerProxy());
            }
            // 6.0
            if (BuildCompat.isM()) {
                addInjector(new IFingerprintManagerProxy());
                addInjector(new IGraphicsStatsProxy());
            }
            // 5.0
            if (BuildCompat.isL()) {
                addInjector(new IJobServiceProxy());
            }
            if (IPhysicalFlingManager.TYPE != null) {
                addInjector(new IPhysicalFlingManagerProxy());
            }
            if (IPopupCameraManager.TYPE != null) {
                addInjector(new IPopupCameraManagerProxy());
            }
            if (ISuperResolutionManager.TYPE != null) {
                addInjector(new ISuperResolutionManagerProxy());
            }
            if (ISystemDefenceManager.TYPE != null) {
                addInjector(new ISystemDefenceManagerProxy());
            }
            if (IVivoPermissonService.TYPE != null) {
                addInjector(new IVivoPermissionServiceProxy());
            }
        }
        injectAll();
    }

    public void checkEnv(Class<?> clazz) {
        IInjectHook iInjectHook = mInjectors.get(clazz);
        if (iInjectHook != null && iInjectHook.isBadEnv()) {
            Log.d(TAG, "checkEnv: " + clazz.getSimpleName() + " is bad env");
            iInjectHook.injectHook();
        }
    }

    void addInjector(IInjectHook injectHook) {
        mInjectors.put(injectHook.getClass(), injectHook);
    }

    void injectAll() {
        for (IInjectHook value : mInjectors.values()) {
            try {
                Slog.d(TAG, "hook: " + value);
                value.injectHook();
            } catch (Exception e) {
                Slog.d(TAG, "hook error: " + value + " " + e.getMessage());
            }
        }
    }
}
