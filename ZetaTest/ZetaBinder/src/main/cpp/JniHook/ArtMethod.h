#ifndef ARTHOOK_ART_METHOD_H
#define ARTHOOK_ART_METHOD_H

#define __ANDROID_API_R__ 30
#define __ANDROID_API_Q__ 29
#define __ANDROID_API_P__ 28

// ART method access flags
static constexpr uint32_t kAccPublic =       0x0001;  // class, field, method, ic
static constexpr uint32_t kAccPrivate =      0x0002;  // field, method, ic
static constexpr uint32_t kAccProtected =    0x0004;  // field, method, ic
static constexpr uint32_t kAccStatic =       0x0008;  // field, method, ic
static constexpr uint32_t kAccFinal =        0x0010;  // class, field, method, ic
static constexpr uint32_t kAccSynchronized = 0x0020;  // method (only allowed on natives)
static constexpr uint32_t kAccSuper =        0x0020;  // class (not used in dex)
static constexpr uint32_t kAccVolatile =     0x0040;  // field
static constexpr uint32_t kAccBridge =       0x0040;  // method (1.5)
static constexpr uint32_t kAccTransient =    0x0080;  // field
static constexpr uint32_t kAccVarargs =      0x0080;  // method (1.5)
static constexpr uint32_t kAccNative =       0x0100;  // method
static constexpr uint32_t kAccInterface =    0x0200;  // class, ic
static constexpr uint32_t kAccAbstract =     0x0400;  // class, method, ic
static constexpr uint32_t kAccStrict =       0x0800;  // method
static constexpr uint32_t kAccSynthetic =    0x1000;  // class, field, method, ic
static constexpr uint32_t kAccAnnotation =   0x2000;  // class, ic (1.5)
static constexpr uint32_t kAccEnum =         0x4000;  // class, field, ic (1.5)

static constexpr uint32_t kAccPublicApi =             0x10000000;  // field, method
static constexpr uint32_t kAccCorePlatformApi =       0x20000000;  // field, method

// Native method flags
static constexpr uint32_t kAccFastNative =            0x00080000;  // method (runtime; native only)
static constexpr uint32_t kAccCriticalNative =        0x00200000;  // method (runtime; native only)

// ART method structure offsets (version-specific)
enum ArtMethodVersion {
    ART_10_0 = 29,  // Android 10
    ART_11_0 = 30,  // Android 11
    ART_12_0 = 31,  // Android 12
    ART_12L_0 = 32, // Android 12L
    ART_13_0 = 33,  // Android 13
    ART_14_0 = 34,  // Android 14
    ART_15_0 = 35   // Android 15
};

#endif // ARTHOOK_ART_METHOD_H