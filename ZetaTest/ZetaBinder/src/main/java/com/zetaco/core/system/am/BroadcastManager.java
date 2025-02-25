package com.zetaco.core.system.am;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zetaco.ZetaBCore;
import com.zetaco.core.system.pm.BPackage;
import com.zetaco.core.system.pm.BPackageManagerService;
import com.zetaco.core.system.pm.BPackageSettings;
import com.zetaco.core.system.pm.PackageMonitor;
import com.zetaco.entity.am.PendingResultData;
import com.zetaco.proxy.ProxyBroadcastReceiver;
import com.zetaco.utils.Slog;

public class BroadcastManager implements PackageMonitor {
    public static final String TAG = "BroadcastManager";
    public static final int TIMEOUT = 9000;
    public static final int MSG_TIME_OUT = 1;
    private static volatile BroadcastManager sBroadcastManager;

    private final BPackageManagerService mPms;
    private final Map<String, List<BroadcastReceiver>> mReceivers = new HashMap<>();
    private final Map<String, PendingResultData> mReceiversData = new HashMap<>();

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_TIME_OUT) {
                try {
                    PendingResultData data = (PendingResultData) msg.obj;
                    data.build().finish();
                    Slog.d(TAG, "Timeout Receiver: " + data);
                } catch (Throwable ignore) { }
            }
        }
    };

    public static BroadcastManager startSystem(BPackageManagerService pms) {
        if (sBroadcastManager == null) {
            synchronized (BroadcastManager.class) {
                if (sBroadcastManager == null) {
                    sBroadcastManager = new BroadcastManager(pms);
                }
            }
        }
        return sBroadcastManager;
    }

    public BroadcastManager(BPackageManagerService pms) {
        this.mPms = pms;
    }

    public void startup() {
        mPms.addPackageMonitor(this);
        List<BPackageSettings> bPackageSettings = mPms.getBPackageSettings();
        for (BPackageSettings bPackageSetting : bPackageSettings) {
            BPackage bPackage = bPackageSetting.pkg;
            registerPackage(bPackage);
        }
    }

    private void registerPackage(BPackage bPackage) {
        synchronized (mReceivers) {
            Slog.d(TAG, "register: " + bPackage.packageName + ", size: " + bPackage.receivers.size());
            for (BPackage.Activity receiver : bPackage.receivers) {
                List<BPackage.ActivityIntentInfo> intents = receiver.intents;
                for (BPackage.ActivityIntentInfo intent : intents) {
                    ProxyBroadcastReceiver proxyBroadcastReceiver = new ProxyBroadcastReceiver();
                    ZetaBCore.getContext().registerReceiver(proxyBroadcastReceiver, intent.intentFilter, Context.RECEIVER_NOT_EXPORTED);
                    addReceiver(bPackage.packageName, proxyBroadcastReceiver);
                }
            }
        }
    }

    private void addReceiver(String packageName, BroadcastReceiver receiver) {
        List<BroadcastReceiver> broadcastReceivers = mReceivers.get(packageName);
        if (broadcastReceivers == null) {
            broadcastReceivers = new ArrayList<>();
            mReceivers.put(packageName, broadcastReceivers);
        }
        broadcastReceivers.add(receiver);
    }

    public void sendBroadcast(PendingResultData pendingResultData) {
        synchronized (mReceiversData) {
            mReceiversData.put(pendingResultData.mBToken, pendingResultData);
            Message obtain = Message.obtain(mHandler, MSG_TIME_OUT, pendingResultData);
            mHandler.sendMessageDelayed(obtain, TIMEOUT);
        }
    }

    public void finishBroadcast(PendingResultData data) {
        synchronized (mReceiversData) {
            mHandler.removeMessages(MSG_TIME_OUT, mReceiversData.get(data.mBToken));
        }
    }

    @Override
    public void onPackageUninstalled(String packageName, boolean removeApp, int userId) {
        if (removeApp) {
            synchronized (mReceivers) {
                List<BroadcastReceiver> broadcastReceivers = mReceivers.get(packageName);
                if (broadcastReceivers != null) {
                    Slog.d(TAG, "unregisterReceiver Package: " + packageName + ", size: " + broadcastReceivers.size());
                    for (BroadcastReceiver broadcastReceiver : broadcastReceivers) {
                        try {
                            ZetaBCore.getContext().unregisterReceiver(broadcastReceiver);
                        } catch (Throwable ignored) { }
                    }
                }
                mReceivers.remove(packageName);
            }
        }
    }

    @Override
    public void onPackageInstalled(String packageName, int userId) {
        synchronized (mReceivers) {
            mReceivers.remove(packageName);
            BPackageSettings bPackageSetting = mPms.getBPackageSetting(packageName);
            if (bPackageSetting != null) {
                registerPackage(bPackageSetting.pkg);
            }
        }
    }
}
