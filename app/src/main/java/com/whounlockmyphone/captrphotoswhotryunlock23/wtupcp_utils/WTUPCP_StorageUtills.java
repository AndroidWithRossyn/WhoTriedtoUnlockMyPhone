package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class WTUPCP_StorageUtills {
    String Package_name;
    private Context kContext;

    public WTUPCP_StorageUtills(Context context) {
        this.kContext = context;
        this.Package_name = context.getPackageName();
    }

    private File getStorageDir(String str) {
        File file = new File(Environment.getExternalStorageDirectory(), str);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public File storeToDirectory(String str, String str2) {
        return new File(getStorageDir(str), str2);
    }

    private File getPackageStorageDir(String str) {
        try {
            File file = new File(this.kContext.getFilesDir() + "/", str);
            if (!file.exists()) {
                file.mkdirs();
            }
            return file;
        } catch (Exception unused) {
            return null;
        }
    }

    public File storeToPackageDirectory(String str, String str2) {
        try {
            return new File(getPackageStorageDir(str), str2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean fileDelete(String str) {
        return Boolean.valueOf(new File(str).delete());
    }

    public static void deleteFileFromMediaStore(ContentResolver contentResolver, File file) {
        String str;
        try {
            str = file.getCanonicalPath();
        } catch (IOException unused) {
            str = file.getAbsolutePath();
        }
        Uri contentUri = MediaStore.Files.getContentUri("external");
        if (contentResolver.delete(contentUri, "_data=?", new String[]{str}) == 0) {
            String absolutePath = file.getAbsolutePath();
            if (!absolutePath.equals(str)) {
                contentResolver.delete(contentUri, "_data=?", new String[]{absolutePath});
            }
        }
    }

    public File getCreateStorageDirectoryPath(String str) {
        return getStorageDir(str);
    }

    public File getCreatePackageStorageDirectoryPath(String str) {
        return getPackageStorageDir(str);
    }

    public void copyAssetsFileToPackageFile(String str, String str2, String str3) {
        try {
            InputStream open = this.kContext.getAssets().open(str);
            FileOutputStream fileOutputStream = new FileOutputStream(storeToPackageDirectory(str2, str3));
            copyFile(open, fileOutputStream);
            open.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap getBitmapFromView(View view) {
        Bitmap createBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Drawable background = view.getBackground();
        if (background != null) {
            background.draw(canvas);
        }
        view.draw(canvas);
        return createBitmap;
    }

    public void saveBitmapPNG(Bitmap bitmap, File file) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public void saveBitmapJPG(Bitmap bitmap, File file) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public void refreshGallery(Activity activity, File file) {
        if (Build.VERSION.SDK_INT >= 19) {
            Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            intent.setData(Uri.fromFile(file));
            activity.sendBroadcast(intent);
            return;
        }
        activity.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.parse("file://" + Environment.getExternalStorageDirectory())));
    }

    public void copyFile(InputStream inputStream, OutputStream outputStream) {
        try {
            byte[] bArr = new byte[1024];
            while (true) {
                int read = inputStream.read(bArr);
                if (read != -1) {
                    outputStream.write(bArr, 0, read);
                } else {
                    return;
                }
            }
        } catch (Exception unused) {
        }
    }

    public Bitmap viewToBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap createBitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.draw(new Canvas(createBitmap));
        return createBitmap;
    }
}
