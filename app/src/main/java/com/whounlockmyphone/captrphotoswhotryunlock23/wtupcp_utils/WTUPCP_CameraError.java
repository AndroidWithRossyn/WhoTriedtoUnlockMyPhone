package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class WTUPCP_CameraError {
    public static final int ERROR_CAMERA_OPEN_FAILED = 1122;
    public static final int ERROR_CAMERA_PERMISSION_NOT_AVAILABLE = 5472;
    public static final int ERROR_DOES_NOT_HAVE_FRONT_CAMERA = 8722;
    public static final int ERROR_DOES_NOT_HAVE_OVERDRAW_PERMISSION = 3136;
    public static final int ERROR_IMAGE_WRITE_FAILED = 9854;

    @Retention(RetentionPolicy.SOURCE)
    public @interface CameraErrorCodes {
    }

    private WTUPCP_CameraError() {
        throw new RuntimeException("Cannot initiate CameraError.");
    }
}
