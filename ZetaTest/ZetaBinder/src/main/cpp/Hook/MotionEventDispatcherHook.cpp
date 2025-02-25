#include "MotionEventDispatcherHook.h"
#include "JniHook/JniHook.h"
#include "Log.h"

// Function pointer for the original dispatchTouchEvent method
//static jboolean (*orig_dispatchTouchEvent)(JNIEnv *env, jobject obj, jobject event) = nullptr;

// Flags to control custom input and touch swapping
bool MotionEventDispatcherHook::isCustomInputEnabled = false;
bool MotionEventDispatcherHook::isSwapTouchEnabled = false;

/*
 * Enable/disable custom input injection
 */
void MotionEventDispatcherHook::enableCustomInput(bool enable) {
    isCustomInputEnabled = enable;
    ALOGD("Custom input injection %s", enable ? "enabled" : "disabled");
}

/*
 * Enable/disable touch event swapping
 */
void MotionEventDispatcherHook::enableSwapTouch(bool enable) {
    isSwapTouchEnabled = enable;
    ALOGD("Touch event swapping %s", enable ? "enabled" : "disabled");
}

/*
 * Inject a swipe gesture from (x1, y1) to (x2, y2)
 */
void MotionEventDispatcherHook::injectSwipe(JNIEnv *env, jobject view, jfloat x1, jfloat y1, jfloat x2, jfloat y2) {
    jclass motionEventClass = env->FindClass("android/view/MotionEvent");
    if (!motionEventClass) {
        ALOGE("Failed to find MotionEvent class");
        return;
    }

    // Get the MotionEvent constants (ACTION_DOWN, ACTION_MOVE, ACTION_UP)
    jfieldID actionDownField = env->GetStaticFieldID(motionEventClass, "ACTION_DOWN", "I");
    jfieldID actionMoveField = env->GetStaticFieldID(motionEventClass, "ACTION_MOVE", "I");
    jfieldID actionUpField = env->GetStaticFieldID(motionEventClass, "ACTION_UP", "I");

    jint ACTION_DOWN = env->GetStaticIntField(motionEventClass, actionDownField);
    jint ACTION_MOVE = env->GetStaticIntField(motionEventClass, actionMoveField);
    jint ACTION_UP = env->GetStaticIntField(motionEventClass, actionUpField);

    // Create a swipe gesture using multiple MotionEvent objects
    jmethodID obtainMethod = env->GetStaticMethodID(motionEventClass, "obtain", "(JJIFFFFIFFFFI)Landroid/view/MotionEvent;");

    // Simulate ACTION_DOWN at (x1, y1)
    jobject downEvent = env->CallStaticObjectMethod(motionEventClass, obtainMethod,
                                                    (jlong) 0, // downTime
                                                    (jlong) 0, // eventTime
                                                    ACTION_DOWN, // action
                                                    x1, y1, // coordinates
                                                    1.0f, // pressure
                                                    1.0f, // size
                                                    0, // metaState
                                                    1.0f, // xPrecision
                                                    1.0f, // yPrecision
                                                    0, // deviceId
                                                    0 // edgeFlags
    );

    // Simulate ACTION_MOVE to (x2, y2)
    jobject moveEvent = env->CallStaticObjectMethod(motionEventClass, obtainMethod,
                                                    (jlong) 0, // downTime
                                                    (jlong) 0, // eventTime
                                                    ACTION_MOVE, // action
                                                    x2, y2, // coordinates
                                                    1.0f, // pressure
                                                    1.0f, // size
                                                    0, // metaState
                                                    1.0f, // xPrecision
                                                    1.0f, // yPrecision
                                                    0, // deviceId
                                                    0 // edgeFlags
    );

    // Simulate ACTION_UP at (x2, y2)
    jobject upEvent = env->CallStaticObjectMethod(motionEventClass, obtainMethod,
                                                  (jlong) 0, // downTime
                                                  (jlong) 0, // eventTime
                                                  ACTION_UP, // action
                                                  x2, y2, // coordinates
                                                  1.0f, // pressure
                                                  1.0f, // size
                                                  0, // metaState
                                                  1.0f, // xPrecision
                                                  1.0f, // yPrecision
                                                  0, // deviceId
                                                  0 // edgeFlags
    );

    // Dispatch the swipe events
    jclass viewClass = env->GetObjectClass(view);
    jmethodID dispatchTouchEventMethod = env->GetMethodID(viewClass, "dispatchTouchEvent", "(Landroid/view/MotionEvent;)Z");
    env->CallBooleanMethod(view, dispatchTouchEventMethod, downEvent);
    env->CallBooleanMethod(view, dispatchTouchEventMethod, moveEvent);
    env->CallBooleanMethod(view, dispatchTouchEventMethod, upEvent);

    // Clean up
    env->DeleteLocalRef(downEvent);
    env->DeleteLocalRef(moveEvent);
    env->DeleteLocalRef(upEvent);
}

/*
 * Inject a click event at (x, y)
 */
void MotionEventDispatcherHook::injectClick(JNIEnv *env, jobject view, jfloat x, jfloat y) {
    jclass motionEventClass = env->FindClass("android/view/MotionEvent");
    if (!motionEventClass) {
        ALOGE("Failed to find MotionEvent class");
        return;
    }

    // Get the MotionEvent constant (ACTION_DOWN)
    jfieldID actionDownField = env->GetStaticFieldID(motionEventClass, "ACTION_DOWN", "I");
    jint ACTION_DOWN = env->GetStaticIntField(motionEventClass, actionDownField);

    // Create a new MotionEvent for a tap
    jmethodID obtainMethod = env->GetStaticMethodID(motionEventClass, "obtain", "(JJIFFFFIFFFFI)Landroid/view/MotionEvent;");
    jobject newEvent = env->CallStaticObjectMethod(motionEventClass, obtainMethod,
                                                   (jlong) 0, // downTime
                                                   (jlong) 0, // eventTime
                                                   ACTION_DOWN, // action
                                                   x, y, // coordinates
                                                   1.0f, // pressure
                                                   1.0f, // size
                                                   0, // metaState
                                                   1.0f, // xPrecision
                                                   1.0f, // yPrecision
                                                   0, // deviceId
                                                   0 // edgeFlags
    );

    // Dispatch the custom event
    jclass viewClass = env->GetObjectClass(view);
    jmethodID dispatchTouchEventMethod = env->GetMethodID(viewClass, "dispatchTouchEvent", "(Landroid/view/MotionEvent;)Z");
    env->CallBooleanMethod(view, dispatchTouchEventMethod, newEvent);

    // Clean up
    env->DeleteLocalRef(newEvent);
}

/*
 * Hook for dispatchTouchEvent method
 */
HOOK_JNI(jboolean, dispatchTouchEvent, JNIEnv *env, jobject obj, jobject event) {
    // Inject a custom touch event if enabled
    if (MotionEventDispatcherHook::isCustomInputEnabled) {
        // Example: Inject a swipe from (100, 100) to (200, 200)
        MotionEventDispatcherHook::injectSwipe(env, obj, 100.0f, 100.0f, 200.0f, 200.0f);

        // Example: Inject a click at (150, 150)
        MotionEventDispatcherHook::injectClick(env, obj, 150.0f, 150.0f);
    }

    // Swap touch events if enabled
    if (MotionEventDispatcherHook::isSwapTouchEnabled) {
        // Swap logic here (if needed)
    }

    // Call the original dispatchTouchEvent
    return orig_dispatchTouchEvent(env, obj, event);
}

void MotionEventDispatcherHook::init(JNIEnv *env) {
    const char *className = "android/view/View";

    // Hook the dispatchTouchEvent method
    JniHook::HookJniFun(env, className, "dispatchTouchEvent", "(Landroid/view/MotionEvent;)Z",
                        (void *) new_dispatchTouchEvent, (void **) (&orig_dispatchTouchEvent), false);

    ALOGD("MotionEventDispatcherHook initialized");
}