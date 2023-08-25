package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_receivers;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_extra.WTUPCP_IntentFactory;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_services.WTUPCP_CameraService;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_Constants;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_SharePreferenceUtils;

public class WTUPCP_AdminReceiver extends DeviceAdminReceiver {

    int value = 0;

    public void onPasswordFailed(Context context, Intent intent) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService("device_policy");
        this.value = devicePolicyManager != null ? devicePolicyManager.getCurrentFailedPasswordAttempts() : 0;
        WTUPCP_SharePreferenceUtils wTUPCP_SharePreferenceUtils = new WTUPCP_SharePreferenceUtils(context, WTUPCP_Constants.PREFERENCE_NAME);
        if (wTUPCP_SharePreferenceUtils.getBoolean(WTUPCP_Constants.SERVICE_ON, Boolean.valueOf(WTUPCP_Constants.SERVICE_ON_DEFAULT)) && wTUPCP_SharePreferenceUtils.getInt(WTUPCP_Constants.SETTING_NO_OF_ATTEMPS, WTUPCP_Constants.SETTING_NO_OF_ATTEMPS_DEFAULT) == this.value) {
            if (wTUPCP_SharePreferenceUtils.getBoolean(WTUPCP_Constants.SETTING_FAIL_UNLOCK_ATTEMPSTS, Boolean.valueOf(WTUPCP_Constants.SETTING_FAIL_UNLOCK_ATTEMPSTS_DEFAULT))) {
                if (!WTUPCP_CameraService.isServiceRunning(context)) {
                    if (Build.VERSION.SDK_INT >= 26) {
                        context.startForegroundService(new WTUPCP_IntentFactory(context).getCameraServiceIntent(true));
                    } else {
                        context.startService(new WTUPCP_IntentFactory(context).getCameraServiceIntent(true));
                    }
                }
                Log.e("WT__SERVICE", "ELSE");
            }
            this.value = 0;
        }
    }
}
