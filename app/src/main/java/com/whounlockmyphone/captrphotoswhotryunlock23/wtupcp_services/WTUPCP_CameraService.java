package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_services;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.documentfile.provider.DocumentFile;
import com.whounlockmyphone.captrphotoswhotryunlock23.R;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_extra.WTUPCP_IntentFactory;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_CameraConfig;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_Constants;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_PermissionUtils;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_SharePreferenceUtils;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_StorageUtills;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.config.WTUPCP_CameraImageFormat;

public class WTUPCP_CameraService extends WTUPCP_HiddenCameraService {
    boolean mIsWrongPattern;
    WTUPCP_SharePreferenceUtils sharePreferenceUtils;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public static boolean isServiceRunning(Context context) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) context.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE)) {
            if (WTUPCP_CameraService.class.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void go2NextStep(String str, Long l) {
        WTUPCP_SharePreferenceUtils wTUPCP_SharePreferenceUtils = this.sharePreferenceUtils;
        wTUPCP_SharePreferenceUtils.putBoolean(WTUPCP_Constants.APP_SESSION_IS_RECORDING, true);
        wTUPCP_SharePreferenceUtils.putLong(WTUPCP_Constants.CURRENT_SESSION_BEGIN, l);
        wTUPCP_SharePreferenceUtils.putString(WTUPCP_Constants.CURRENT_SESSION_PHOTO, str);
        if (this.mIsWrongPattern) {
            startService(new WTUPCP_IntentFactory(this).getSaveRepServiceIntent(true, false));
        } else {
            startService(new WTUPCP_IntentFactory(this).getSaveRepServiceIntent(false, false));
        }
        stopSelf();
    }

    private void stopServiceWithError() {
        stopSelf();
    }

    public void onCameraError(int i) {
        Log.e("CAMERA_ERROR", "" + i);
        if (i == 1122) {
            stopServiceWithError();
        } else if (i == 3136) {
            stopServiceWithError();
        } else if (i == 5472) {
            stopServiceWithError();
        } else if (i == 8722) {
            Toast.makeText(this, "Your device does not have front camera :( Camera is stopped", 1).show();
            stopServiceWithError();
        } else if (i == 9854) {
            stopServiceWithError();
        }
    }

    public void onImageCapture(DocumentFile documentFile, Long l) {
        documentFile.getName();
        go2NextStep(documentFile.getUri().toString(), l);
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        this.sharePreferenceUtils = new WTUPCP_SharePreferenceUtils(this, WTUPCP_Constants.PREFERENCE_NAME);
        this.mIsWrongPattern = intent.getBooleanExtra(WTUPCP_Constants.WRONG_PATTERN_EXTRA, false);
        WTUPCP_StorageUtills wTUPCP_StorageUtills = new WTUPCP_StorageUtills(this);
        String string = getResources().getString(R.string.app_name);
        wTUPCP_StorageUtills.storeToDirectory(string, System.currentTimeMillis() + ".jpg");
        Log.e("WT__SERVICE", "CAMERA RUNNING OUT.....");
        if (WTUPCP_PermissionUtils.isSysAlertWndwGranted(this)) {
            Log.e("WT__SERVICE", "CAMERA RUNNING.....");
            startCamera(new WTUPCP_CameraConfig().getBuilder(this).setCameraFacing(this.sharePreferenceUtils.getInt(WTUPCP_Constants.PHOTO_CAMERA_PREF, WTUPCP_Constants.PHOTO_CAMERA_DEFAULT)).setCameraResolution(this.sharePreferenceUtils.getInt(WTUPCP_Constants.PHOTO_RES_PREF, WTUPCP_Constants.PHOTO_RES_DEFAULT)).setImageRotation(this.sharePreferenceUtils.getInt(WTUPCP_Constants.PHOTO_ROTATION_PREF, WTUPCP_Constants.PHOTO_ROTATION_DEFAULT)).setImageFormat(WTUPCP_CameraImageFormat.FORMAT_JPEG).build());
            new Handler().postDelayed(new Runnable() {
                public final void run() {
                    WTUPCP_CameraService.this.takePicture();
                }
            }, 500);
            return 2;
        }
        stopServiceWithError();
        return 2;
    }

    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26) {
            ((NotificationManager) getSystemService("notification")).createNotificationChannel(new NotificationChannel("my_channel_01", "Camera Service Running...", 3));
            startForeground(11, new NotificationCompat.Builder(this, "my_channel_01").setContentTitle("").setContentText("").build());
        }
    }
}
