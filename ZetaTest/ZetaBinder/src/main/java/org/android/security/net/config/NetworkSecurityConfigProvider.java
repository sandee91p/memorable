package org.android.security.net.config;

import android.content.Context;

import org.Reflector;

public class NetworkSecurityConfigProvider {
    public static final Reflector REF = Reflector.on("android.security.net.config.NetworkSecurityConfigProvider");

    public static Reflector.StaticMethodWrapper<Void> install = REF.staticMethod("install", Context.class);
}
