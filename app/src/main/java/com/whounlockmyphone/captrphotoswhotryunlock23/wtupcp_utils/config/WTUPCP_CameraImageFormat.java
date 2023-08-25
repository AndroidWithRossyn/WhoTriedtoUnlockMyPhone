package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class WTUPCP_CameraImageFormat {
    public static final int FORMAT_JPEG = 849;
    public static final int FORMAT_PNG = 545;
    public static final int FORMAT_WEBP = 563;

    @Retention(RetentionPolicy.SOURCE)
    public @interface SupportedImageFormat {
    }

    private WTUPCP_CameraImageFormat() {
        throw new RuntimeException("Cannot initialize CameraImageFormat.");
    }
}
