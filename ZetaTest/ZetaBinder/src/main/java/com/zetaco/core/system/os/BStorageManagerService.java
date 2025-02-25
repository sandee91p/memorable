package com.zetaco.core.system.os;

import android.net.Uri;
import android.os.Process;
import android.os.storage.StorageVolume;

import java.io.File;

import org.android.os.storage.StorageManager;
import com.zetaco.ZetaBCore;
import com.zetaco.core.env.BEnvironment;
import com.zetaco.core.system.ISystemService;
import com.zetaco.core.system.user.BUserHandle;
import com.zetaco.fake.provider.FileProvider;
import com.zetaco.proxy.ProxyManifest;
import com.zetaco.utils.compat.BuildCompat;

public class BStorageManagerService extends IBStorageManagerService.Stub implements ISystemService {
    private static final BStorageManagerService sService = new BStorageManagerService();

    public static BStorageManagerService get() {
        return sService;
    }

    public BStorageManagerService() { }

    @Override
    public StorageVolume[] getVolumeList(int uid, String packageName, int flags, int userId) {
        if (StorageManager.getVolumeList == null) {
            return null;
        }

        try {
            StorageVolume[] storageVolumes = StorageManager.getVolumeList.call(BUserHandle.getUserId(Process.myUid()), 0);
            if (storageVolumes == null) {
                return null;
            }

            for (StorageVolume storageVolume : storageVolumes) {
                org.android.os.storage.StorageVolume.mPath.set(storageVolume, BEnvironment.getExternalUserDir(userId));
                if (BuildCompat.isPie()) {
                    org.android.os.storage.StorageVolume.mInternalPath.set(storageVolume, BEnvironment.getExternalUserDir(userId));
                }
            }
            return storageVolumes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Uri getUriForFile(String file) {
        return FileProvider.getUriForFile(ZetaBCore.getContext(), ProxyManifest.getProxyFileProvider(), new File(file));
    }

    @Override
    public void systemReady() { }
}
