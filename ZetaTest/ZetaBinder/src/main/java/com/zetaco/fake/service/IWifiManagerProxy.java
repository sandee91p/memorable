package com.zetaco.fake.service;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.android.net.wifi.IWifiManager;
import org.android.net.wifi.WifiSsid;
import org.android.os.ServiceManager;
import com.zetaco.fake.hook.BinderInvocationStub;
import com.zetaco.fake.hook.MethodHook;
import com.zetaco.fake.hook.ProxyMethod;

public class IWifiManagerProxy extends BinderInvocationStub {
    public static final String TAG = "IWifiManagerProxy";

    public IWifiManagerProxy() {
        super(ServiceManager.getService.call(Context.WIFI_SERVICE));
    }

    @Override
    protected Object getWho() {
        return IWifiManager.Stub.asInterface.call(ServiceManager.getService.call(Context.WIFI_SERVICE));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService(Context.WIFI_SERVICE);
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @Override
    protected void onBindMethod() {
        addMethodHook(new GetConnectionInfo());
        addMethodHook(new GetScanResults());
    }

    @ProxyMethod("getConnectionInfo")
    public static class GetConnectionInfo extends MethodHook {
        /*
         * It doesn't have public method to set BSSID and SSID fields in WifiInfo class,
         * So the reflection framework invocation appeared.
         * commented by BlackBoxing at 2022/03/08
         */
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            WifiInfo wifiInfo = (WifiInfo) method.invoke(who, args);
            org.android.net.wifi.WifiInfo.mBSSID.set(wifiInfo, "ac:62:5a:82:65:c4");
            org.android.net.wifi.WifiInfo.mMacAddress.set(wifiInfo, "ac:62:5a:82:65:c4");
            org.android.net.wifi.WifiInfo.mWifiSsid.set(wifiInfo, WifiSsid.createFromAsciiEncoded.call("ZetaB_Wifi"));
            return wifiInfo;
        }
    }

    @ProxyMethod("getScanResults")
    public static class GetScanResults extends MethodHook {
        /*
         * It doesn't have public method to set BSSID and SSID fields in WifiInfo class,
         * So the reflection framework invocation appeared.
         * commented by BlackBoxing at 2022/03/08
         */
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            Log.d(TAG, "GetScanResults");
            return new ArrayList<ScanResult>();
        }
    }
}
