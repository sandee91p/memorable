#ifndef ZETAB_BOXCORE_H
#define ZETAB_BOXCORE_H

#include <jni.h>
#include <unistd.h>


class BoxCore {
public:
    static JavaVM *getJavaVM();
    static int getApiLevel();
    static int getCallingUid(int orig);
    static jstring redirectPathString(JNIEnv *env, jstring path);
    static jobject redirectPathFile(JNIEnv *env, jobject path);
    static void replaceFD(JNIEnv *env, jobject fd);
};


#endif // ZETAB_BOXCORE_H
