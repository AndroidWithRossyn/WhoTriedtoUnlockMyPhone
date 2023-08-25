package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_Constants;
import java.util.List;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class WTUPCP_BaseActivity extends AppCompatActivity {
    private final int REQUEST_CODE_PATH_TO_DATA = 54;
    private final int STORAGE_PERMISSION = 32;
    StorageCallbacks callbacks;
    EasyPermissions.PermissionCallbacks permissionCallbacks;
    String requestName;
    SharedPreferences sharedPreferences;

    public interface StorageCallbacks {
        void onFolderAccessGranted(DocumentFile documentFile);
    }

    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.sharedPreferences = getSharedPreferences(WTUPCP_Constants.PREFERENCE_NAME, 0);
        this.permissionCallbacks = new EasyPermissions.PermissionCallbacks() {
            public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
            }

            public void onPermissionsGranted(int i, List<String> list) {
                if (i == 32) {
                    WTUPCP_BaseActivity.this.selectIfHavePermission();
                }
            }

            public void onPermissionsDenied(int i, List<String> list) {
                if (EasyPermissions.somePermissionPermanentlyDenied((Activity) WTUPCP_BaseActivity.this, list)) {
                    new AppSettingsDialog.Builder((Activity) WTUPCP_BaseActivity.this).build().show();
                } else {
                    EasyPermissions.requestPermissions((Activity) WTUPCP_BaseActivity.this, "Grant permission to use app!", 32, "android.permission.WRITE_EXTERNAL_STORAGE");
                }
            }
        };
    }

    public boolean checkStorage() {
        return this.sharedPreferences.contains("FOLDER_URI");
    }

    public void requestMainFolderAccess(String str, StorageCallbacks storageCallbacks) {
        if (checkStorage()) {
            DocumentFile fromTreeUri = DocumentFile.fromTreeUri(this, Uri.parse(this.sharedPreferences.getString(WTUPCP_Constants.FOLDER_URI, "")));
            Log.e("folder_", fromTreeUri.getName());
            if (fromTreeUri == null || !fromTreeUri.exists() || !fromTreeUri.getName().equals(str)) {
                Log.e("folder_", "ELSE");
                DocumentFile fromTreeUri2 = DocumentFile.fromTreeUri(this, Uri.parse(this.sharedPreferences.getString(WTUPCP_Constants.GIVEN_SCOPE, "")));
                if (fromTreeUri2 != null && fromTreeUri2.exists()) {
                    DocumentFile findFile = fromTreeUri2.findFile(str);
                    if (findFile == null || !findFile.exists()) {
                        findFile = fromTreeUri2.createDirectory(str);
                    }
                    Log.e("folder", findFile.getName());
                    storageCallbacks.onFolderAccessGranted(findFile);
                    return;
                }
                return;
            }
            Log.e("folder_", "IF");
            storageCallbacks.onFolderAccessGranted(fromTreeUri);
        } else if (getContentResolver().getPersistedUriPermissions().isEmpty()) {
            this.requestName = str;
            this.callbacks = storageCallbacks;
            Log.e("folder_1", "here req");
            if (Build.VERSION.SDK_INT < 29) {
                selectIfHavePermission();
            } else {
                startActivityForResult(new Intent("android.intent.action.OPEN_DOCUMENT_TREE"), 54);
            }
        } else {
            Uri uri = getContentResolver().getPersistedUriPermissions().get(0).getUri();
            Log.e("folder_1", uri.toString() + "abc");
            this.sharedPreferences.edit().putString(WTUPCP_Constants.GIVEN_SCOPE, uri.toString()).apply();
            DocumentFile fromTreeUri3 = DocumentFile.fromTreeUri(this, uri);
            if (fromTreeUri3.exists()) {
                DocumentFile findFile2 = fromTreeUri3.findFile(str);
                if (findFile2 == null || !findFile2.exists()) {
                    findFile2 = fromTreeUri3.createDirectory(str);
                    Log.e("folder__2", findFile2.getName());
                    this.sharedPreferences.edit().putString(WTUPCP_Constants.FOLDER_URI, findFile2.getUri().toString()).apply();
                } else {
                    Log.e("folder__1", findFile2.getName());
                    this.sharedPreferences.edit().putString(WTUPCP_Constants.FOLDER_URI, findFile2.getUri().toString()).apply();
                }
                storageCallbacks.onFolderAccessGranted(findFile2);
                return;
            }
            this.requestName = str;
            this.callbacks = storageCallbacks;
            if (Build.VERSION.SDK_INT < 29) {
                selectIfHavePermission();
            } else {
                startActivityForResult(new Intent("android.intent.action.OPEN_DOCUMENT_TREE"), 54);
            }
        }
    }

    
    @AfterPermissionGranted(32)
    public void selectIfHavePermission() {
        if (EasyPermissions.hasPermissions(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            startActivityForResult(new Intent("android.intent.action.OPEN_DOCUMENT_TREE"), 54);
        } else {
            EasyPermissions.requestPermissions((Activity) this, "Grant permission to save meme audio.", 32, "android.permission.WRITE_EXTERNAL_STORAGE");
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        EasyPermissions.onRequestPermissionsResult(i, strArr, iArr, this.permissionCallbacks);
    }

    
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 54) {
            if (i2 == -1 && intent != null) {
                DocumentFile fromTreeUri = DocumentFile.fromTreeUri(this, intent.getData());
                grantUriPermission(getPackageName(), intent.getData(), 3);
                getContentResolver().takePersistableUriPermission(intent.getData(), 3);
                this.sharedPreferences.edit().putString(WTUPCP_Constants.GIVEN_SCOPE, intent.getData().toString()).apply();
                DocumentFile findFile = fromTreeUri.findFile(this.requestName);
                if (findFile == null || !findFile.exists()) {
                    findFile = fromTreeUri.createDirectory(this.requestName);
                }
                Log.e("folder__3", findFile.getName());
                this.sharedPreferences.edit().putString(WTUPCP_Constants.FOLDER_URI, findFile.getUri().toString()).apply();
                this.callbacks.onFolderAccessGranted(findFile);
            }
        } else if (i == 16061) {
            selectIfHavePermission();
        }
    }
}
