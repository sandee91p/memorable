package com.zetaco.core.system.pm.installer;

import com.zetaco.core.env.BEnvironment;
import com.zetaco.core.system.pm.BPackageSettings;
import com.zetaco.entity.pm.InstallOption;
import com.zetaco.utils.FileUtils;

public class CreatePackageExecutor implements Executor {

    @Override
    public int exec(BPackageSettings ps, InstallOption option, int userId) {
        FileUtils.deleteDir(BEnvironment.getAppDir(ps.pkg.packageName));
        FileUtils.mkdirs(BEnvironment.getAppDir(ps.pkg.packageName));
        FileUtils.mkdirs(BEnvironment.getAppLibDir(ps.pkg.packageName));
        return 0;
    }
}
