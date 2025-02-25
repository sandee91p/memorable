#ifndef ZETAB_BINDERHOOK_H
#define ZETAB_BINDERHOOK_H

#include "BaseHook.h"

class BinderHook : public BaseHook{
public:
    static void init(JNIEnv *env);
};

#endif // ZETAB_BINDERHOOK_H
