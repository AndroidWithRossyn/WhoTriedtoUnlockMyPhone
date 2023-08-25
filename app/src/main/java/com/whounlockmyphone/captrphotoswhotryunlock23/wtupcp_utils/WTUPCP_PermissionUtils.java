package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import androidx.core.content.ContextCompat;

public class WTUPCP_PermissionUtils {
    public static boolean isUsageStatsGranted(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
            if (((AppOpsManager) context.getSystemService("appops")).checkOpNoThrow("android:get_usage_stats", applicationInfo.uid, applicationInfo.packageName) == 0) {
                return true;
            }
            return false;
        } catch (PackageManager.NameNotFoundException unused) {
            Log.e("ERROR", "NameNotFoundException");
            return false;
        }
    }

    public static boolean refreshIsReadyStatus(Activity activity) {
        if (Build.VERSION.SDK_INT < 21) {
            return true;
        }
        boolean isUsageStatsGranted = isUsageStatsGranted(activity);
        if (Build.VERSION.SDK_INT < 23) {
            return isUsageStatsGranted;
        }
        if (Build.VERSION.SDK_INT < 23 || !isUsageStatsGranted || !isSysAlertWndwGranted(activity) || ContextCompat.checkSelfPermission(activity, "android.permission.CAMERA") != 0 || ContextCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            return false;
        }
        return true;
    }

    public static boolean isSysAlertWndwGranted(Context context) {
        return Build.VERSION.SDK_INT < 23 || Settings.canDrawOverlays(context);
    }
}
