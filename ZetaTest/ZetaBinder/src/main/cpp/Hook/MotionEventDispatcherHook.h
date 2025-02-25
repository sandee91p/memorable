#ifndef ZETAB_MOTIONEVENTDISPATCHERHOOK_H
#define ZETAB_MOTIONEVENTDISPATCHERHOOK_H

#include "BaseHook.h"

class MotionEventDispatcherHook : public BaseHook {
public:
    static void init(JNIEnv *env);

    // Enable/disable custom input injection
    static void enableCustomInput(bool enable);

    // Enable/disable touch event swapping
    static void enableSwapTouch(bool enable);

    // Inject a swipe gesture
    static void injectSwipe(JNIEnv *env, jobject view, jfloat x1, jfloat y1, jfloat x2, jfloat y2);

    // Inject a click event
    static void injectClick(JNIEnv *env, jobject view, jfloat x, jfloat y);

    static bool isSwapTouchEnabled;
    static bool isCustomInputEnabled;
};

#endif // ZETAB_MOTIONEVENTDISPATCHERHOOK_H