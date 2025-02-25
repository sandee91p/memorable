package org.android.net.wifi;

import org.Reflector;

public class WifiSsid {
    public static final Reflector REF = Reflector.on("android.net.wifi.WifiSsid");

    public static Reflector.StaticMethodWrapper<Object> createFromAsciiEncoded = REF.staticMethod("createFromAsciiEncoded", String.class);
}
