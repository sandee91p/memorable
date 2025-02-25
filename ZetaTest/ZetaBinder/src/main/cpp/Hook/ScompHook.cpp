#include "ScompHook.h"
#include "JniHook/JniHook.h"
#include "Log.h"
#include <sys/prctl.h>
#include <linux/seccomp.h>
#include <sys/syscall.h>

//// Function pointer for the original prctl function
//static int (*orig_prctl)(int option, unsigned long arg2, unsigned long arg3, unsigned long arg4, unsigned long arg5) = nullptr;
//
//// Function pointer for the original seccomp syscall
//static long (*orig_seccomp)(unsigned int operation, unsigned int flags, void *args) = nullptr;

/*
 * Hook for prctl system call
 */
HOOK_JNI(int, prctl, int option, unsigned long arg2, unsigned long arg3, unsigned long arg4, unsigned long arg5) {
    // Log the prctl call for debugging
    ALOGD("prctl called with option: %d", option);

    // Intercept PR_SET_SECCOMP to manipulate seccomp behavior
    if (option == PR_SET_SECCOMP) {
        ALOGD("Intercepted PR_SET_SECCOMP with arg2: %lu", arg2);
        // You can modify the behavior here if needed
    }

    // Call the original prctl function
    return orig_prctl(option, arg2, arg3, arg4, arg5);
}

/*
 * Hook for seccomp syscall
 */
HOOK_JNI(long, seccomp, unsigned int operation, unsigned int flags, void *args) {
    // Log the seccomp call for debugging
    ALOGD("seccomp called with operation: %u, flags: %u", operation, flags);

    // Intercept SECCOMP_SET_MODE_FILTER to manipulate seccomp filters
    if (operation == SECCOMP_SET_MODE_FILTER) {
        ALOGD("Intercepted SECCOMP_SET_MODE_FILTER");
        // You can modify the behavior here if needed
    }

    // Call the original seccomp syscall
    return orig_seccomp(operation, flags, args);
}

void ScompHook::init(JNIEnv *env) {
    // Hook the prctl system call
    JniHook::HookJniFun(env, "libc.so", "prctl", "(IJJJJ)I",
                        (void *) new_prctl, (void **) (&orig_prctl), false);

    // Hook the seccomp syscall
    JniHook::HookJniFun(env, "libc.so", "seccomp", "(IIJ)J",
                        (void *) new_seccomp, (void **) (&orig_seccomp), false);

    ALOGD("ScompHook initialized");
}