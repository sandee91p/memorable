#ifndef ZETAB_SCOMPHOOK_H
#define ZETAB_SCOMPHOOK_H

#include "BaseHook.h"

class ScompHook : public BaseHook {
public:
    static void init(JNIEnv *env);
};

#endif // ZETAB_SCOMPHOOK_H