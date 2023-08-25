package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class WTUPCP_CameraFacing {
    public static final int FRONT_FACING_CAMERA = 1;
    public static final int REAR_FACING_CAMERA = 0;

    @Retention(RetentionPolicy.SOURCE)
    public @interface SupportedCameraFacing {
    }

    private WTUPCP_CameraFacing() {
        throw new RuntimeException("Cannot initialize this class.");
    }
}
