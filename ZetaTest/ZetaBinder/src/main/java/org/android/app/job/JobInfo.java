package org.android.app.job;

import android.content.ComponentName;

import org.Reflector;

public class JobInfo {
    public static final Reflector REF = Reflector.on("android.app.job.JobInfo");

    public static Reflector.FieldWrapper<ComponentName> service = REF.field("service");
}
