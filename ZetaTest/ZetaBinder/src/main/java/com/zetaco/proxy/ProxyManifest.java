package com.zetaco.proxy;

import java.util.Locale;

import com.zetaco.ZetaBCore;

public class ProxyManifest {
    public static final int FREE_COUNT = 50;

    public static boolean isProxy(String msg) {
        return getBindProvider().equals(msg) || msg.contains("proxy_content_provider_");
    }

    public static String getBindProvider() {
        return ZetaBCore.getHostPkg() + ".zetab.SystemCallProvider";
    }

    public static String getProxyAuthorities(int index) {
        return String.format(Locale.CHINA, "%s.proxy_content_provider_%d", ZetaBCore.getHostPkg(), index);
    }

    public static String getProxyPendingActivity(int index) {
        return String.format(Locale.CHINA, "com.zetaco.proxy.ProxyPendingActivity$P%d", index);
    }

    public static String getProxyActivity(int index) {
        return String.format(Locale.CHINA, "com.zetaco.proxy.ProxyActivity$P%d", index);
    }

    public static String TransparentProxyActivity(int index) {
        return String.format(Locale.CHINA, "com.zetaco.proxy.TransparentProxyActivity$P%d", index);
    }

    public static String getProxyService(int index) {
        return String.format(Locale.CHINA, "com.zetaco.proxy.ProxyService$P%d", index);
    }

    public static String getProxyJobService(int index) {
        return String.format(Locale.CHINA, "com.zetaco.proxy.ProxyJobService$P%d", index);
    }

    public static String getProxyFileProvider() {
        return ZetaBCore.getHostPkg() + ".zetab.FileProvider";
    }

    public static String getProcessName(int bPid) {
        return ZetaBCore.getHostPkg() + ":p" + bPid;
    }
}
