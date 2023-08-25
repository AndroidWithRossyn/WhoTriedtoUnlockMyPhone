package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_interfaces;

import androidx.documentfile.provider.DocumentFile;

public interface WTUPCP_CameraCallbacks {
    void onCameraError(int i);

    void onImageCapture(DocumentFile documentFile, Long l);
}
