package com.zetaco.fake.service.base;

import java.lang.reflect.Method;

import com.zetaco.ZetaBCore;
import com.zetaco.app.BActivityThread;
import com.zetaco.fake.hook.MethodHook;

public class UidMethodProxy extends MethodHook {
    private final int index;
    private final String name;

    public UidMethodProxy(String name, int index) {
        this.index = index;
        this.name = name;
    }

    @Override
    protected String getMethodName() {
        return name;
    }

    @Override
    protected Object hook(Object who, Method method, Object[] args) throws Throwable {
        int uid = (int) args[index];
        if (uid == BActivityThread.getBUid()) {
            args[index] = ZetaBCore.getHostUid();
        }
        return method.invoke(who, args);
    }
}
