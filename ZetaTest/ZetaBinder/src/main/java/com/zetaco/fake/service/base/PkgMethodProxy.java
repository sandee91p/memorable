package com.zetaco.fake.service.base;

import java.lang.reflect.Method;

import com.zetaco.fake.hook.MethodHook;
import com.zetaco.utils.MethodParameterUtils;

public class PkgMethodProxy extends MethodHook {
	final String mName;

	public PkgMethodProxy(String name) {
		this.mName = name;
	}

	@Override
	protected String getMethodName() {
		return mName;
	}

	@Override
	protected Object hook(Object who, Method method, Object[] args) throws Throwable {
		MethodParameterUtils.replaceFirstAppPkg(args);
		return method.invoke(who, args);
	}
}
