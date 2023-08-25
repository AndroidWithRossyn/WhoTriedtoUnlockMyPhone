package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class WTUPCP_CameraRotation {
    public static final int ROTATION_0 = 0;
    public static final int ROTATION_180 = 180;
    public static final int ROTATION_270 = 270;
    public static final int ROTATION_90 = 90;

    @Retention(RetentionPolicy.SOURCE)
    public @interface SupportedRotation {
    }

    private WTUPCP_CameraRotation() {
        throw new RuntimeException("Cannot initialize this class.");
    }
}
