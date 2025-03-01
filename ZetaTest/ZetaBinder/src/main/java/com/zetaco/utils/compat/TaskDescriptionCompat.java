package com.zetaco.utils.compat;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.Locale;

import com.zetaco.ZetaBCore;
import com.zetaco.app.BActivityThread;
import com.zetaco.utils.DrawableUtils;

public class TaskDescriptionCompat {
    public static ActivityManager.TaskDescription fix(ActivityManager.TaskDescription td) {
        String label = td.getLabel();
        Bitmap icon = td.getIcon();

        if (label != null && icon != null) {
            return td;
        }

        label = getTaskDescriptionLabel(BActivityThread.getUserId(), getApplicationLabel());
        Drawable drawable = getApplicationIcon();
        if (drawable == null) {
            return td;
        }

        ActivityManager am = (ActivityManager) ZetaBCore.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        int iconSize = am.getLauncherLargeIconSize();

        icon = DrawableUtils.drawableToBitmap(drawable, iconSize, iconSize);
        td = new ActivityManager.TaskDescription(label, icon, td.getPrimaryColor());
        return td;
    }

    public static String getTaskDescriptionLabel(int userId, CharSequence label) {
        return String.format(Locale.CHINA, "[B%d]%s", userId, label);
    }

    private static CharSequence getApplicationLabel() {
        try {
            PackageManager pm = ZetaBCore.getPackageManager();
            return pm.getApplicationLabel(pm.getApplicationInfo(BActivityThread.getAppPackageName(), 0));
        } catch (PackageManager.NameNotFoundException ignore) {
            return null;
        }
    }

    private static Drawable getApplicationIcon() {
        try {
            return ZetaBCore.getPackageManager().getApplicationIcon(BActivityThread.getAppPackageName());
        } catch (PackageManager.NameNotFoundException ignore) {
            return null;
        }
    }
}
