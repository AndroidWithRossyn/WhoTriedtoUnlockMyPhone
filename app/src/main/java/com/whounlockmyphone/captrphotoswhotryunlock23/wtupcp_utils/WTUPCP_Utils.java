package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import androidx.exifinterface.media.ExifInterface;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_services.WTUPCP_SaveReportService;
import java.io.IOException;
import java.util.Date;

public class WTUPCP_Utils {
    public static boolean flash_stop;
    public static boolean is_vibrating;
    public static boolean vibrate_stop;

    public static boolean checkDeviceWasUnlocked(Context context, boolean z) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(WTUPCP_Constants.PREFERENCE_NAME, 0);
        String string = sharedPreferences.getString(WTUPCP_Constants.PRE_LAST_SCREEN_EVENT_TYPE, "android.intent.action.SCREEN_ON");
        String string2 = sharedPreferences.getString(WTUPCP_Constants.LAST_SCREEN_EVENT_TYPE, "android.intent.action.SCREEN_OFF");
        if (!string.equals("android.intent.action.USER_PRESENT") && !string.equals("android.intent.action.SCREEN_ON")) {
            return false;
        }
        if (string2.equals("android.intent.action.USER_PRESENT") || string2.equals("android.intent.action.SCREEN_ON")) {
            return true;
        }
        return false;
    }

    public static boolean isUsageAppAccessGranted(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
            if ((Build.VERSION.SDK_INT > 19 ? ((AppOpsManager) context.getSystemService("appops")).checkOpNoThrow("android:get_usage_stats", applicationInfo.uid, applicationInfo.packageName) : 0) == 0) {
                return true;
            }
            return false;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    public static String getAppNameFromPkgName(Context context, String str) {
        try {
            PackageManager packageManager = context.getPackageManager();
            return (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(str, 128));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void stop_alarm() {
        try {
            if (WTUPCP_SaveReportService.mPlayer != null && WTUPCP_SaveReportService.mPlayer.isPlaying()) {
                WTUPCP_SaveReportService.mPlayer.stop();
            }
            vibrate_stop = true;
            flash_stop = true;
            vibrate_stop = true;
            is_vibrating = false;
            if (WTUPCP_SaveReportService.vibrator != null) {
                WTUPCP_SaveReportService.vibrator.cancel();
            }
            if (WTUPCP_SaveReportService.mNotificationmanager_alarm != null) {
                WTUPCP_SaveReportService.mNotificationmanager_alarm.cancel(WTUPCP_SaveReportService.Sound_CHANNEL_ID, 1);
            }
        } catch (Exception e) {
            Log.e("in_Utilities ", "Exception_stop_alarm " + e);
        }
    }

    public static class Helper_ExifGeoConverter {
        Float Latitude;
        Float Longitude;
        private boolean valid = false;

        public Helper_ExifGeoConverter(ExifInterface exifInterface) {
            String attribute = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String attribute2 = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            String attribute3 = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            String attribute4 = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
            String replace = attribute.replace("-", "");
            String replace2 = attribute3.replace("-", "");
            if (replace != null && attribute2 != null && replace2 != null && attribute4 != null) {
                this.valid = true;
                if (attribute2.equals("N")) {
                    this.Latitude = convertToDegree(replace);
                } else {
                    this.Latitude = Float.valueOf(0.0f - convertToDegree(replace).floatValue());
                }
                if (attribute4.equals(ExifInterface.LONGITUDE_EAST)) {
                    this.Longitude = convertToDegree(replace2);
                } else {
                    this.Longitude = Float.valueOf(0.0f - convertToDegree(replace2).floatValue());
                }
            }
        }

        private Float convertToDegree(String str) {
            String[] split = str.split(",", 3);
            String[] split2 = split[0].split("/", 2);
            Double valueOf = Double.valueOf(Double.valueOf(split2[0]).doubleValue() / Double.valueOf(split2[1]).doubleValue());
            String[] split3 = split[1].split("/", 2);
            Double valueOf2 = Double.valueOf(Double.valueOf(split3[0]).doubleValue() / Double.valueOf(split3[1]).doubleValue());
            String[] split4 = split[2].split("/", 2);
            return new Float(valueOf.doubleValue() + (valueOf2.doubleValue() / 60.0d) + (Double.valueOf(Double.valueOf(split4[0]).doubleValue() / Double.valueOf(split4[1]).doubleValue()).doubleValue() / 3600.0d));
        }

        public String toString() {
            return String.valueOf(this.Latitude) + ", " + String.valueOf(this.Longitude);
        }

        public float getLatitude() {
            return this.Latitude.floatValue();
        }

        public float getLongitude() {
            return this.Longitude.floatValue();
        }
    }

    public static Boolean writeExifData(Context context, Location location, Date date, String str) {
        String str2;
        String str3;
        String str4 = str;
        boolean z = location != null;
        Log.e("AlertService", "isLocationOn image: " + z);
        try {
            ExifInterface exifInterface = new ExifInterface(str4);
            if (z) {
                double latitude = location.getLatitude();
                String[] split = Location.convert(Math.abs(latitude), 2).split(":");
                String[] split2 = split[2].split("\\.");
                if (split2.length == 0) {
                    str2 = split[2];
                } else {
                    str2 = split2[0];
                }
                exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE, split[0] + "/1," + split[1] + "/1," + str2 + "/1");
                exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, latitude > 0.0d ? "N" : ExifInterface.LATITUDE_SOUTH);
                double longitude = location.getLongitude();
                String[] split3 = Location.convert(Math.abs(longitude), 2).split(":");
                String[] split4 = split3[2].split("\\.");
                if (split4.length == 0) {
                    str3 = split3[2];
                } else {
                    str3 = split4[0];
                }
                exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, split3[0] + "/1," + split3[1] + "/1," + str3 + "/1");
                exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, longitude > 0.0d ? ExifInterface.LONGITUDE_EAST : ExifInterface.LONGITUDE_WEST);
                exifInterface.setAttribute(ExifInterface.TAG_USER_COMMENT, "accuracy=" + String.valueOf(Math.round(location.getAccuracy())));
            }
            exifInterface.setAttribute(ExifInterface.TAG_DATETIME, date.toString());
            exifInterface.saveAttributes();
            Log.e("AlertService", "GeoTagged image: " + str4);
            return true;
        } catch (IOException e) {
            Log.e("AlertService", "Failed to write Exif data  " + e.getLocalizedMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getDeviceName() {
        String str = Build.MANUFACTURER;
        String str2 = Build.MODEL;
        if (str2.startsWith(str)) {
            return capitalizeFirstLetterInEveryWord(str2);
        }
        return capitalizeFirstLetterInEveryWord(str) + " " + str2;
    }

    private static String capitalizeFirstLetterInEveryWord(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        char charAt = str.charAt(0);
        if (Character.isUpperCase(charAt)) {
            return str;
        }
        return Character.toUpperCase(charAt) + str.substring(1);
    }
}
