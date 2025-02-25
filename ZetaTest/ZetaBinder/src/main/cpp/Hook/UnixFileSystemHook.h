#ifndef ZETAB_UNIXFILESYSTEMHOOK_H
#define ZETAB_UNIXFILESYSTEMHOOK_H

#include "BaseHook.h"

class UnixFileSystemHook : public BaseHook {
public:
    static void init(JNIEnv *env);
};

#endif // ZETAB_UNIXFILESYSTEMHOOK_H
