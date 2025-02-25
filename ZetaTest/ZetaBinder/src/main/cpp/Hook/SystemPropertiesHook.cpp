#include <dobby.h>
#include "SystemPropertiesHook.h"
#include "IO.h"
#include "BoxCore.h"
#import "JniHook/JniHook.h"
#include "Log.h"
#include <sys/system_properties.h> // For __system_property_get

static std::map<std::string, std::string> prop_map;


// Hooked JNI method
HOOK_JNI(jstring, native_get, JNIEnv *env, jobject obj, jstring key, jstring def) {
    const char *key_str = env->GetStringUTFChars(key, JNI_FALSE);
    const char *def_str = env->GetStringUTFChars(def, JNI_FALSE);
    if (key == nullptr || def == nullptr) {
        return orig_native_get(env, obj, key, def);
    }

    auto ret = prop_map.find(key_str);
    if (ret != prop_map.end()) {
        const char *ret_value = ret->second.c_str();
        return env->NewStringUTF(ret_value);
    }

    env->ReleaseStringUTFChars(key, key_str);
    env->ReleaseStringUTFChars(key, def_str);
    return orig_native_get(env, obj, key, def);
}

// Hooked system property get
HOOK_JNI(int, __system_property_get, const char *name, char *value) {
    if (name == nullptr || value == nullptr) {
        return orig___system_property_get(name, value);
    }

    ALOGD(name, value);
    auto ret = prop_map.find(name);
    if (ret != prop_map.end()) {
        const char *ret_value = ret->second.c_str();
        strcpy(value, ret_value);
        return strlen(ret_value);
    }
    return orig___system_property_get(name, value);
}

// Helper function to get current device property
std::string getDeviceProperty(const char *key) {
    char value[PROP_VALUE_MAX] = {0};
    __system_property_get(key, value);
    return std::string(value);
}

// Initialize hooks
void initSystemPropertiesHook(JNIEnv *env) {
    // Populate property map with current device details
    prop_map["ro.product.board"] = getDeviceProperty("ro.product.board");
    prop_map["ro.product.brand"] = getDeviceProperty("ro.product.brand");
    prop_map["ro.product.device"] = getDeviceProperty("ro.product.device");
    prop_map["ro.build.display.id"] = getDeviceProperty("ro.build.display.id");
    prop_map["ro.build.host"] = getDeviceProperty("ro.build.host");
    prop_map["ro.build.id"] = getDeviceProperty("ro.build.id");
    prop_map["ro.product.manufacturer"] = getDeviceProperty("ro.product.manufacturer");
    prop_map["ro.product.model"] = getDeviceProperty("ro.product.model");
    prop_map["ro.product.name"] = getDeviceProperty("ro.product.name");
    prop_map["ro.build.tags"] = getDeviceProperty("ro.build.tags");
    prop_map["ro.build.type"] = getDeviceProperty("ro.build.type");
    prop_map["ro.build.user"] = getDeviceProperty("ro.build.user");

    // Hook JNI method
    JniHook::HookJniFun(env, "android/os/SystemProperties",
                        "native_get", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;",
                        (void *)new_native_get, (void **)&orig_native_get, true);

    // Hook __system_property_get using Dobby
    void *property_get_sym = DobbySymbolResolver("libc.so", "__system_property_get");
    if (property_get_sym) {
        DobbyHook(property_get_sym,
                  (dobby_dummy_func_t)new___system_property_get,
                  (dobby_dummy_func_t *)&orig___system_property_get);
    } else {
        ALOGE("Failed to resolve __system_property_get symbol!");
    }
}