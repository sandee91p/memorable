package com.zetaco.core.system.pm.installer;

import com.zetaco.core.env.BEnvironment;
import com.zetaco.core.system.pm.BPackageSettings;
import com.zetaco.entity.pm.InstallOption;
import com.zetaco.utils.FileUtils;

public class RemoveAppExecutor implements Executor {
    @Override
    public int exec(BPackageSettings ps, InstallOption option, int userId) {
        FileUtils.deleteDir(BEnvironment.getAppDir(ps.pkg.packageName));
        return 0;
    }
}
