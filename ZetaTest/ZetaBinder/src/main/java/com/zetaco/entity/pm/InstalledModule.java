package com.zetaco.entity.pm;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Parcel;
import android.os.Parcelable;

import com.zetaco.ZetaBCore;
import com.zetaco.core.system.user.BUserHandle;

public class InstalledModule implements Parcelable {
    public String packageName;
    public String name;
    public String desc;
    public String main;
    public boolean enable;

    public InstalledModule() { }

    public ApplicationInfo getApplication() {
        return ZetaBCore.getBPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA, BUserHandle.USER_XPOSED);
    }

    public PackageInfo getPackageInfo() {
        return ZetaBCore.getBPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA, BUserHandle.USER_XPOSED);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packageName);
        dest.writeString(this.name);
        dest.writeString(this.desc);
        dest.writeString(this.main);
        dest.writeByte(this.enable ? (byte) 1 : (byte) 0);
    }

    protected InstalledModule(Parcel in) {
        this.packageName = in.readString();
        this.name = in.readString();
        this.desc = in.readString();
        this.main = in.readString();
        this.enable = in.readByte() != 0;
    }

    public static final Creator<InstalledModule> CREATOR = new Creator<InstalledModule>() {
        @Override
        public InstalledModule createFromParcel(Parcel source) {
            return new InstalledModule(source);
        }

        @Override
        public InstalledModule[] newArray(int size) {
            return new InstalledModule[size];
        }
    };
}
