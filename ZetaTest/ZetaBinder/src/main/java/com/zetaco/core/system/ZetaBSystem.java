package com.zetaco.core.system;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.zetaco.ZetaBCore;
import com.zetaco.core.env.AppSystemEnv;
import com.zetaco.core.env.BEnvironment;
import com.zetaco.core.system.accounts.BAccountManagerService;
import com.zetaco.core.system.am.BActivityManagerService;
import com.zetaco.core.system.am.BJobManagerService;
import com.zetaco.core.system.location.BLocationManagerService;
import com.zetaco.core.system.notification.BNotificationManagerService;
import com.zetaco.core.system.os.BStorageManagerService;
import com.zetaco.core.system.pm.BPackageInstallerService;
import com.zetaco.core.system.pm.BPackageManagerService;
import com.zetaco.core.system.pm.BXposedManagerService;
import com.zetaco.core.system.user.BUserHandle;
import com.zetaco.core.system.user.BUserManagerService;
import com.zetaco.entity.pm.InstallOption;

public class ZetaBSystem {
    private final List<ISystemService> mServices = new ArrayList<>();
    private final static AtomicBoolean isStartup = new AtomicBoolean(false);

    private static final class SZetaBoxSystemHolder {
        static final ZetaBSystem S_ZETA_B_SYSTEM = new ZetaBSystem();
    }

    public static ZetaBSystem getSystem() {
        return SZetaBoxSystemHolder.S_ZETA_B_SYSTEM;
    }

    public void startup() {
        if (isStartup.getAndSet(true)) {
            return;
        }

        BEnvironment.load();
        mServices.add(BPackageManagerService.get());
        mServices.add(BUserManagerService.get());
        mServices.add(BActivityManagerService.get());
        mServices.add(BJobManagerService.get());
        mServices.add(BStorageManagerService.get());
        mServices.add(BPackageInstallerService.get());
        mServices.add(BXposedManagerService.get());
        mServices.add(BProcessManagerService.get());
        mServices.add(BAccountManagerService.get());
        mServices.add(BLocationManagerService.get());
        mServices.add(BNotificationManagerService.get());

        for (ISystemService service : mServices) {
            service.systemReady();
        }

        List<String> preInstallPackages = AppSystemEnv.getPreInstallPackages();
        for (String preInstallPackage : preInstallPackages) {
            try {
                if (!BPackageManagerService.get().isInstalled(preInstallPackage, BUserHandle.USER_ALL)) {
                    PackageInfo packageInfo = ZetaBCore.getPackageManager().getPackageInfo(preInstallPackage, 0);
                    BPackageManagerService.get().installPackageAsUser(packageInfo.applicationInfo.sourceDir, InstallOption.installBySystem(), BUserHandle.USER_ALL);
                }
            } catch (PackageManager.NameNotFoundException ignored) { }
        }
    }
}
