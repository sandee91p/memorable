package org.android.content.pm;

import android.content.pm.PackageParser;

import java.io.File;

import org.Reflector;

public class PackageParserQ {
    public static final Reflector REF = Reflector.on("android.content.pm.PackageParser");

    // Constructor for creating PackageParser instances
    public static Reflector.ConstructorWrapper<PackageParser> _new = REF.constructor();

    // Method for parsing APK files
    public static Reflector.MethodWrapper<PackageParser.Package> parsePackage =
            REF.method("parsePackage", File.class, int.class);

    // Method for collecting certificates
    public static Reflector.StaticMethodWrapper<Void> collectCertificates =
            REF.staticMethod("collectCertificates", PackageParser.Package.class, boolean.class);
}