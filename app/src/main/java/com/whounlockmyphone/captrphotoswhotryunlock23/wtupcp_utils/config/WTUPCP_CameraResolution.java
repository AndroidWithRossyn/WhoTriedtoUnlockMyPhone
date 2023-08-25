package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class WTUPCP_CameraResolution {
    public static final int HIGH_RESOLUTION = 2006;
    public static final int LOW_RESOLUTION = 7821;
    public static final int MEDIUM_RESOLUTION = 7895;

    @Retention(RetentionPolicy.SOURCE)
    public @interface SupportedResolution {
    }

    private WTUPCP_CameraResolution() {
        throw new RuntimeException("Cannot initiate CameraResolution.");
    }
}
