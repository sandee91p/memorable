package com.zetaco.core.system.pm.installer;

import com.zetaco.core.system.pm.BPackageSettings;
import com.zetaco.entity.pm.InstallOption;

public interface Executor {
    String TAG = "InstallExecutor";

    int exec(BPackageSettings ps, InstallOption option, int userId);
}
