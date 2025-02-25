package com.zetaco.fake.frameworks;

import android.os.IBinder;
import android.os.IInterface;

import java.lang.reflect.ParameterizedType;

import org.Reflector;
import com.zetaco.ZetaBCore;

public abstract class BlackManager<Service extends IInterface> {
    public static final String TAG = "BlackManager";

    private Service mService;

    protected abstract String getServiceName();

    public Service getService() {
        if (mService != null && mService.asBinder().pingBinder() && mService.asBinder().isBinderAlive()) {
            return mService;
        }
        try {
            // 通过反射调用T.Stub.asInterface
            mService = Reflector.on(getTClass().getName() + "$Stub").staticMethod("asInterface", IBinder.class)
                    .callWithClass(ZetaBCore.get().getService(getServiceName()));
            mService.asBinder().linkToDeath(new IBinder.DeathRecipient() {
                @Override
                public void binderDied() {
                    mService.asBinder().unlinkToDeath(this, 0);
                    mService = null;
                }
            }, 0);
            return getService();
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    private Class<Service> getTClass() {
        return (Class<Service>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}