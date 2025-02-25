package com.zetaco.core.system.pm.installer;

import com.zetaco.core.env.BEnvironment;
import com.zetaco.core.system.pm.BPackageSettings;
import com.zetaco.entity.pm.InstallOption;
import com.zetaco.utils.FileUtils;

public class RemoveUserExecutor implements Executor {

    @Override
    public int exec(BPackageSettings ps, InstallOption option, int userId) {
        String packageName = ps.pkg.packageName;
		
        FileUtils.deleteDir(BEnvironment.getDataDir(packageName, userId));
        FileUtils.deleteDir(BEnvironment.getDeDataDir(packageName, userId));
        FileUtils.deleteDir(BEnvironment.getExternalDataDir(packageName, userId));
        return 0;
    }
}
