package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.whounlockmyphone.captrphotoswhotryunlock23.R;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_manager.WTUPCP_PolicyManager;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_Constants;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_PermissionUtils;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_SharePreferenceUtils;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_Utils;
import pub.devrel.easypermissions.EasyPermissions;

public class WTUPCP_SplashActivity extends AppCompatActivity {
    Handler handler;
    WTUPCP_PolicyManager policyManager;
    Runnable runnable = new Runnable() {
        public void run() {
            String string = WTUPCP_SplashActivity.this.sharePreferenceUtils.getString(WTUPCP_Constants.FOLDER_URI, "");
            if (Build.VERSION.SDK_INT > 28) {
                if (!WTUPCP_SplashActivity.this.policyManager.isAdminActive() || !WTUPCP_Utils.isUsageAppAccessGranted(WTUPCP_SplashActivity.this) || string.length() <= 0 || !EasyPermissions.hasPermissions(WTUPCP_SplashActivity.this, WTUPCP_Constants.camera_storage_Permission_new) || !WTUPCP_PermissionUtils.isSysAlertWndwGranted(WTUPCP_SplashActivity.this)) {
                    WTUPCP_SplashActivity.this.startActivity(new Intent(WTUPCP_SplashActivity.this, WTUPCP_PermissionActivity.class));
                } else {
                    WTUPCP_SplashActivity.this.startActivity(new Intent(WTUPCP_SplashActivity.this, WTUPCP_HomeActivity.class));
                }
            } else if (!WTUPCP_SplashActivity.this.policyManager.isAdminActive() || !WTUPCP_Utils.isUsageAppAccessGranted(WTUPCP_SplashActivity.this) || string.length() <= 0 || !EasyPermissions.hasPermissions(WTUPCP_SplashActivity.this, WTUPCP_Constants.camera_storage_Permission) || !WTUPCP_PermissionUtils.isSysAlertWndwGranted(WTUPCP_SplashActivity.this)) {
                WTUPCP_SplashActivity.this.startActivity(new Intent(WTUPCP_SplashActivity.this, WTUPCP_PermissionActivity.class));
            } else {
                WTUPCP_SplashActivity.this.startActivity(new Intent(WTUPCP_SplashActivity.this, WTUPCP_HomeActivity.class));
            }
            WTUPCP_SplashActivity.this.finish();
        }
    };
    WTUPCP_SharePreferenceUtils sharePreferenceUtils;

    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        setContentView((int) R.layout.wtupcp_activity_splash);
        this.policyManager = new WTUPCP_PolicyManager(this);
        this.sharePreferenceUtils = new WTUPCP_SharePreferenceUtils(this, WTUPCP_Constants.PREFERENCE_NAME);
        this.handler = new Handler(Looper.getMainLooper());
    }

    
    public void onStop() {
        super.onStop();
        this.handler.removeCallbacks(this.runnable);
    }

    
    public void onResume() {
        super.onResume();
        this.handler.postDelayed(this.runnable, 2000);
    }
}
