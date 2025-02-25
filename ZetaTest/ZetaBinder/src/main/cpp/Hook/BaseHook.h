#ifndef ZETAB_BASEHOOK_H
#define ZETAB_BASEHOOK_H

#include <jni.h>
#include <Log.h>

class BaseHook {
public:
    static void init(JNIEnv *env);
};

#endif // ZETAB_BASEHOOK_H
