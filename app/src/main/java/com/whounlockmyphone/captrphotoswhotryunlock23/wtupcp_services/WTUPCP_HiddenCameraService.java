package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_services;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.view.ViewGroup;
import android.view.WindowManager;
import androidx.core.app.ActivityCompat;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_interfaces.WTUPCP_CameraCallbacks;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_CameraConfig;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_CameraError;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_HiddenCameraUtils;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.config.WTUPCP_CameraResolution;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_view.WTUPCP_CameraPreview;

public abstract class WTUPCP_HiddenCameraService extends Service implements WTUPCP_CameraCallbacks {
    private WTUPCP_CameraPreview mCameraPreview;
    private WindowManager mWindowManager;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        super.onDestroy();
        stopCamera();
    }

    public void startCamera(WTUPCP_CameraConfig wTUPCP_CameraConfig) {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), "android.permission.CAMERA") != 0) {
            onCameraError(WTUPCP_CameraError.ERROR_CAMERA_PERMISSION_NOT_AVAILABLE);
        } else if (wTUPCP_CameraConfig.getFacing() != 1 || WTUPCP_HiddenCameraUtils.isFrontCameraAvailable(this)) {
            if (this.mCameraPreview == null) {
                this.mCameraPreview = addPreView();
            }
            this.mCameraPreview.startCameraInternal(wTUPCP_CameraConfig);
        } else {
            onCameraError(WTUPCP_CameraError.ERROR_DOES_NOT_HAVE_FRONT_CAMERA);
        }
    }

    public void takePicture() {
        WTUPCP_CameraPreview wTUPCP_CameraPreview = this.mCameraPreview;
        if (wTUPCP_CameraPreview == null) {
            throw new RuntimeException("Background camera not initialized. Call startCamera() to initialize the camera.");
        } else if (wTUPCP_CameraPreview.isSafeToTakePictureInternal()) {
            this.mCameraPreview.takePictureInternal();
        }
    }

    public void stopCamera() {
        WTUPCP_CameraPreview wTUPCP_CameraPreview = this.mCameraPreview;
        if (wTUPCP_CameraPreview != null) {
            wTUPCP_CameraPreview.stopPreviewAndFreeCamera();
            this.mWindowManager.removeView(this.mCameraPreview);
        }
    }

    private WTUPCP_CameraPreview addPreView() {
        WTUPCP_CameraPreview wTUPCP_CameraPreview = new WTUPCP_CameraPreview(this, this);
        wTUPCP_CameraPreview.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        this.mWindowManager = (WindowManager) getSystemService("window");
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(1, 1, Build.VERSION.SDK_INT < 26 ? WTUPCP_CameraResolution.HIGH_RESOLUTION : 2038, 8, -3);
        layoutParams.gravity = 48;
        layoutParams.x = 0;
        layoutParams.y = 0;
        this.mWindowManager.addView(wTUPCP_CameraPreview, layoutParams);
        return wTUPCP_CameraPreview;
    }
}
