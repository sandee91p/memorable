package com.zetaco.utils.compat;

import android.os.Build;

public class BuildCompat {

    // 14
    public static boolean isU() {
        return Build.VERSION.SDK_INT >= 34 || (Build.VERSION.SDK_INT >= 33 && Build.VERSION.PREVIEW_SDK_INT == 1);
    }

    // 13
    public static boolean isT() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && Build.VERSION.PREVIEW_SDK_INT == 1);
    }

    // 12
    public static boolean isS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Build.VERSION.PREVIEW_SDK_INT == 1);
    }

    // 11
    public static boolean isR() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && Build.VERSION.PREVIEW_SDK_INT == 1);
    }

    // 10
    public static boolean isQ() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && Build.VERSION.PREVIEW_SDK_INT == 1);
    }

    // 9
    public static boolean isPie() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && Build.VERSION.PREVIEW_SDK_INT == 1);
    }

    // 8
    public static boolean isOreo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && Build.VERSION.PREVIEW_SDK_INT == 1);
    }

    // 7
    public static boolean isN() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.PREVIEW_SDK_INT == 1);
    }

    // 6
    public static boolean isM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    // 5
    public static boolean isL() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isSamsung() {
        return "samsung".equalsIgnoreCase(Build.BRAND) || "samsung".equalsIgnoreCase(Build.MANUFACTURER);
    }

    public static boolean isEMUI() {
        if (Build.DISPLAY.toUpperCase().startsWith("EMUI")) {
            return true;
        }

        String property = SystemPropertiesCompat.get("ro.build.version.emui");
        return property != null && property.contains("EmotionUI");
    }

    public static boolean isMIUI() {
        return SystemPropertiesCompat.getInt("ro.miui.ui.version.code", 0) > 0;
    }

    public static boolean isFlyme() {
        return Build.DISPLAY.toLowerCase().contains("flyme");
    }

    public static boolean isColorOS() {
        return SystemPropertiesCompat.isExist("ro.build.version.opporom") || SystemPropertiesCompat.isExist("ro.rom.different.version");
    }

    public static boolean is360UI() {
        String property = SystemPropertiesCompat.get("ro.build.uiversion");
        return property != null && property.toUpperCase().contains("360UI");
    }

    public static boolean isLetv() {
        return Build.MANUFACTURER.equalsIgnoreCase("Letv");
    }

    public static boolean isVivo() {
        return SystemPropertiesCompat.isExist("ro.vivo.os.build.display.id");
    }

    private static ROMType sRomType;
    public static ROMType getROMType() {
        if (sRomType == null) {
            if (isEMUI()) {
                sRomType = ROMType.EMUI;
            } else if (isMIUI()) {
                sRomType = ROMType.MIUI;
            } else if (isFlyme()) {
                sRomType = ROMType.FLYME;
            } else if (isColorOS()) {
                sRomType = ROMType.COLOR_OS;
            } else if (is360UI()) {
                sRomType = ROMType._360;
            } else if (isLetv()) {
                sRomType = ROMType.LETV;
            } else if (isVivo()) {
                sRomType = ROMType.VIVO;
            } else if (isSamsung()) {
                sRomType = ROMType.SAMSUNG;
            } else {
                sRomType = ROMType.OTHER;
            }
        }
        return sRomType;
    }

    public enum ROMType {
        EMUI,
        MIUI,
        FLYME,
        COLOR_OS,
        LETV,
        VIVO,
        _360,
        SAMSUNG,
        OTHER
    }
}