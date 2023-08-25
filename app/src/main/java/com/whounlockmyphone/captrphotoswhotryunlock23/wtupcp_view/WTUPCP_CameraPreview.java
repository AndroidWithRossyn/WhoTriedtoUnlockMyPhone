package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.documentfile.provider.DocumentFile;
import androidx.exifinterface.media.ExifInterface;
import com.whounlockmyphone.captrphotoswhotryunlock23.R;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_interfaces.WTUPCP_CameraCallbacks;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_CameraConfig;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_CameraError;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_Constants;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_HiddenCameraUtils;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_SharePreferenceUtils;
import java.io.IOException;
import java.util.List;

public class WTUPCP_CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    Context context;
    
    public Camera mCamera;
    
    public WTUPCP_CameraCallbacks mCameraCallbacks;
    
    public WTUPCP_CameraConfig mCameraConfig;
    private SurfaceHolder mHolder;
    
    public volatile boolean safeToTakePicture = false;
    WTUPCP_SharePreferenceUtils sharePreferenceUtils;

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
    }

    public WTUPCP_CameraPreview(Context context2, WTUPCP_CameraCallbacks wTUPCP_CameraCallbacks) {
        super(context2);
        this.context = context2;
        this.sharePreferenceUtils = new WTUPCP_SharePreferenceUtils(context2, "MySharedPref");
        this.mCameraCallbacks = wTUPCP_CameraCallbacks;
        initSurfaceView();
    }

    private void initSurfaceView() {
        SurfaceHolder holder = getHolder();
        this.mHolder = holder;
        holder.addCallback(this);
        this.mHolder.setType(3);
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        if (this.mCamera == null) {
            this.mCameraCallbacks.onCameraError(WTUPCP_CameraError.ERROR_CAMERA_OPEN_FAILED);
        } else if (surfaceHolder.getSurface() == null) {
            this.mCameraCallbacks.onCameraError(WTUPCP_CameraError.ERROR_CAMERA_OPEN_FAILED);
        } else {
            try {
                this.mCamera.stopPreview();
            } catch (Exception unused) {
            }
            Camera.Parameters parameters = this.mCamera.getParameters();
            List<Camera.Size> supportedPictureSizes = this.mCamera.getParameters().getSupportedPictureSizes();
            int resolution = this.mCameraConfig.getResolution();
            if (resolution == 2006) {
                Log.e("CAMERA_SIZE", "1");
                Camera.Size size = supportedPictureSizes.get(0);
            } else if (resolution == 7821) {
                Log.e("CAMERA_SIZE", ExifInterface.GPS_MEASUREMENT_2D);
                Camera.Size size2 = supportedPictureSizes.get(supportedPictureSizes.size() - 1);
            } else if (resolution == 7895) {
                Log.e("CAMERA_SIZE", ExifInterface.GPS_MEASUREMENT_3D);
                Camera.Size size3 = supportedPictureSizes.get(supportedPictureSizes.size() / 2);
            } else {
                throw new RuntimeException("Invalid camera resolution.");
            }
            initialCameraPictureSize(this.context, parameters);
            requestLayout();
            this.mCamera.setParameters(parameters);
            try {
                this.mCamera.setDisplayOrientation(90);
                this.mCamera.setPreviewDisplay(surfaceHolder);
                this.mCamera.startPreview();
                this.safeToTakePicture = true;
            } catch (IOException | NullPointerException unused2) {
                this.mCameraCallbacks.onCameraError(WTUPCP_CameraError.ERROR_CAMERA_OPEN_FAILED);
            }
        }
    }

    public static void initialCameraPictureSize(Context context2, Camera.Parameters parameters) {
        List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();
        if (supportedPictureSizes != null) {
            for (String str : context2.getResources().getStringArray(R.array.pref_camera_picturesize_entryvalues)) {
                if (setCameraPictureSize(str, supportedPictureSizes, parameters)) {
                    SharedPreferences.Editor edit = context2.getSharedPreferences("com.harteg.crookcatcher_preferences", 0).edit();
                    edit.putString("key_picturesize", str);
                    edit.apply();
                    return;
                }
            }
            Log.e("myTag", "No supported picture size found");
        }
    }

    public static boolean setCameraPictureSize(String str, List<Camera.Size> list, Camera.Parameters parameters) {
        int indexOf = str.indexOf(120);
        if (indexOf == -1) {
            return false;
        }
        int parseInt = Integer.parseInt(str.substring(0, indexOf));
        int parseInt2 = Integer.parseInt(str.substring(indexOf + 1));
        for (Camera.Size next : list) {
            if (next.width == parseInt && next.height == parseInt2) {
                parameters.setPictureSize(parseInt, parseInt2);
                return true;
            }
        }
        return false;
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Camera camera = this.mCamera;
        if (camera != null) {
            camera.stopPreview();
        }
    }

    public void startCameraInternal(WTUPCP_CameraConfig wTUPCP_CameraConfig) {
        this.mCameraConfig = wTUPCP_CameraConfig;
        if (!safeCameraOpen(wTUPCP_CameraConfig.getFacing())) {
            this.mCameraCallbacks.onCameraError(WTUPCP_CameraError.ERROR_CAMERA_OPEN_FAILED);
        } else if (this.mCamera != null) {
            requestLayout();
            try {
                this.mCamera.setPreviewDisplay(this.mHolder);
                this.mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
                this.mCameraCallbacks.onCameraError(WTUPCP_CameraError.ERROR_CAMERA_OPEN_FAILED);
            }
        }
    }

    private boolean safeCameraOpen(int i) {
        try {
            stopPreviewAndFreeCamera();
            Camera open = Camera.open(1);
            this.mCamera = open;
            return open != null;
        } catch (Exception e) {
            Log.e("CameraPreview", "failed to open Camera");
            Log.e("CameraPreview", e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean isSafeToTakePictureInternal() {
        Log.e("WT__SERVICE", "SAFE TO TAKE PICTURE => " + this.safeToTakePicture);
        return this.safeToTakePicture;
    }

    public void takePictureInternal() {
        this.safeToTakePicture = false;
        Log.e("WT__SERVICE", "PICTURE TAKEN.....");
        if (this.mCamera != null) {
            Log.e("WT__SERVICE", "CAMERA NOT NULL");
            this.mCamera.takePicture((Camera.ShutterCallback) null, (Camera.PictureCallback) null, new Camera.PictureCallback() {
                public void onPictureTaken(final byte[] bArr, Camera camera) {
                    new Thread(new Runnable() {
                        public void run() {
                            //byte[] bArr = bArr;
                            Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
                            if (WTUPCP_CameraPreview.this.mCameraConfig.getImageRotation() != 0) {
                                decodeByteArray = WTUPCP_HiddenCameraUtils.rotateBitmap(decodeByteArray, WTUPCP_CameraPreview.this.mCameraConfig.getImageRotation());
                            }
                            final DocumentFile access$100 = WTUPCP_CameraPreview.this.getFile(WTUPCP_CameraPreview.this.context.getResources().getString(R.string.captured_image));
                            if (WTUPCP_HiddenCameraUtils.saveImageFromFile(WTUPCP_CameraPreview.this.context, decodeByteArray, access$100, WTUPCP_CameraPreview.this.mCameraConfig.getImageFormat())) {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    public void run() {
                                        WTUPCP_CameraPreview.this.mCameraCallbacks.onImageCapture(access$100, WTUPCP_CameraPreview.this.mCameraConfig.getImageTime());
                                    }
                                });
                            } else {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    public void run() {
                                        WTUPCP_CameraPreview.this.mCameraCallbacks.onCameraError(WTUPCP_CameraError.ERROR_IMAGE_WRITE_FAILED);
                                    }
                                });
                            }
                            WTUPCP_CameraPreview.this.mCamera.startPreview();
                            boolean unused = WTUPCP_CameraPreview.this.safeToTakePicture = true;
                        }
                    }).start();
                }
            });
            return;
        }
        Log.e("WT__SERVICE", "CAMERA NOT NULL");
        this.mCameraCallbacks.onCameraError(WTUPCP_CameraError.ERROR_CAMERA_OPEN_FAILED);
        this.safeToTakePicture = true;
    }

    public void stopPreviewAndFreeCamera() {
        this.safeToTakePicture = false;
        Camera camera = this.mCamera;
        if (camera != null) {
            camera.stopPreview();
            this.mCamera.release();
            this.mCamera = null;
        }
    }

    
    public DocumentFile getFile(String str) {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences(WTUPCP_Constants.PREFERENCE_NAME, 0);
        DocumentFile.fromTreeUri(this.context, Uri.parse(sharedPreferences.getString(WTUPCP_Constants.FOLDER_URI, "")));
        DocumentFile fromTreeUri = DocumentFile.fromTreeUri(this.context, Uri.parse(sharedPreferences.getString(WTUPCP_Constants.GIVEN_SCOPE, "")));
        if (fromTreeUri == null || !fromTreeUri.exists()) {
            return null;
        }
        DocumentFile findFile = fromTreeUri.findFile(str);
        if (findFile == null || !findFile.exists()) {
            findFile = fromTreeUri.createDirectory(str);
        }
        Log.e("folder", findFile.getName());
        return findFile.createFile("image/*", System.currentTimeMillis() + ".jpg");
    }
}
