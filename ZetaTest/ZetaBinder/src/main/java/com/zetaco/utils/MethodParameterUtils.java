package com.zetaco.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

import com.zetaco.ZetaBCore;
import com.zetaco.app.BActivityThread;

public class MethodParameterUtils {
    public static <T> T getFirstParam(Object[] args, Class<T> tClass) {
        if (args == null) {
            return null;
        }

        int index = ArrayUtils.indexOfFirst(args, tClass);
        if (index != -1) {
            return (T) args[index];
        }
        return null;
    }

    public static <T> T getFirstParamByInstance(Object[] args, Class<T> tClass) {
        if (args == null) {
            return null;
        }

        int index = ArrayUtils.indexOfObject(args, tClass, 0);
        if (index != -1) {
            return (T) args[index];
        }
        return null;
    }

    public static String replaceFirstAppPkg(Object[] args) {
        if (args == null) {
            return null;
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof String) {
                String value = (String) args[i];
                if (ZetaBCore.get().isInstalled(value, BActivityThread.getUserId())) {
                    args[i] = ZetaBCore.getHostPkg();
                    return value;
                }
            }
        }
        return null;
    }

    public static void replaceAllAppPkg(Object[] args) {
        if (args == null) {
            return;
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                continue;
            }

            if (args[i] instanceof String) {
                String value = (String) args[i];
                if (ZetaBCore.get().isInstalled(value, BActivityThread.getUserId())) {
                    args[i] = ZetaBCore.getHostPkg();
                }
            }
        }
    }

    public static void replaceFirstUid(Object[] args) {
        if (args == null) {
            return;
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Integer) {
                int uid = (int) args[i];
                if (uid == BActivityThread.getBUid()) {
                    args[i] = ZetaBCore.getHostUid();
                }
            }
        }
    }

    public static void replaceLastUid(Object[] args) {
        int index = ArrayUtils.indexOfLast(args, Integer.class);
        if (index != -1) {
            int uid = (int) args[index];
            if (uid == BActivityThread.getBUid()) {
                args[index] = ZetaBCore.getHostUid();
            }
        }
    }

    public static void replaceLastAppPkg(Object[] args) {
        int index = ArrayUtils.indexOfLast(args, String.class);
        if (index != -1) {
            String pkg = (String) args[index];
            if (ZetaBCore.get().isInstalled(pkg, BActivityThread.getUserId())) {
                args[index] = ZetaBCore.getHostPkg();
            }
        }
    }

    public static Class<?>[] getAllInterface(Class<?> clazz) {
        HashSet<Class<?>> classes = new HashSet<>();
        getAllInterfaces(clazz, classes);

        Class<?>[] result = new Class[classes.size()];
        classes.toArray(result);
        return result;
    }

    public static void getAllInterfaces(Class<?> clazz, HashSet<Class<?>> interfaceCollection) {
        Class<?>[] classes = clazz.getInterfaces();
        if (classes.length != 0) {
            interfaceCollection.addAll(Arrays.asList(classes));
        }

        if (clazz.getSuperclass() != Object.class) {
            getAllInterfaces(Objects.requireNonNull(clazz.getSuperclass()), interfaceCollection);
        }
    }
}
