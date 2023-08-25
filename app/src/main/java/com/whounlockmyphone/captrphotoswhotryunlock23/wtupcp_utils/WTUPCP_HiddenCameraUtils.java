package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import androidx.documentfile.provider.DocumentFile;

public class WTUPCP_HiddenCameraUtils {
    public static String facing2String(int i) {
        return i != 0 ? i != 1 ? "UNKNOWN" : "FRONT" : "BACK";
    }

    public static String resolution2String(int i) {
        return i != 2006 ? i != 7821 ? i != 7895 ? "UNKNOWN" : "MED" : "LOW" : "HIGH";
    }

    public static boolean isFrontCameraAvailable(Context context) {
        return Camera.getNumberOfCameras() > 0 && context.getPackageManager().hasSystemFeature("android.hardware.camera.front");
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int i) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) i);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static boolean saveImageFromFile(Context context, Bitmap bitmap, DocumentFile documentFile, int i) {
        Bitmap.CompressFormat compressFormat;
        if (i == 563) {
            compressFormat = Bitmap.CompressFormat.WEBP;
        } else if (i != 849) {
            compressFormat = Bitmap.CompressFormat.PNG;
        } else {
            compressFormat = Bitmap.CompressFormat.JPEG;
        }
        try {
            bitmap.compress(compressFormat, 100, context.getContentResolver().openOutputStream(documentFile.getUri()));
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }
}
