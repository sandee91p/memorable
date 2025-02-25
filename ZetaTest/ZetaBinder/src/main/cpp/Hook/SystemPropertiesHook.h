#ifndef ZETAB_SYSTEMPROPERTIESHOOK_H
#define ZETAB_SYSTEMPROPERTIESHOOK_H

#include <map>
#include "BaseHook.h"
#include <string>

class SystemPropertiesHook : public BaseHook{
public:
    static void init(JNIEnv *env);
};

#endif // ZETAB_SYSTEMPROPERTIESHOOK_H
