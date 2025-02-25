package com.zetaco.utils.compat;

import android.content.pm.PackageParser;
import android.os.Build;
import android.util.DisplayMetrics;

import java.io.File;

import org.android.content.pm.PackageParserLollipop;
import org.android.content.pm.PackageParserLollipop22;
import org.android.content.pm.PackageParserMarshmallow;
import org.android.content.pm.PackageParserNougat;
import org.android.content.pm.PackageParserPie;
import org.android.content.pm.PackageParserQ;
import org.android.content.pm.PackageParserR;
import org.android.content.pm.PackageParserS;
import org.android.content.pm.PackageParserT;
import org.android.content.pm.PackageParserU;

public class PackageParserCompat {
    private static final int API_LEVEL = Build.VERSION.SDK_INT;

    public static PackageParser createParser() {
        if (API_LEVEL >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            return PackageParserU._new.newInstance();
        } else if (API_LEVEL >= Build.VERSION_CODES.TIRAMISU) {
            return PackageParserT._new.newInstance();
        } else if (API_LEVEL >= Build.VERSION_CODES.S) {
            return PackageParserS._new.newInstance();
        } else if (API_LEVEL >= Build.VERSION_CODES.R) {
            return PackageParserR._new.newInstance();
        } else if (API_LEVEL >= Build.VERSION_CODES.Q) {
            return PackageParserQ._new.newInstance();
        } else if (API_LEVEL >= Build.VERSION_CODES.P) {
            return PackageParserPie._new.newInstance();
        } else if (API_LEVEL >= Build.VERSION_CODES.N) {
            return PackageParserNougat._new.newInstance();
        } else if (API_LEVEL >= Build.VERSION_CODES.M) {
            return PackageParserMarshmallow._new.newInstance();
        } else if (API_LEVEL >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            return PackageParserLollipop22._new.newInstance();
        } else if (API_LEVEL >= Build.VERSION_CODES.LOLLIPOP) {
            return PackageParserLollipop._new.newInstance();
        }
        return null;
    }

    public static PackageParser.Package parsePackage(PackageParser parser, File packageFile, int flags) {
        if (API_LEVEL >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            return PackageParserU.parsePackage.call(parser, packageFile, flags);
        } else if (API_LEVEL >= Build.VERSION_CODES.TIRAMISU) {
            return PackageParserT.parsePackage.call(parser, packageFile, flags);
        } else if (API_LEVEL >= Build.VERSION_CODES.S) {
            return PackageParserS.parsePackage.call(parser, packageFile, flags);
        } else if (API_LEVEL >= Build.VERSION_CODES.R) {
            return PackageParserR.parsePackage.call(parser, packageFile, flags);
        } else if (API_LEVEL >= Build.VERSION_CODES.Q) {
            return PackageParserQ.parsePackage.call(parser, packageFile, flags);
        } else if (API_LEVEL >= Build.VERSION_CODES.P) {
            return PackageParserPie.parsePackage.call(parser, packageFile, flags);
        } else if (API_LEVEL >= Build.VERSION_CODES.N) {
            return PackageParserNougat.parsePackage.call(parser, packageFile, flags);
        } else if (API_LEVEL >= Build.VERSION_CODES.M) {
            return PackageParserMarshmallow.parsePackage.call(parser, packageFile, flags);
        } else if (API_LEVEL >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            return PackageParserLollipop22.parsePackage.call(parser, packageFile, flags);
        } else if (API_LEVEL >= Build.VERSION_CODES.LOLLIPOP) {
            return PackageParserLollipop.parsePackage.call(parser, packageFile, flags);
        }
        return org.android.content.pm.PackageParser.parsePackage.call(parser, packageFile, null, new DisplayMetrics(), flags);
    }

    public static void collectCertificates(PackageParser parser, PackageParser.Package p, int flags) {
        if (API_LEVEL >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            PackageParserU.collectCertificates.call(p, true);
        } else if (API_LEVEL >= Build.VERSION_CODES.TIRAMISU) {
            PackageParserT.collectCertificates.call(p, true);
        } else if (API_LEVEL >= Build.VERSION_CODES.S) {
            PackageParserS.collectCertificates.call(p, true);
        } else if (API_LEVEL >= Build.VERSION_CODES.R) {
            PackageParserR.collectCertificates.call(p, true);
        } else if (API_LEVEL >= Build.VERSION_CODES.Q) {
            PackageParserQ.collectCertificates.call(p, true);
        } else if (API_LEVEL >= Build.VERSION_CODES.P) {
            PackageParserPie.collectCertificates.call(p, true);
        } else if (API_LEVEL >= Build.VERSION_CODES.N) {
            PackageParserNougat.collectCertificates.call(p, flags);
        } else if (API_LEVEL >= Build.VERSION_CODES.M) {
            PackageParserMarshmallow.collectCertificates.call(parser, p, flags);
        } else if (API_LEVEL >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            PackageParserLollipop22.collectCertificates.call(parser, p, flags);
        } else if (API_LEVEL >= Build.VERSION_CODES.LOLLIPOP) {
            PackageParserLollipop.collectCertificates.call(parser, p, flags);
        }
        org.android.content.pm.PackageParser.collectCertificates.call(parser, p, flags);
    }
}


//public class PackageParserCompat {
//    private static final int API_LEVEL = Build.VERSION.SDK_INT;
//
//    public static PackageParser createParser() {
//        if (BuildCompat.isQ()) {
//            PackageParser packageParser = PackageParserPie._new.newInstance();
//            packageParser.setCallback(new PackageParser.CallbackImpl(ZetaBCore.getPackageManager()));
//            return packageParser;
//        } else if (API_LEVEL >= 28) {
//            return PackageParserPie._new.newInstance();
//        } else if (API_LEVEL >= M) {
//            return PackageParserMarshmallow._new.newInstance();
//        } else if (API_LEVEL >= LOLLIPOP_MR1) {
//            return PackageParserLollipop22._new.newInstance();
//        } else if (API_LEVEL >= LOLLIPOP) {
//            return PackageParserLollipop._new.newInstance();
//        }
//        return null;
//    }
//
//    public static Package parsePackage(PackageParser parser, File packageFile, int flags) {
//        if (BuildCompat.isPie()) {
//            return PackageParserPie.parsePackage.call(parser, packageFile, flags);
//        } else if (API_LEVEL >= M) {
//            return PackageParserMarshmallow.parsePackage.call(parser, packageFile, flags);
//        } else if (API_LEVEL >= LOLLIPOP_MR1) {
//            return PackageParserLollipop22.parsePackage.call(parser, packageFile, flags);
//        } else if (API_LEVEL >= LOLLIPOP) {
//            return PackageParserLollipop.parsePackage.call(parser, packageFile, flags);
//        }
//        return org.android.content.pm.PackageParser.parsePackage.call(parser, packageFile, null, new DisplayMetrics(), flags);
//    }
//
//    public static void collectCertificates(PackageParser parser, Package p, int flags) {
//        if (BuildCompat.isPie()) {
//            PackageParserPie.collectCertificates.call(p, true);
//        } else if (API_LEVEL >= N) {
//            PackageParserNougat.collectCertificates.call(p, flags);
//        } else if (API_LEVEL >= M) {
//            PackageParserMarshmallow.collectCertificates.call(parser, p, flags);
//        } else if (API_LEVEL >= LOLLIPOP_MR1) {
//            PackageParserLollipop22.collectCertificates.call(parser, p, flags);
//        } else if (API_LEVEL >= LOLLIPOP) {
//            PackageParserLollipop.collectCertificates.call(parser, p, flags);
//        }
//        org.android.content.pm.PackageParser.collectCertificates.call(parser, p, flags);
//    }
//}
