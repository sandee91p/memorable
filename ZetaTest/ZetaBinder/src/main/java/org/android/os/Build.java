package org.android.os;

import org.Reflector;

public class Build {
    public static final Reflector REF = Reflector.on("android.os.Build");

    // Standard Build fields
    public static Reflector.FieldWrapper<String> BOARD = REF.field("BOARD");
    public static Reflector.FieldWrapper<String> BRAND = REF.field("BRAND");
    public static Reflector.FieldWrapper<String> DEVICE = REF.field("DEVICE");
    public static Reflector.FieldWrapper<String> DISPLAY = REF.field("DISPLAY");
    public static Reflector.FieldWrapper<String> HOST = REF.field("HOST");
    public static Reflector.FieldWrapper<String> ID = REF.field("ID");
    public static Reflector.FieldWrapper<String> MANUFACTURER = REF.field("MANUFACTURER");
    public static Reflector.FieldWrapper<String> MODEL = REF.field("MODEL");
    public static Reflector.FieldWrapper<String> PRODUCT = REF.field("PRODUCT");
    public static Reflector.FieldWrapper<String> TAGS = REF.field("TAGS");
    public static Reflector.FieldWrapper<String> TYPE = REF.field("TYPE");
    public static Reflector.FieldWrapper<String> USER = REF.field("USER");

    // Additional Build fields
    public static Reflector.FieldWrapper<String> FINGERPRINT = REF.field("FINGERPRINT");
    public static Reflector.FieldWrapper<String> HARDWARE = REF.field("HARDWARE");
    public static Reflector.FieldWrapper<String> RADIO = REF.field("RADIO");
    public static Reflector.FieldWrapper<String> BOOTLOADER = REF.field("BOOTLOADER");
    public static Reflector.FieldWrapper<String> SERIAL = REF.field("SERIAL");
    public static Reflector.FieldWrapper<String[]> SUPPORTED_ABIS = REF.field("SUPPORTED_ABIS");
    public static Reflector.FieldWrapper<String[]> SUPPORTED_32_BIT_ABIS = REF.field("SUPPORTED_32_BIT_ABIS");
    public static Reflector.FieldWrapper<String[]> SUPPORTED_64_BIT_ABIS = REF.field("SUPPORTED_64_BIT_ABIS");
    public static Reflector.FieldWrapper<Long> TIME = REF.field("TIME");
    public static Reflector.FieldWrapper<String> UNKNOWN = REF.field("UNKNOWN");

    // Version fields
    public static class VERSION {
        public static final Reflector REF = Reflector.on("android.os.Build$VERSION");

        public static Reflector.FieldWrapper<String> INCREMENTAL = REF.field("INCREMENTAL");
        public static Reflector.FieldWrapper<String> RELEASE = REF.field("RELEASE");
        public static Reflector.FieldWrapper<Integer> SDK_INT = REF.field("SDK_INT");
        public static Reflector.FieldWrapper<String> SECURITY_PATCH = REF.field("SECURITY_PATCH");
        public static Reflector.FieldWrapper<String> CODENAME = REF.field("CODENAME");
        public static Reflector.FieldWrapper<String> BASE_OS = REF.field("BASE_OS");
        public static Reflector.FieldWrapper<Integer> PREVIEW_SDK_INT = REF.field("PREVIEW_SDK_INT");
        public static Reflector.FieldWrapper<Integer> SDK_EXTENSION_INT = REF.field("SDK_EXTENSION_INT");
    }

    // Security-related fields
    public static Reflector.FieldWrapper<Boolean> IS_DEBUGGABLE = REF.field("IS_DEBUGGABLE");
    public static Reflector.FieldWrapper<Boolean> IS_ENG = REF.field("IS_ENG");
    public static Reflector.FieldWrapper<Boolean> IS_TREBLE_ENABLED = REF.field("IS_TREBLE_ENABLED");
    public static Reflector.FieldWrapper<Boolean> IS_USER = REF.field("IS_USER");
    public static Reflector.FieldWrapper<Boolean> IS_USERDEBUG = REF.field("IS_USERDEBUG");

    // Real-time system fields
    public static Reflector.FieldWrapper<Long> UPTIME = REF.field("UPTIME");
    public static Reflector.FieldWrapper<Long> CURRENT_TIME = REF.field("CURRENT_TIME");
}