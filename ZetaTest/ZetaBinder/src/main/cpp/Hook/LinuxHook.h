#ifndef ZETAB_LINUXHOOK_H
#define ZETAB_LINUXHOOK_H

#include "BaseHook.h"

class LinuxHook : public BaseHook {
public:
    static void init(JNIEnv *env);
};

#endif // ZETAB_LINUXHOOK_H
