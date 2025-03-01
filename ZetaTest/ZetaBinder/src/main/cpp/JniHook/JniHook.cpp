#include <jni.h>
#include <cstring>
#include "JniHook.h"
#include "Log.h"
#include "ArtMethod.h"

#define JNIHOOKCLASS "com/zetaco/jnihook/JniHook"
#define JNIMETHOOG_Utills "com/zetaco/jnihook/MethodUtils"

static struct {
    int api_level;
    unsigned int art_field_size;
    int art_field_flags_offset;

    unsigned int art_method_size;
    int art_method_flags_offset;
    int art_method_native_offset;

    int class_flags_offset;

    jclass method_utils_class;
    jmethodID get_method_desc_id;
    jmethodID get_method_declaring_class_id;
    jmethodID get_method_name_id;
} HookEnv;

// Helper function to get method description
static const char *GetMethodDesc(JNIEnv *env, jobject javaMethod) {
    jstring desc = reinterpret_cast<jstring>(env->CallStaticObjectMethod(HookEnv.method_utils_class, HookEnv.get_method_desc_id, javaMethod));
    const char *result = env->GetStringUTFChars(desc, JNI_FALSE);
    env->DeleteLocalRef(desc); // Release the local reference
    return result;
}

// Helper function to get method declaring class
static const char *GetMethodDeclaringClass(JNIEnv *env, jobject javaMethod) {
    jstring desc = reinterpret_cast<jstring>(env->CallStaticObjectMethod(HookEnv.method_utils_class, HookEnv.get_method_declaring_class_id, javaMethod));
    const char *result = env->GetStringUTFChars(desc, JNI_FALSE);
    env->DeleteLocalRef(desc); // Release the local reference
    return result;
}

// Helper function to get method name
static const char *GetMethodName(JNIEnv *env, jobject javaMethod) {
    jstring desc = reinterpret_cast<jstring>(env->CallStaticObjectMethod(HookEnv.method_utils_class, HookEnv.get_method_name_id, javaMethod));
    const char *result = env->GetStringUTFChars(desc, JNI_FALSE);
    env->DeleteLocalRef(desc); // Release the local reference
    return result;
}

// Get access flags from ART method
inline static uint32_t GetAccessFlags(const char *art_method) {
    return *reinterpret_cast<const uint32_t *>(art_method + HookEnv.art_method_flags_offset);
}

// Set access flags for ART method
inline static bool SetAccessFlags(char *art_method, uint32_t flags) {
    *reinterpret_cast<uint32_t *>(art_method + HookEnv.art_method_flags_offset) = flags;
    return true;
}

// Add access flag to ART method
inline static bool AddAccessFlag(char *art_method, uint32_t flag) {
    uint32_t old_flag = GetAccessFlags(art_method);
    uint32_t new_flag = old_flag | flag;
    return new_flag != old_flag && SetAccessFlags(art_method, new_flag);
}

// Clear access flag from ART method
inline static bool ClearAccessFlag(char *art_method, uint32_t flag) {
    uint32_t old_flag = GetAccessFlags(art_method);
    uint32_t new_flag = old_flag & ~flag;
    return new_flag != old_flag && SetAccessFlags(art_method, new_flag);
}

// Check if ART method has access flag
inline static bool HasAccessFlag(char *art_method) {
    uint32_t flags = GetAccessFlags(art_method);
    return (flags & kAccNative) == kAccNative;
}

// Clear FastNative flag for ART method
inline static bool ClearFastNativeFlag(char *art_method) {
    return HookEnv.api_level < __ANDROID_API_P__ && ClearAccessFlag(art_method, kAccFastNative);
}

// Get ART method pointer
static void *GetArtMethod(JNIEnv *env, jclass clazz, jmethodID methodId) {
    if (HookEnv.api_level >= __ANDROID_API_Q__) {
        jclass executable = env->FindClass("java/lang/reflect/Executable");
        jfieldID artId = env->GetFieldID(executable, "artMethod", "J");
        jobject method = env->ToReflectedMethod(clazz, methodId, true);
        return reinterpret_cast<void *>(env->GetLongField(method, artId));
    } else {
        return methodId;
    }
}

// Get ART field pointer
static void *GetFieldMethod(JNIEnv *env, jobject field) {
    if (HookEnv.api_level >= __ANDROID_API_Q__) {
        jclass fieldClass = env->FindClass("java/lang/reflect/Field");
        jmethodID getArtField = env->GetMethodID(fieldClass, "getArtField", "()J");
        return reinterpret_cast<void *>(env->CallLongMethod(field, getArtField));
    } else {
        return env->FromReflectedField(field);
    }
}

// Check ART method flags
bool CheckFlags(void *artMethod) {
    char *method = static_cast<char *>(artMethod);
    if (!HasAccessFlag(method)) {
        ALOGE("not native method");
        return false;
    }
    ClearFastNativeFlag(method);
    return true;
}

// Hook JNI function with Java method object
void JniHook::HookJniFun(JNIEnv *env, jobject java_method, void *new_fun, void **orig_fun, bool is_static) {
    const char *class_name = GetMethodDeclaringClass(env, java_method);
    const char *method_name = GetMethodName(env, java_method);
    const char *sign = GetMethodDesc(env, java_method);
    HookJniFun(env, class_name, method_name, sign, new_fun, orig_fun, is_static);
}

// Hook JNI function with class name, method name, and signature
void JniHook::HookJniFun(JNIEnv *env, const char *class_name, const char *method_name, const char *sign, void *new_fun, void **orig_fun, bool is_static) {
    if (HookEnv.art_method_native_offset == 0) {
        return;
    }

    jclass clazz = env->FindClass(class_name);
    if (!clazz) {
        ALOGD("findClass fail: %s %s", class_name, method_name);
        env->ExceptionClear();
        return;
    }

    jmethodID method;
    if (is_static) {
        method = env->GetStaticMethodID(clazz, method_name, sign);
    } else {
        method = env->GetMethodID(clazz, method_name, sign);
    }

    if (!method) {
        env->ExceptionClear();
        ALOGD("get method id fail: %s %s", class_name, method_name);
        return;
    }

    JNINativeMethod gMethods[] = {
            { method_name, sign, (void *) new_fun },
    };

    auto artMethod = reinterpret_cast<uintptr_t *>(GetArtMethod(env, clazz, method));
    // 不检查系统包
    if (!strncmp(class_name, "android.", 8) && !CheckFlags(artMethod)) {
        ALOGE("check flags error. class：%s, method：%s", class_name, method_name);
        return;
    }

    *orig_fun = reinterpret_cast<void *>(artMethod[HookEnv.art_method_native_offset]);
    if (env->RegisterNatives(clazz, gMethods, 1) < 0) {
        ALOGE("JNI hook error. class：%s, method：%s", class_name, method_name);
        return;
    }

    // FastNative
    if (HookEnv.api_level == __ANDROID_API_O__ || HookEnv.api_level == __ANDROID_API_O_MR1__) {
        AddAccessFlag((char *) artMethod, kAccFastNative);
    }
    ALOGD("Register class：%s, method：%s success!", class_name, method_name);
}

// Native offset functions
__attribute__((section (".mytext")))
JNICALL void native_offset(JNIEnv *env, jclass obj) { }

__attribute__((section (".mytext")))
JNICALL void native_offset2(JNIEnv *env, jclass obj) { }

// Set method accessible
__attribute__((section (".mytext")))
JNICALL void set_method_accessible(JNIEnv *env, jclass obj, jclass clazz, jobject method) {
    jmethodID methodId = env->FromReflectedMethod(method);
    char *art_method = static_cast<char *>(GetArtMethod(env, clazz, methodId));

    AddAccessFlag(art_method, kAccPublic);
    if (HookEnv.api_level >= __ANDROID_API_Q__) {
        AddAccessFlag(art_method, kAccPublicApi);
    }
}

// Set field accessible
__attribute__((section (".mytext")))
JNICALL void set_field_accessible(JNIEnv *env, jclass obj, jclass clazz, jobject field) {
    char *artField = static_cast<char *>(GetFieldMethod(env, field));
    AddAccessFlag(artField, kAccPublic);

    if (HookEnv.api_level >= __ANDROID_API_Q__) {
        AddAccessFlag(artField, kAccPublicApi);
    }
    ClearAccessFlag(artField, kAccFinal);
}

// Register native methods
void registerNative(JNIEnv *env) {
    jclass clazz = env->FindClass(JNIHOOKCLASS);
    JNINativeMethod gMethods[] = {
            {"nativeOffset", "()V", (void *) native_offset},
            {"nativeOffset2", "()V", (void *) native_offset2},
    };

    if (env->RegisterNatives(clazz, gMethods, sizeof(gMethods) / sizeof(gMethods[0])) < 0) {
        ALOGE("JNI register error.");
    }
}

// Initialize JNI hook
void JniHook::InitJniHook(JNIEnv *env, int api_level) {
    registerNative(env);
    HookEnv.api_level = api_level;

    jclass clazz = env->FindClass(JNIHOOKCLASS);
    jmethodID nativeOffsetId = env->GetStaticMethodID(clazz, "nativeOffset", "()V");
    jmethodID nativeOffset2Id = env->GetStaticMethodID(clazz, "nativeOffset2", "()V");

    jfieldID nativeOffsetFieldId = env->GetStaticFieldID(clazz, "NATIVE_OFFSET", "I");
    jfieldID nativeOffsetField2Id = env->GetStaticFieldID(clazz, "NATIVE_OFFSET_2", "I");

    void *nativeOffsetField = GetFieldMethod(env, env->ToReflectedField(clazz, nativeOffsetFieldId, true));
    void *nativeOffsetField2 = GetFieldMethod(env, env->ToReflectedField(clazz, nativeOffsetField2Id, true));
    HookEnv.art_field_size = (size_t) nativeOffsetField2 - (size_t) nativeOffsetField;

    void *nativeOffset = GetArtMethod(env, clazz, nativeOffsetId);
    void *nativeOffset2 = GetArtMethod(env, clazz, nativeOffset2Id);
    HookEnv.art_method_size = (size_t) nativeOffset2 - (size_t) nativeOffset;

    // Calculate native offset
    auto artMethod = reinterpret_cast<uintptr_t *>(nativeOffset);
    for (int i = 0; i < HookEnv.art_method_size; ++i) {
        if (reinterpret_cast<void *>(artMethod[i]) == native_offset) {
            HookEnv.art_method_native_offset = i;
            break;
        }
    }

    uint32_t flags = 0x0;
    flags = flags | kAccPublic;
    flags = flags | kAccStatic;
    flags = flags | kAccNative;
    flags = flags | kAccFinal;
    if (api_level >= __ANDROID_API_Q__) {
        flags = flags | kAccPublicApi;
    }

    char *start = reinterpret_cast<char *>(artMethod);
    for (int i = 1; i < HookEnv.art_method_size; ++i) {
        auto value = *(uint32_t *) (start + i * sizeof(uint32_t));
        if (value == flags) {
            HookEnv.art_method_flags_offset = i * sizeof(uint32_t);
            break;
        }
    }

    flags = 0x0;
    flags = flags | kAccPublic;
    flags = flags | kAccStatic;
    flags = flags | kAccFinal;
    if (api_level >= __ANDROID_API_Q__) {
        flags = flags | kAccPublicApi;
    }

    char *fieldStart = reinterpret_cast<char *>(nativeOffsetField);
    for (int i = 1; i < HookEnv.art_field_size; ++i) {
        auto value = *(int32_t *) (fieldStart + i * sizeof(int32_t));
        if (value == flags) {
            HookEnv.art_field_flags_offset = i * sizeof(int32_t);
            break;
        }
    }

    HookEnv.method_utils_class = env->FindClass(JNIMETHOOG_Utills);
    HookEnv.get_method_desc_id = env->GetStaticMethodID(HookEnv.method_utils_class, "getDesc", "(Ljava/lang/reflect/Method;)Ljava/lang/String;");
    HookEnv.get_method_declaring_class_id = env->GetStaticMethodID(HookEnv.method_utils_class, "getDeclaringClass", "(Ljava/lang/reflect/Method;)Ljava/lang/String;");
    HookEnv.get_method_name_id = env->GetStaticMethodID(HookEnv.method_utils_class, "getMethodName", "(Ljava/lang/reflect/Method;)Ljava/lang/String;");
}