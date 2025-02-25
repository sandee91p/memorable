package com.zetaco.fake.service;

import org.android.os.Build;
import com.zetaco.fake.hook.ClassInvocationStub;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class BuildProxy extends ClassInvocationStub {

    private Timer timer;

    @Override
    protected Object getWho() {
        return Build.REF;
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        // Standard Build fields
        Build.BOARD.set(getSystemProperty("ro.product.board", "unknown"));
        Build.BRAND.set(getSystemProperty("ro.product.brand", "unknown"));
        Build.DEVICE.set(getSystemProperty("ro.product.device", "unknown"));
        Build.DISPLAY.set(getSystemProperty("ro.build.display.id", "unknown"));
        Build.HOST.set(getSystemProperty("ro.build.host", "unknown"));
        Build.ID.set(getSystemProperty("ro.build.id", "unknown"));
        Build.MANUFACTURER.set(getSystemProperty("ro.product.manufacturer", "unknown"));
        Build.MODEL.set(getSystemProperty("ro.product.model", "unknown"));
        Build.PRODUCT.set(getSystemProperty("ro.product.name", "unknown"));
        Build.TAGS.set(getSystemProperty("ro.build.tags", "unknown"));
        Build.TYPE.set(getSystemProperty("ro.build.type", "unknown"));
        Build.USER.set(getSystemProperty("ro.build.user", "unknown"));

        // Additional Build fields
        Build.FINGERPRINT.set(getSystemProperty("ro.build.fingerprint", "unknown"));
        Build.HARDWARE.set(getSystemProperty("ro.hardware", "unknown"));
        Build.RADIO.set(getSystemProperty("ro.baseband", "unknown"));
        Build.BOOTLOADER.set(getSystemProperty("ro.bootloader", "unknown"));
        Build.SERIAL.set(getSystemProperty("ro.serialno", "unknown"));
        Build.SUPPORTED_ABIS.set(getSystemProperty("ro.product.cpu.abilist", "unknown").split(","));
        Build.SUPPORTED_32_BIT_ABIS.set(getSystemProperty("ro.product.cpu.abilist32", "unknown").split(","));
        Build.SUPPORTED_64_BIT_ABIS.set(getSystemProperty("ro.product.cpu.abilist64", "unknown").split(","));
        Build.TIME.set(getSystemPropertyLong("ro.build.date.utc", 0L) * 1000L);
        Build.UNKNOWN.set(getSystemProperty("ro.build.unknown", "unknown"));

        // Version fields
        Build.VERSION.INCREMENTAL.set(getSystemProperty("ro.build.version.incremental", "unknown"));
        Build.VERSION.RELEASE.set(getSystemProperty("ro.build.version.release", "unknown"));
        Build.VERSION.SDK_INT.set(getSystemPropertyInt("ro.build.version.sdk", 0));
        Build.VERSION.SECURITY_PATCH.set(getSystemProperty("ro.build.version.security_patch", "unknown"));
        Build.VERSION.CODENAME.set(getSystemProperty("ro.build.version.codename", "unknown"));
        Build.VERSION.BASE_OS.set(getSystemProperty("ro.build.version.base_os", "unknown"));
        Build.VERSION.PREVIEW_SDK_INT.set(getSystemPropertyInt("ro.build.version.preview_sdk", 0));
        Build.VERSION.SDK_EXTENSION_INT.set(getSystemPropertyInt("ro.build.version.sdk_extension", 0));

        // Security-related fields
        Build.IS_DEBUGGABLE.set(getSystemPropertyBoolean("ro.debuggable", false));
        Build.IS_ENG.set(getSystemPropertyBoolean("ro.eng", false));
        Build.IS_TREBLE_ENABLED.set(getSystemPropertyBoolean("ro.treble.enabled", false));
        Build.IS_USER.set(getSystemProperty("ro.build.type", "user").equals("user"));
        Build.IS_USERDEBUG.set(getSystemProperty("ro.build.type", "userdebug").equals("userdebug"));

        // Real-time system fields
        startRealTimeUpdates();

        // Debugging: Print all properties to log
        debugProperties();

        // Hide container, sandbox, and emulator-related information
        hideContainerSandboxEmulatorInfo();
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    /**
     * Start real-time updates for UPTIME and CURRENT_TIME.
     */
    private void startRealTimeUpdates() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Build.UPTIME.set(getSystemUptime());
                Build.CURRENT_TIME.set(System.currentTimeMillis());
            }
        }, 0, 1000); // Update every second
    }

    /**
     * Stop real-time updates.
     */
    public void stopRealTimeUpdates() {
        if (timer != null) {
            timer.cancel();
        }
    }

    /**
     * Hide container, sandbox, and emulator-related information.
     */
    private void hideContainerSandboxEmulatorInfo() {
        // Hide container-related information
//        Build.BOARD.set("unknown");
//        Build.BRAND.set("unknown");
//        Build.DEVICE.set("unknown");
//
//        // Hide sandbox-related information
//        Build.HOST.set("unknown");
//        Build.ID.set("unknown");
//
//        // Hide emulator-related information
//        Build.MODEL.set("unknown");
//        Build.PRODUCT.set("unknown");
//        Build.SERIAL.set("unknown");
    }

    // Helper methods for fetching system properties
    private String getSystemProperty(String key, String defaultValue) {
        try {
            Class<?> systemPropertiesClass = Class.forName("android.os.SystemProperties");
            return (String) systemPropertiesClass.getMethod("get", String.class, String.class)
                    .invoke(null, key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    private long getSystemPropertyLong(String key, long defaultValue) {
        try {
            String value = getSystemProperty(key, Long.toString(defaultValue));
            return Long.parseLong(value);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    private int getSystemPropertyInt(String key, int defaultValue) {
        try {
            String value = getSystemProperty(key, Integer.toString(defaultValue));
            return Integer.parseInt(value);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    private boolean getSystemPropertyBoolean(String key, boolean defaultValue) {
        try {
            String value = getSystemProperty(key, Boolean.toString(defaultValue));
            return Boolean.parseBoolean(value);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * Fetch real system uptime from /proc/uptime.
     */
    private long getSystemUptime() {
        try (BufferedReader reader = new BufferedReader(new FileReader("/proc/uptime"))) {
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(" ");
                if (parts.length > 0) {
                    return (long) (Double.parseDouble(parts[0]) * 1000L); // Convert seconds to milliseconds
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis() - System.nanoTime() / 1_000_000L; // Fallback to Java-based calculation
    }

    /**
     * Debugging method to print all properties to the log.
     */
    private void debugProperties() {
        System.out.println("Current Device Properties:");
        System.out.println("Board: " + Build.BOARD.get());
        System.out.println("Brand: " + Build.BRAND.get());
        System.out.println("Device: " + Build.DEVICE.get());
        System.out.println("Display: " + Build.DISPLAY.get());
        System.out.println("Host: " + Build.HOST.get());
        System.out.println("ID: " + Build.ID.get());
        System.out.println("Manufacturer: " + Build.MANUFACTURER.get());
        System.out.println("Model: " + Build.MODEL.get());
        System.out.println("Product: " + Build.PRODUCT.get());
        System.out.println("Tags: " + Build.TAGS.get());
        System.out.println("Type: " + Build.TYPE.get());
        System.out.println("User: " + Build.USER.get());
        System.out.println("Fingerprint: " + Build.FINGERPRINT.get());
        System.out.println("Hardware: " + Build.HARDWARE.get());
        System.out.println("Radio: " + Build.RADIO.get());
        System.out.println("Bootloader: " + Build.BOOTLOADER.get());
        System.out.println("Serial: " + Build.SERIAL.get());
        System.out.println("Supported ABIs: " + String.join(",", Build.SUPPORTED_ABIS.get()));
        System.out.println("Supported 32-bit ABIs: " + String.join(",", Build.SUPPORTED_32_BIT_ABIS.get()));
        System.out.println("Supported 64-bit ABIs: " + String.join(",", Build.SUPPORTED_64_BIT_ABIS.get()));
        System.out.println("Build Time: " + Build.TIME.get());
        System.out.println("Unknown: " + Build.UNKNOWN.get());
        System.out.println("Incremental Version: " + Build.VERSION.INCREMENTAL.get());
        System.out.println("Release Version: " + Build.VERSION.RELEASE.get());
        System.out.println("SDK Version: " + Build.VERSION.SDK_INT.get());
        System.out.println("Security Patch: " + Build.VERSION.SECURITY_PATCH.get());
        System.out.println("Codename: " + Build.VERSION.CODENAME.get());
        System.out.println("Base OS: " + Build.VERSION.BASE_OS.get());
        System.out.println("Preview SDK: " + Build.VERSION.PREVIEW_SDK_INT.get());
        System.out.println("SDK Extension: " + Build.VERSION.SDK_EXTENSION_INT.get());
        System.out.println("Is Debuggable: " + Build.IS_DEBUGGABLE.get());
        System.out.println("Is Eng: " + Build.IS_ENG.get());
        System.out.println("Is Treble Enabled: " + Build.IS_TREBLE_ENABLED.get());
        System.out.println("Is User: " + Build.IS_USER.get());
        System.out.println("Is Userdebug: " + Build.IS_USERDEBUG.get());
        System.out.println("Uptime: " + Build.UPTIME.get());
        System.out.println("Current Time: " + Build.CURRENT_TIME.get());
    }
}